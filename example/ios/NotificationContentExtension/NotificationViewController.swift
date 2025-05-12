import UIKit
import UserNotifications
import UserNotificationsUI
import Dengage
@objc(NotificationViewController)

class NotificationViewController: UIViewController, UNNotificationContentExtension {
  
  let carouselView = DengageNotificationCarouselView.create()
  
  func didReceive(_ notification: UNNotification) {
    
    //dev-app.dengage.com: ios-rn-sandbox-test
    //let integrationKey = "RtWFz7e1SIvcYm3IeaPg6mWwtSZS_p_l_uZUZjQ5H5PYMphJDQaj_p_l_2x_p_l_sygPdd8k4OnwDfKfxy8e6zfLezdFrlmUAVDz1o2avZsolsEgQq3eDJy_p_l_TLRRjHT2wzGZwPkbmTla5_p_l_uM4299j2Jde4iNO9MLcA_e_q__e_q_"
    
    //dev-app.dengage.com: ios-rn-testflight-test
    let integrationKey = "n_p_l_lO9FMM4fzlX4jbudL355PGRpKsipGVHMgaPbOGAxR_s_l_MJo3J1KWkHYAz4u93U09KjPXZeVqZAZpC_s_l_JjJSlGPBWiHO2pIFsMG71SR5_p_l_YMNTnN1_p_l_Aje3MXqKCRpYjEUBNroiGTGF609W8EhEcRaQAZA_e_q__e_q_"
    
    Dengage.setIntegrationKey(key: integrationKey)
    Dengage.setLog(isVisible: true)
    Dengage.setDevelopmentStatus(isDebug: true)
    carouselView.didReceive(notification)
    
  }
  
  func didReceive(_ response: UNNotificationResponse, completionHandler completion: @escaping (UNNotificationContentExtensionResponseOption) -> Void) {
    carouselView.didReceive(response, completionHandler: completion)
  }
  
  override func loadView() {
    self.view = carouselView
  }
}
