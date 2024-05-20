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
      "_p_l_NO5ikc0BTAhM9_s_l_N_p_l_Yuoww3Qy_p_l_Eh_s_l_hjBKP4axDG823EokwOHnQ6oNHTAubaZY7Bp1Pd_s_l_uCtuhzno_p_l_MuCxMHI9Hn3jANu9l2QzI3ISlSgnmqZtv1p0hDI8Sd5OaGoB1Dp3sHJu2tQQzHLREp2kdBCRA_e_q__e_q_",
      "YOURE_HUAWEI_KEY_HERE",
      getApplicationContext()
    );
  }
}
