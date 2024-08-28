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
      "_s_l_WprIlK2ckReLoPwyt_p_l_ntJ_s_l_jZvzATaqdK2V4nb3IFP_p_l_cGhQllF6fF_s_l_4vbq22VEeggMneXR_p_l_qqS48Ew7KXtakSQJHAXvacgRiZN4Uydz0qbCm0r8mx3iw8x_s_l_rUeRcOa1ITjijMVIZoWPcOpcpC0jxyA_e_q__e_q_",
      "YOURE_HUAWEI_KEY_HERE",
      getApplicationContext(),
      false
    );
  }
}
