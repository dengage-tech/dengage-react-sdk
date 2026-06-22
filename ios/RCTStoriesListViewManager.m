//
//  RCTStoriesListViewManager.m
//  react-native-related-digital
//
//  Created by Egemen Gülkılık on 22.03.2025.
//

#import "RCTStoriesListViewManager.h"

#import <React/RCTBridge.h>
#import <React/RCTComponent.h>
#import <React/RCTUIManager.h>

#if __has_include("react_native_dengage/react_native_dengage-Swift.h")
#import "react_native_dengage/react_native_dengage-Swift.h"
#else
#import "react_native_dengage-Swift.h"
#endif

@implementation RCTStoriesListViewManager

RCT_EXPORT_VIEW_PROPERTY(storyPropertyId, NSString)
RCT_EXPORT_VIEW_PROPERTY(screenName, NSString)
RCT_EXPORT_VIEW_PROPERTY(customParams, NSDictionary)
RCT_EXPORT_VIEW_PROPERTY(hideIfNotFound, BOOL)
RCT_EXPORT_VIEW_PROPERTY(onStoryVisibilityChanged, RCTDirectEventBlock)
RCT_EXPORT_MODULE(RCTStoriesListView)

- (UIView *)view {
    RCTStoriesListView *storiesView = [[RCTStoriesListView alloc] init];
    storiesView.bridge = self.bridge;
    return storiesView;
}

+ (BOOL)requiresMainQueueSetup
{
    return YES;
}

@end
