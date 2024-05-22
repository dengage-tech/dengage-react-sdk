import UIKit
import Dengage
import WebKit
class InAppInlineView: InAppInlineElementView {

 
    
    @objc var propertyId = "" {
      didSet {
        self.callInAppInline()
      }
    }
    
    @objc var screenName = "" {
      didSet {
          self.setupView()
      }
    }
    
    @objc var customParams = [String:Any ](){
      didSet {
          self.setupView()
      }
    }
 
    override init(frame: CGRect, configuration: WKWebViewConfiguration) {
        super.init(frame: frame, configuration: configuration)
      
    }

    required init?(coder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }

  private func setupView() {
      print(propertyId)
      print(screenName)
      print(customParams)
   
      
  }
    private func callInAppInline() {
        print(customParams)
        DispatchQueue.main.asyncAfter(deadline: .now() + 0.2) {
            Dengage.showInAppInLine(propertyID: self.propertyId,inAppInlineElement: self,screenName:self.screenName,customParams:self.customParams as?  Dictionary<String,String>)
        }
        
    }
 

}
