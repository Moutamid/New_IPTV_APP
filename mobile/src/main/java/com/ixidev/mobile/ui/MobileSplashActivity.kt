package com.ixidev.mobile.ui

import android.content.Intent
import android.os.Bundle
import androidx.core.os.bundleOf
import com.ixidev.data.AbstractSplashActivity
import com.ixidev.data.logger.EVENT_EXTRACTS_DEVICE
import com.ixidev.data.logger.EVENT_OPEN_APP
import com.ixidev.data.logger.IAppLogger
import com.ixidev.mobile.ui.common.SaveSharedPreference
import com.ixidev.mobile.ui.main.MobileMainActivity
import com.yodo1.mas.Yodo1Mas
import com.yodo1.mas.appopenad.Yodo1MasAppOpenAd
import com.yodo1.mas.appopenad.Yodo1MasAppOpenAdListener
import com.yodo1.mas.error.Yodo1MasError
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * Created by ABDELMAJID ID ALI on 3/5/21.
 * Email : abdelmajid.idali@gmail.com
 * Github : https://github.com/ixiDev
 */
@AndroidEntryPoint
class MobileSplashActivity : AbstractSplashActivity() {

    @Inject
    lateinit var logger: IAppLogger

    override fun openMain() {
        //gotoMobileMainActivity()
        initMAS() // Yodo1
    }

    private fun initMAS() {
        print("Yodo1 MAS : Init SDK")

        Yodo1Mas.getInstance().setCOPPA(false)
        Yodo1Mas.getInstance().setGDPR(true)
        Yodo1Mas.getInstance().setCCPA(false)
//iRPCQKh8fo
        Yodo1Mas.getInstance().init(this, "iRPCQKh8fo", object : Yodo1Mas.InitListener {
            override fun onMasInitSuccessful() {
                print("Yodo1 MAS : SDK initialized")
                if (SaveSharedPreference.showAds(this@MobileSplashActivity)) {
                    Yodo1MasAppOpenAd.getInstance().autoDelayIfLoadFail = true
                    showOpenAppAd()
                }else{
                    gotoMobileMainActivity()
                }
            }

            override fun onMasInitFailed(error: Yodo1MasError) {
                print("Yodo1 MAS : SDK failed")
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
                ad.showAd(this@MobileSplashActivity)
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

    private fun gotoMobileMainActivity(){
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