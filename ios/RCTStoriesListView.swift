import UIKit
import React
import Dengage


@objc public class RCTStoriesListView: RCTView {
  
  static let viewTag = 1453
    
  @objc var storyPropertyId: String?
  @objc var screenName: String?
  @objc var customParams: [String: String]? = nil
    
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
    
    
    Dengage.showAppStory(storyPropertyID: storyPropertyId, screenName: screenName, customParams: customParams) { storiesListView in
      
      if let storiesListView = storiesListView {
        storiesListView.translatesAutoresizingMaskIntoConstraints = false
        storyView.addSubview(storiesListView as UIView)
        NSLayoutConstraint.activate([storiesListView.topAnchor.constraint(equalTo: storyView.topAnchor),
                                     storiesListView.bottomAnchor.constraint(equalTo: storyView.bottomAnchor),
                                     storiesListView.leadingAnchor.constraint(equalTo: storyView.leadingAnchor),
                                     storiesListView.trailingAnchor.constraint(equalTo: storyView.trailingAnchor)])
        
      }
      
    }
    storyView.tag = Self.viewTag
    self.addSubview(storyView)
    storyView.frame = self.frame
  }
  
}

