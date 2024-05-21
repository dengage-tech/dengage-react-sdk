
import Dengage
@objc (InAppInlineViewManager)

class InAppInlineViewManager: RCTViewManager {

  override static func requiresMainQueueSetup() -> Bool {
    return true
  }

  override func view() -> UIView! {
      return InAppInlineView()
  }

}
