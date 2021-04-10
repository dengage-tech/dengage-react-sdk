package com.example.reactnativedengage;

import android.os.Bundle;

import com.facebook.react.ReactActivity;
import com.reactnativedengage.DengageRNCoordinator;

public class MainActivity extends ReactActivity {

  /**
   * Returns the name of the main component registered from JavaScript. This is used to schedule
   * rendering of the component.
   */
  @Override
  protected String getMainComponentName() {
    return "DengageExample";
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    // These three lines need to be added
    DengageRNCoordinator coordinator = DengageRNCoordinator.Companion.getSharedInstance();
    coordinator.injectReactInstanceManager(getReactInstanceManager());
    coordinator.setupDengage(
      true,
      "m8yFy8Alhxzz0_s_l_wrAxFLJy3uHM7iX_p_l_8wN3qVbZuTcq21qjEGfM2502y_p_l_K5qT7XGgHHCkoMF4N_p_l_UdyKcwS5mIJdKtO0_s_l_cK3C6BHu6k_p_l_DJnETfFL6taM1vTtl2AjXbEol_s_l_",
      "YOURE_HUAWEI_KEY_HERE",
      getApplicationContext()
    );
  }
}
