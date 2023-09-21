package com.ixidev.mobile.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.lifecycle.lifecycleScope
import com.ixidev.data.logger.EVENT_EXTRACTS_DEVICE
import com.ixidev.data.logger.EVENT_OPEN_APP
import com.ixidev.data.logger.IAppLogger
import com.ixidev.mobile.R
import com.ixidev.mobile.ui.common.SaveSharedPreference
import com.ixidev.mobile.ui.main.MobileMainActivity
import com.yodo1.mas.Yodo1Mas
import com.yodo1.mas.appopenad.Yodo1MasAppOpenAd
import com.yodo1.mas.appopenad.Yodo1MasAppOpenAdListener
import com.yodo1.mas.error.Yodo1MasError
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {

    @Inject
    lateinit var logger: IAppLogger
    private var sdkInitCalled = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        Log.d("tttttt", "Yodo1 MAS : Init SDKfffffff")

        initMAS()
    }

    private fun initMAS() {
        Log.d("tttttt", "Yodo1 MAS : Init SDK")

        lifecycleScope.launch {
            delay(3000)
            if(!sdkInitCalled) {
                if (SaveSharedPreference.showAds(this@SplashActivity)) {
                    Yodo1MasAppOpenAd.getInstance().autoDelayIfLoadFail = true
                    showOpenAppAd()
                } else {
                    gotoMobileMainActivity()
                }
            }
        }

        Yodo1Mas.getInstance().setCOPPA(false)
        Yodo1Mas.getInstance().setGDPR(true)
        Yodo1Mas.getInstance().setCCPA(false)
//iRPCQKh8fo
        Yodo1Mas.getInstance().init(this, "iRPCQKh8fo", object : Yodo1Mas.InitListener {
            override fun onMasInitSuccessful() {
                Log.d("tttttt", "Yodo1 MAS : SDK initialized")
                sdkInitCalled = true
                if (SaveSharedPreference.showAds(this@SplashActivity)) {
                    Yodo1MasAppOpenAd.getInstance().autoDelayIfLoadFail = true
                    showOpenAppAd()
                } else {
                    gotoMobileMainActivity()
                }
            }

            override fun onMasInitFailed(error: Yodo1MasError) {
                Log.d("tttttt", "Yodo1 MAS : SDK failed")
                gotoMobileMainActivity()
            }
        })
    }

    private fun showOpenAppAd() {
        val appOpenAd = Yodo1MasAppOpenAd.getInstance()
        appOpenAd.loadAd(this)
        appOpenAd.setAdListener(object : Yodo1MasAppOpenAdListener {
            override fun onAppOpenAdLoaded(ad: Yodo1MasAppOpenAd) {
                // Code to be executed when an ad finishes loading.
                ad.showAd(this@SplashActivity)
            }

            override fun onAppOpenAdFailedToLoad(ad: Yodo1MasAppOpenAd, error: Yodo1MasError) {
                // Code to be executed when an ad request fails.
                print("Yodo1 MAS : onAppOpenAdFailedToLoad")
                gotoMobileMainActivity()
            }

            override fun onAppOpenAdOpened(ad: Yodo1MasAppOpenAd) {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
            }

            override fun onAppOpenAdFailedToOpen(ad: Yodo1MasAppOpenAd, error: Yodo1MasError) {
                // Code to be executed when an ad open fails.
                print("Yodo1 MAS : onAppOpenAdFailedToOpen")
                gotoMobileMainActivity()
            }

            override fun onAppOpenAdClosed(ad: Yodo1MasAppOpenAd) {
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
                print("Yodo1 MAS : onAppOpenAdClosed")
                gotoMobileMainActivity()
            }
        })
    }

    private fun gotoMobileMainActivity() {
        logger.logEvent(
            EVENT_OPEN_APP,
            bundleOf(
                EVENT_EXTRACTS_DEVICE to "mobile"
            )
        )
        startActivity(
            Intent(
                this,
                MobileMainActivity::class.java
            )
        )
        finish()
    }
}