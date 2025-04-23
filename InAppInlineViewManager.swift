//
//  InAppInlineViewManager.swift
//  Pods
//
//  Created by Egemen Gülkılık on 19.04.2025.
//

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
