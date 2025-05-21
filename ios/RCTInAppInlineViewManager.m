//
//  InAppInlineViewManager.swift
//  Pods
//
//  Created by Egemen Gülkılık on 19.04.2025.
//

#import "RCTInAppInlineViewManager.h"

#import <React/RCTBridge.h>
#import <React/RCTUIManager.h>

#import "react_native_dengage-Swift.h"

@implementation RCTInAppInlineViewManager

RCT_EXPORT_VIEW_PROPERTY(propertyId, NSString)
RCT_EXPORT_VIEW_PROPERTY(screenName, NSString)
RCT_EXPORT_VIEW_PROPERTY(customParams, NSDictionary)
RCT_EXPORT_MODULE(RCTInAppInlineView)

- (UIView *)view {
    return [[RCTInAppInlineView alloc] init];
}

+ (BOOL)requiresMainQueueSetup
{
    return YES;
}

@end




