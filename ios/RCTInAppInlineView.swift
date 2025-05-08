import UIKit
import WebKit
import React
import Dengage

@objc public class RCTInAppInlineView: RCTView {
  
  private var didSetup = false
  static let viewTag = 1789

  @objc var propertyId: String?
  @objc var screenName: String?
  @objc var customParams: [String: String]?

  private let inAppInlineElementView: InAppInlineElementView = {
    let cfg = WKWebViewConfiguration()
    let wv = InAppInlineElementView(frame: .zero, configuration: cfg)
    wv.translatesAutoresizingMaskIntoConstraints = false
    return wv
  }()

  private var heightConstraint: NSLayoutConstraint!

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

  public override func layoutSubviews() {
    super.layoutSubviews()
    guard !didSetup else { return }
    didSetup = true
    callInAppInline()
  }

  private func callInAppInline() {
    if let old = viewWithTag(Self.viewTag) {
      old.removeFromSuperview()
    }

    inAppInlineElementView.tag = Self.viewTag
    Dengage.showInAppInLine(
      propertyID: self.propertyId,
      inAppInlineElement: self.inAppInlineElementView,
      screenName: self.screenName,
      customParams: self.customParams,
      hideIfNotFound: true
    )
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
      //self.layoutIfNeeded()
      self.invalidateIntrinsicContentSize()
    }
  }
}
