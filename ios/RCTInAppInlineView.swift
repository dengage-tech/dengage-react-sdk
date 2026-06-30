import UIKit
import WebKit
import React
import Dengage

@objc public class RCTInAppInlineView: RCTView {

    @objc public var propertyId: String? {
        didSet { scheduleApplyInline() }
    }
    @objc public var screenName: String? {
        didSet { scheduleApplyInline() }
    }
    @objc public var customParams: [String: String]? {
        didSet { scheduleApplyInline() }
    }
    @objc public var hideIfNotFound: Bool = true {
        didSet { scheduleApplyInline() }
    }

    @objc public var onInlineVisibilityChanged: RCTDirectEventBlock?

    private let inAppInlineElementView: InAppInlineElementView = {
        let cfg = WKWebViewConfiguration()
        let wv = InAppInlineElementView(frame: .zero, configuration: cfg)
        wv.translatesAutoresizingMaskIntoConstraints = false
        return wv
    }()

    private var heightConstraint: NSLayoutConstraint!
    private var lastAppliedConfigurationSignature: String?

    private var pendingApplyWorkItem: DispatchWorkItem?
    private var pendingShowWorkItem: DispatchWorkItem?

    private var pollTimer: Timer?
    private var lastReportedHidden: Bool?
    private var hiddenSinceUptime: TimeInterval?
    private let hiddenDebounceSec: TimeInterval = 0
    private let pollIntervalSec: TimeInterval = 0.05

    override init(frame: CGRect) {
        super.init(frame: frame)
        commonInit()
    }

    required init?(coder aDecoder: NSCoder) {
        super.init(coder: aDecoder)
        commonInit()
    }

    deinit {
        stopVisibilityPolling()
    }

    private func commonInit() {
        backgroundColor = .clear
        clipsToBounds = true
        inAppInlineElementView.navigationDelegate = self
        addSubview(inAppInlineElementView)
        NSLayoutConstraint.activate([
            inAppInlineElementView.leadingAnchor.constraint(equalTo: self.leadingAnchor),
            inAppInlineElementView.trailingAnchor.constraint(equalTo: self.trailingAnchor),
            inAppInlineElementView.topAnchor.constraint(equalTo: self.topAnchor)
        ])
        // Start at 0 so we do not draw a 1pt “hairline” before content; expand in WKNavigationDelegate.
        heightConstraint = inAppInlineElementView.heightAnchor.constraint(equalToConstant: 0)
        heightConstraint.isActive = true
    }

    public override func didMoveToWindow() {
        super.didMoveToWindow()
        if window != nil {
            scheduleApplyInline()
            startVisibilityPolling()
        } else {
            stopVisibilityPolling()
        }
    }

    private func configurationSignature() -> String? {
        guard let p = propertyId, let s = screenName else { return nil }
        let params = customParams ?? [:]
        let paramsPart = params.keys.sorted().map { k in "\(k)=\(params[k] ?? "")" }.joined(separator: "\u{1}")
        return "\(p)\u{0}\(s)\u{0}\(paramsPart)\u{0}\(hideIfNotFound)"
    }

    private func scheduleApplyInline() {
        pendingApplyWorkItem?.cancel()
        pendingShowWorkItem?.cancel()
        let item = DispatchWorkItem { [weak self] in
            self?.applyInlineIfReady()
        }
        pendingApplyWorkItem = item
        DispatchQueue.main.async(execute: item)
    }

    private func resetInlineWebContent() {
        lastReportedHidden = nil
        hiddenSinceUptime = nil
        inAppInlineElementView.stopLoading()
        if let blank = URL(string: "about:blank") {
            inAppInlineElementView.load(URLRequest(url: blank))
        }
        heightConstraint.constant = 0
        invalidateIntrinsicContentSize()
    }

    /// Dengage SDK hides “not found” inline via `isHidden` (and zero frame). Use `isHidden` only — zero
    /// `bounds` are also true before first layout, which would falsely report hidden.
    private var isInlineEffectivelyHidden: Bool {
        inAppInlineElementView.isHidden
    }

    private func applyInlineIfReady() {
        guard window != nil else { return }
        guard let signature = configurationSignature() else { return }
        if signature == lastAppliedConfigurationSignature {
            return
        }
        let reloading = lastAppliedConfigurationSignature != nil
        if reloading {
            resetInlineWebContent()
        }
        lastAppliedConfigurationSignature = signature

        let runShow = { [weak self] in
            guard let self = self else { return }
            guard self.configurationSignature() == signature else { return }
            Dengage.showInAppInLine(
                propertyID: self.propertyId,
                inAppInlineElement: self.inAppInlineElementView,
                screenName: self.screenName,
                customParams: self.customParams,
                hideIfNotFound: self.hideIfNotFound
            )
        }

        if reloading {
            pendingShowWorkItem?.cancel()
            let showItem = DispatchWorkItem { runShow() }
            pendingShowWorkItem = showItem
            DispatchQueue.main.async(execute: showItem)
        } else {
            runShow()
        }

        // SDK may apply hide asynchronously; re-sync visibility after layout passes.
        DispatchQueue.main.async { [weak self] in
            self?.emitVisibilityFromCurrentState()
        }
        DispatchQueue.main.asyncAfter(deadline: .now() + 0.2) { [weak self] in
            self?.emitVisibilityFromCurrentState()
        }
    }

    private func emitVisibilityFromCurrentState() {
        let hidden = isInlineEffectivelyHidden
        if lastReportedHidden == nil || lastReportedHidden != hidden {
            lastReportedHidden = hidden
            applyCollapsedNativeLayout(hidden: hidden)
            onInlineVisibilityChanged?(["isHidden": hidden])
        }
    }

    private func applyCollapsedNativeLayout(hidden: Bool) {
        if hidden {
            heightConstraint.constant = 0
        }
        invalidateIntrinsicContentSize()
    }

    private func startVisibilityPolling() {
        stopVisibilityPolling()
        lastReportedHidden = nil
        hiddenSinceUptime = nil
        let timer = Timer(timeInterval: pollIntervalSec, repeats: true) { [weak self] _ in
            guard let self = self else { return }
            let rawHidden = self.isInlineEffectivelyHidden
            let now = ProcessInfo.processInfo.systemUptime
            let debouncedHidden: Bool
            if rawHidden {
                if self.hiddenSinceUptime == nil {
                    self.hiddenSinceUptime = now
                }
                debouncedHidden = (now - (self.hiddenSinceUptime ?? now)) >= self.hiddenDebounceSec
            } else {
                self.hiddenSinceUptime = nil
                debouncedHidden = false
            }
            if self.lastReportedHidden == nil || self.lastReportedHidden != debouncedHidden {
                self.lastReportedHidden = debouncedHidden
                self.applyCollapsedNativeLayout(hidden: debouncedHidden)
                self.onInlineVisibilityChanged?(["isHidden": debouncedHidden])
            }
        }
        RunLoop.main.add(timer, forMode: .common)
        pollTimer = timer
    }

    private func stopVisibilityPolling() {
        pollTimer?.invalidate()
        pollTimer = nil
    }

    public override var intrinsicContentSize: CGSize {
        if isInlineEffectivelyHidden {
            return CGSize(width: UIView.noIntrinsicMetric, height: 0)
        }
        return CGSize(width: UIView.noIntrinsicMetric,
                      height: heightConstraint.constant)
    }
}

extension RCTInAppInlineView: WKNavigationDelegate {
    public func webView(_ webView: WKWebView, didFinish navigation: WKNavigation!) {
        if isInlineEffectivelyHidden {
            heightConstraint.constant = 0
            invalidateIntrinsicContentSize()
            return
        }
        webView.evaluateJavaScript("document.body.scrollHeight") { [weak self] result, _ in
            guard let self = self, let h = result as? CGFloat else { return }
            if self.isInlineEffectivelyHidden {
                self.heightConstraint.constant = 0
            } else {
                self.heightConstraint.constant = max(h, 1)
            }
            self.setNeedsLayout()
            self.invalidateIntrinsicContentSize()
        }
    }
}
