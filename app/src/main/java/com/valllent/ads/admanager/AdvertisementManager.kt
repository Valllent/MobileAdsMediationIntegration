package com.valllent.ads.admanager

import android.content.Context
import android.util.Log
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.admanager.AdManagerAdRequest
import com.google.android.gms.ads.admanager.AdManagerAdView
import kotlinx.coroutines.withTimeoutOrNull

object AdvertisementManager {

    private const val TAG = "AdvertisementManager"
    private const val WIDTH = 320
    private const val HEIGHT = 50
    private const val GAM_AD_UNIT_ID = "/5300653/app_qa_banner"

    /**
     * Maximum allowed time for waiting mediation response.
     */
    private const val WAITING_TIME = 3_000L

    private var plugins = listOf<MediationPlugin>()

    fun setPlugins(plugins: List<MediationPlugin>) {
        this.plugins = plugins
    }

    fun init(context: Context) {
        MobileAds.initialize(context)
        for (plugin in plugins) {
            plugin.initSdk(context)
        }
    }

    suspend fun loadAd(adView: AdManagerAdView) {
        adView.adUnitId = GAM_AD_UNIT_ID
        adView.setAdSizes(AdSize(WIDTH, HEIGHT))
        adView.adListener = createAdListener(adView)

        var adRequest = AdManagerAdRequest.Builder().build()
        for (plugin in plugins) {
            val pluginName = plugin::class.java.simpleName
            val startTime = System.currentTimeMillis()

            Log.d(TAG, "Requesting ad for plugin: $pluginName")
            val modifiedRequest = withTimeoutOrNull(WAITING_TIME) {
                plugin.makeBannerRequest(adRequest)
            }
            Log.d(TAG, "Requesting $pluginName took ${System.currentTimeMillis() - startTime}ms.")

            adRequest = modifiedRequest ?: continue
        }
        adView.loadAd(adRequest)
    }

    private fun createAdListener(adView: AdManagerAdView): AdListener {
        return object : AdListener() {
            override fun onAdLoaded() {
                for (plugin in plugins) {
                    plugin.onAdLoaded(adView)
                }
            }
        }
    }

}