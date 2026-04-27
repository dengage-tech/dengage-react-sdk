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

    override init(frame: CGRect) {
        super.init(frame: frame)
        commonInit()
    }

    required init?(coder aDecoder: NSCoder) {
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

    public override func didMoveToWindow() {
        super.didMoveToWindow()
        if window != nil {
            scheduleApplyInline()
        }
    }

    private func configurationSignature() -> String? {
        guard let p = propertyId, let s = screenName else { return nil }
        let params = customParams ?? [:]
        let paramsPart = params.keys.sorted().map { k in "\(k)=\(params[k] ?? "")" }.joined(separator: "\u{1}")
        return "\(p)\u{0}\(s)\u{0}\(paramsPart)"
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
        inAppInlineElementView.stopLoading()
        if let blank = URL(string: "about:blank") {
            inAppInlineElementView.load(URLRequest(url: blank))
        }
        heightConstraint.constant = 1
        invalidateIntrinsicContentSize()
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
                hideIfNotFound: true
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
    }

    public override var intrinsicContentSize: CGSize {
        return CGSize(width: UIView.noIntrinsicMetric,
                      height: heightConstraint.constant)
    }
}

extension RCTInAppInlineView: WKNavigationDelegate {
    public func webView(_ webView: WKWebView, didFinish navigation: WKNavigation!) {
        webView.evaluateJavaScript("document.body.scrollHeight") { [weak self] result, _ in
            guard let self = self, let h = result as? CGFloat else { return }
            self.heightConstraint.constant = h
            self.setNeedsLayout()
            self.invalidateIntrinsicContentSize()
        }
    }
}
