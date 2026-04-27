//
//  InAppInlineViewManager.swift
//  Pods
//
//  Created by Egemen Gülkılık on 19.04.2025.
//

#import "RCTInAppInlineViewManager.h"

#import <React/RCTBridge.h>
#import <React/RCTComponent.h>
#import <React/RCTUIManager.h>

#if __has_include("react_native_dengage/react_native_dengage-Swift.h")
#import "react_native_dengage/react_native_dengage-Swift.h"
#else
#import "react_native_dengage-Swift.h"
#endif

@implementation RCTInAppInlineViewManager

RCT_EXPORT_VIEW_PROPERTY(propertyId, NSString)
RCT_EXPORT_VIEW_PROPERTY(screenName, NSString)
RCT_EXPORT_VIEW_PROPERTY(customParams, NSDictionary)
RCT_EXPORT_VIEW_PROPERTY(hideIfNotFound, BOOL)
RCT_EXPORT_VIEW_PROPERTY(onInlineVisibilityChanged, RCTDirectEventBlock)
RCT_EXPORT_MODULE(RCTInAppInlineView)

- (UIView *)view {
    return [[RCTInAppInlineView alloc] init];
}

+ (BOOL)requiresMainQueueSetup
{
    return YES;
}

@end




