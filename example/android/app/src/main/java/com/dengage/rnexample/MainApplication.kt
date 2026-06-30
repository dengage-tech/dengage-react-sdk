package com.dengage.rnexample

import android.app.Application
import com.facebook.react.PackageList
import com.facebook.react.ReactApplication
import com.facebook.react.ReactHost
import com.facebook.react.ReactNativeHost
import com.facebook.react.ReactPackage
import com.facebook.react.defaults.DefaultNewArchitectureEntryPoint.load
import com.facebook.react.defaults.DefaultReactHost.getDefaultReactHost
import com.facebook.react.defaults.DefaultReactNativeHost
import com.facebook.react.soloader.OpenSourceMergedSoMapping
import com.facebook.soloader.SoLoader
import com.dengagetech.reactnativedengage.DengageRNCoordinator
import com.dengage.hms.DengageHmsManager


class MainApplication : Application(), ReactApplication {

    override val reactNativeHost: ReactNativeHost =
        object : DefaultReactNativeHost(this) {
            override fun getPackages(): List<ReactPackage> =
                PackageList(this).packages.apply {
                    // Packages that cannot be autolinked yet can be added manually here, for example:
                    // add(MyReactNativePackage())
                }

            override fun getJSMainModuleName(): String = "index"

            override fun getUseDeveloperSupport(): Boolean = BuildConfig.DEBUG

            override val isNewArchEnabled: Boolean = BuildConfig.IS_NEW_ARCHITECTURE_ENABLED
            override val isHermesEnabled: Boolean = BuildConfig.IS_HERMES_ENABLED
        }

    override val reactHost: ReactHost
        get() = getDefaultReactHost(applicationContext, reactNativeHost)


    override fun onCreate() {
        super.onCreate()
        SoLoader.init(this, OpenSourceMergedSoMapping)
        if (BuildConfig.IS_NEW_ARCHITECTURE_ENABLED) {
            // If you opted-in for the New Architecture, we load the native entry point for this app.
            load()
        }

        DengageRNCoordinator.sharedInstance.configureReactBridge(
            isNewArchitectureEnabled = BuildConfig.IS_NEW_ARCHITECTURE_ENABLED,
        ) {
            reactNativeHost.reactInstanceManager
        }

        val dengageHmsManager = DengageHmsManager()

        DengageRNCoordinator.sharedInstance.setupDengage(
            firebaseIntegrationKey = Constants.FIREBASE_APP_INTEGRATION_KEY,
            huaweiIntegrationKey = Constants.HUAWEI_APP_INTEGRATION_KEY,
            dengageHmsManager = dengageHmsManager,
            context = this,
            deviceConfigurationPreference = com.dengage.sdk.data.remote.api.DeviceConfigurationPreference.Google,
            disableOpenWebUrl = false,
            logEnabled = true,
            enableGeoFence = true,
            developmentStatus = true
        )
    }
}
