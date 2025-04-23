
#import <React/RCTViewManager.h>

@interface RCT_EXTERN_MODULE(InAppInlineViewManager, RCTViewManager)
RCT_EXPORT_VIEW_PROPERTY(propertyId, NSString)
RCT_EXPORT_VIEW_PROPERTY(screenName, NSString)
RCT_EXPORT_VIEW_PROPERTY(customParams, NSDictionary)

@end
