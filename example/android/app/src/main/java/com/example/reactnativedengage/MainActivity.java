package com.example.reactnativedengage;

import android.content.Context;
import android.os.Bundle;

import com.dengage.sdk.DengageManager;
import com.facebook.react.ReactActivity;

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
    Context context = getApplicationContext();
    final DengageManager manager = DengageManager .getInstance(context)
      .setLogStatus(true)
      .setFirebaseIntegrationKey("YOUR_DENGAGE_FIREBASE_APP_INTEGRATION_KEY")
      .setHuaweiIntegrationKey("OUR_DENGAGE_HUAWEI_APP_INTEGRATION_KEY")
      .init();

  }

}
