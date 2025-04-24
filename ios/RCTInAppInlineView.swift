import UIKit
import Dengage
import React

@objc public class RCTInAppInlineView: RCTView {
  
  static let viewTag = 1789
  
  @objc var propertyId: String?
  @objc var screenName: String?
  @objc var customParams: [String: String]? = nil
  
  var inAppInlineElementView: InAppInlineElementView = {
    let wv = InAppInlineElementView.init(frame: CGRect.init(x: 20, y: 380, width: UIScreen.main.bounds.width - 40, height: UIScreen.main.bounds.height - 420))
    wv.translatesAutoresizingMaskIntoConstraints = false
    wv.contentMode = .scaleAspectFit
    wv.sizeToFit()
    wv.autoresizesSubviews = true
    wv.backgroundColor = .green
    return wv
  }()
  
  override init(frame: CGRect) {
    super.init(frame: frame)
  }
  
  required init?(coder aDecoder: NSCoder) {
    super.init(coder: aDecoder)
  }
  
  public override func layoutSubviews() {
    setupView()
  }
  
  private func setupView() {
    print(propertyId ?? "")
    print(screenName ?? "")
    callInAppInline()
    
    
  }
  private func callInAppInline() {
    
    if self.viewWithTag(Self.viewTag) != nil {
      self.viewWithTag(Self.viewTag)?.removeFromSuperview()
    }
    
    
    DispatchQueue.main.asyncAfter(deadline: .now() + 0.2) {
      Dengage.showInAppInLine(propertyID: self.propertyId,
                              inAppInlineElement: self.inAppInlineElementView,
                              screenName: self.screenName,
                              customParams:self.customParams)
    }
    
    inAppInlineElementView.tag = Self.viewTag
    self.addSubview(inAppInlineElementView)
    inAppInlineElementView.frame = self.frame
    
  }
  
  
}
