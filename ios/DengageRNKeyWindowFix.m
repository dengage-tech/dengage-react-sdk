#import <UIKit/UIKit.h>
#import <React/RCTUtils.h>
#import <objc/runtime.h>

// Dengage iOS SDK resolves the presenter via UIApplication.keyWindow, which is often nil
// in UIScene-based React Native apps. Route keyWindow to RCTKeyWindow() so story taps
// can present StoryDisplayViewController.
static IMP DengageRNOriginalKeyWindowIMP = NULL;

static UIWindow *DengageRNKeyWindow(id self, SEL _cmd)
{
  UIWindow *rnWindow = RCTKeyWindow();
  if (rnWindow != nil) {
    return rnWindow;
  }
  if (DengageRNOriginalKeyWindowIMP != NULL) {
    return ((UIWindow *(*)(id, SEL))DengageRNOriginalKeyWindowIMP)(self, _cmd);
  }
  return nil;
}

__attribute__((constructor)) static void DengageRNInstallKeyWindowFix(void)
{
  Method keyWindowMethod = class_getInstanceMethod(UIApplication.class, @selector(keyWindow));
  if (keyWindowMethod == NULL) {
    return;
  }
  DengageRNOriginalKeyWindowIMP = method_getImplementation(keyWindowMethod);
  method_setImplementation(keyWindowMethod, (IMP)DengageRNKeyWindow);
}
