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
      "hQvYUaYWvQq5CO4n5T_s_l_JjrPc1YJQdQ32k1X_p_l_nQfsNTp9klnfZuDyklQlJU1eB3WZqEP_s_l_Az2LtM5OZYGBc3Hlj1_p_l__s_l_9o_s_l_kDyNLQERuwTsDm14PZ_s_l_xnvObV_s_l_lFY_p_l_hHbw5RfbSizp5pdPXYdqeykE41F5g_e_q__e_q_",
      "YOURE_HUAWEI_KEY_HERE",
      true,
      getApplicationContext()
    );
  }
}
