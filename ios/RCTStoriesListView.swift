import UIKit
import React
import Dengage


@objc public class RCTStoriesListView: RCTView {
  
  static let viewTag = 1453
  
  static let urlKey = "url"
  
  @objc var storyPropertyId: String?
  @objc var screenName: String?
    
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
    let storyView = UIView()
    if self.viewWithTag(Self.viewTag) != nil {
      self.viewWithTag(Self.viewTag)?.removeFromSuperview()
    }
    
    
    Dengage.showAppStory(storyPropertyID: storyPropertyId, screenName: screenName) { storiesListView in
      
      if let storiesListView = storiesListView {
        storiesListView.translatesAutoresizingMaskIntoConstraints = false
        storyView.addSubview(storiesListView as UIView)
      }
      
    }
    storyView.tag = Self.viewTag
    self.addSubview(storyView)
    storyView.frame = self.frame
  }
  
}

