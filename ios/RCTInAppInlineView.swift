import UIKit
import WebKit
import React
import Dengage

@objc public class RCTInAppInlineView: RCTView {

    private var didSetup = false
    static let viewTag = 1789

    @objc public var propertyId: String? {
        didSet { syncInAppInlineIfReady() }
    }

    @objc public var screenName: String? {
        didSet { syncInAppInlineIfReady() }
    }

    @objc public var customParams: [String: String]? {
        didSet { syncInAppInlineIfReady() }
    }

    /// When true (default), native hides the inline WebView if no message matches; we poll and notify JS to collapse layout.
    /// Prefer setting via `RCT_CUSTOM_VIEW_PROPERTY` in ObjC so RN BOOL/NSNumber bridges reliably.
    @objc public var hideIfNotFound: Bool = true {
        didSet { syncInAppInlineIfReady() }
    }

    @objc public dynamic var onVisibilityChanged: RCTDirectEventBlock?

    private let inAppInlineElementView: InAppInlineElementView = {
        let cfg = WKWebViewConfiguration()
        let wv = InAppInlineElementView(frame: .zero, configuration: cfg)
        wv.translatesAutoresizingMaskIntoConstraints = false
        return wv
    }()

    private var heightConstraint: NSLayoutConstraint!
    private var pollTimer: Timer?
    private var lastReportedHidden: Bool?
    private var hiddenSinceUptime: TimeInterval?
    private let hiddenDebounceSec: TimeInterval = 0
    /// Monotonic uptime when `Dengage.showInAppInLine` last ran; used to suppress premature "hidden" while the SDK loads.
    private var lastInlineSdkInvokeUptime: TimeInterval?
    private let inlineShowGraceSec: TimeInterval = 0.55

    public override init(frame: CGRect) {
        super.init(frame: frame)
        commonInit()
    }

    public required init?(coder aDecoder: NSCoder) {
        super.init(coder: aDecoder)
        commonInit()
    }

    private func commonInit() {
        inAppInlineElementView.navigationDelegate = self
        addSubview(inAppInlineElementView)
        NSLayoutConstraint.activate([
            inAppInlineElementView.leadingAnchor.constraint(equalTo: self.leadingAnchor),
            inAppInlineElementView.trailingAnchor.constraint(equalTo: self.trailingAnchor),
            inAppInlineElementView.topAnchor.constraint(equalTo: self.topAnchor)
        ])
        heightConstraint = inAppInlineElementView.heightAnchor.constraint(equalToConstant: 1)
        heightConstraint.isActive = true
    }

    deinit {
        pollTimer?.invalidate()
    }

    public override func layoutSubviews() {
        super.layoutSubviews()
        guard !didSetup else { return }
        didSetup = true
        callInAppInline()
        startVisibilityPollingIfNeeded()
    }

    private func syncInAppInlineIfReady() {
        guard didSetup else { return }
        callInAppInline()
    }

    /// Match Flutter iOS: rely on `isHidden` / alpha (SDK sets `isHidden` + frame when hiding).
    /// Avoid using `bounds` here — Auto Layout can briefly give 0×0 while the view is still meant to show.
    private func inlineAppearsHiddenForReporting() -> Bool {
        let wv = inAppInlineElementView
        if wv.isHidden { return true }
        if wv.alpha < 0.02 { return true }
        return false
    }

    /// Aligns with Android: avoid reporting hidden to JS during the grace window after show when `hideIfNotFound` is on.
    private func hiddenForJsEvent(debouncedHidden: Bool, now: TimeInterval) -> Bool {
        if !debouncedHidden { return false }
        if !hideIfNotFound { return true }
        guard let t = lastInlineSdkInvokeUptime else { return debouncedHidden }
        return (now - t) >= inlineShowGraceSec
    }

    private func emitVisibilityIfNeeded(_ debouncedHidden: Bool) {
        if lastReportedHidden == nil || lastReportedHidden != debouncedHidden {
            lastReportedHidden = debouncedHidden
            let payload: [String: Any] = ["isHidden": debouncedHidden]
            // Must be synchronous: async delivery can reorder (e.g. `true` then a stale `false`) so JS stays on "visible slot".
            onVisibilityChanged?(payload)
        }
    }

    /// SDK sometimes applies `isHidden` after the current run loop; re-sync a couple of times.
    private func schedulePostSdkVisibilityResync() {
        for delay in [0.08, 0.22] as [TimeInterval] {
            DispatchQueue.main.asyncAfter(deadline: .now() + delay) { [weak self] in
                guard let self else { return }
                let now = ProcessInfo.processInfo.systemUptime
                let rawHidden = self.inlineAppearsHiddenForReporting()
                let forEmit = self.hiddenForJsEvent(debouncedHidden: rawHidden, now: now)
                if self.lastReportedHidden != forEmit {
                    self.lastReportedHidden = nil
                    self.emitVisibilityIfNeeded(forEmit)
                }
            }
        }
    }

    private func callInAppInline() {
        if let old = viewWithTag(Self.viewTag), old !== inAppInlineElementView {
            old.removeFromSuperview()
        }

        inAppInlineElementView.tag = Self.viewTag

        let pid = propertyId?.trimmingCharacters(in: .whitespacesAndNewlines) ?? ""
        let screen = screenName?.trimmingCharacters(in: .whitespacesAndNewlines) ?? ""

        // Avoid calling Dengage with empty ids — that path never applies `hideIfNotFound`, so the WKWebView stays blank but "visible".
        guard !pid.isEmpty, !screen.isEmpty else {
            lastReportedHidden = nil
            hiddenSinceUptime = nil
            lastInlineSdkInvokeUptime = nil
            if hideIfNotFound {
                inAppInlineElementView.isHidden = true
                heightConstraint.constant = 0
                invalidateIntrinsicContentSize()
                emitVisibilityIfNeeded(true)
            }
            return
        }

        inAppInlineElementView.isHidden = false
        inAppInlineElementView.alpha = 1

        lastInlineSdkInvokeUptime = ProcessInfo.processInfo.systemUptime
        Dengage.showInAppInLine(
            propertyID: pid,
            inAppInlineElement: self.inAppInlineElementView,
            screenName: screen,
            customParams: self.customParams,
            hideIfNotFound: hideIfNotFound
        )

        // `showInAppInLine` uses optional chaining — if Dengage never started, nothing runs and the WKWebView stays visible.
        if Dengage.dengage == nil && hideIfNotFound {
            inAppInlineElementView.isHidden = true
            heightConstraint.constant = 0
            invalidateIntrinsicContentSize()
        }

        lastReportedHidden = nil
        hiddenSinceUptime = nil

        schedulePostSdkVisibilityResync()
    }

    private func startVisibilityPollingIfNeeded() {
        pollTimer?.invalidate()
        let timer = Timer(timeInterval: 0.05, repeats: true) { [weak self] _ in
            guard let self else { return }

            let rawHidden = self.inlineAppearsHiddenForReporting()
            let now = ProcessInfo.processInfo.systemUptime
            let debouncedHidden: Bool
            if rawHidden {
                if self.hiddenSinceUptime == nil { self.hiddenSinceUptime = now }
                debouncedHidden = (now - (self.hiddenSinceUptime ?? now)) >= self.hiddenDebounceSec
            } else {
                self.hiddenSinceUptime = nil
                debouncedHidden = false
            }

            let forEmit = self.hiddenForJsEvent(debouncedHidden: debouncedHidden, now: now)

            if forEmit {
                self.heightConstraint.constant = 0
                self.invalidateIntrinsicContentSize()
            }

            self.emitVisibilityIfNeeded(forEmit)
        }
        pollTimer = timer
        RunLoop.main.add(timer, forMode: .common)
    }

    public override var intrinsicContentSize: CGSize {
        CGSize(
            width: UIView.noIntrinsicMetric,
            height: heightConstraint.constant
        )
    }
}

extension RCTInAppInlineView: WKNavigationDelegate {
    public func webView(_ webView: WKWebView, didFinish navigation: WKNavigation!) {
        webView.evaluateJavaScript("document.body.scrollHeight") { [weak self] result, _ in
            guard let self = self, let h = result as? CGFloat else { return }
            if self.inlineAppearsHiddenForReporting() { return }
            self.heightConstraint.constant = h
            self.setNeedsLayout()
            self.invalidateIntrinsicContentSize()
        }
    }
}
