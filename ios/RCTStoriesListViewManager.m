//
//  RCTStoriesListViewManager.m
//  react-native-related-digital
//
//  Created by Egemen Gülkılık on 22.03.2025.
//

#import "RCTStoriesListViewManager.h"

#import <React/RCTBridge.h>
#import <React/RCTUIManager.h>

#if __has_include("react_native_dengage/react_native_dengage-Swift.h")
#import "react_native_dengage/react_native_dengage-Swift.h"
#else
#import "react_native_dengage-Swift.h"
#endif

@implementation RCTStoriesListViewManager

RCT_EXPORT_VIEW_PROPERTY(storyPropertyId, NSString)
RCT_EXPORT_VIEW_PROPERTY(screenName, NSString)
RCT_EXPORT_MODULE(RCTStoriesListView)

- (UIView *)view {
    return [[RCTStoriesListView alloc] init];
}

@end

