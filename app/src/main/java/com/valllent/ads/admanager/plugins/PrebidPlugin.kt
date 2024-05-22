package com.valllent.ads.admanager.plugins

import android.content.Context
import android.util.Log
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.admanager.AdManagerAdRequest
import com.google.android.gms.ads.admanager.AdManagerAdView
import com.valllent.ads.admanager.MediationPlugin
import kotlinx.coroutines.suspendCancellableCoroutine
import org.prebid.mobile.BannerAdUnit
import org.prebid.mobile.Host
import org.prebid.mobile.PrebidMobile
import org.prebid.mobile.ResultCode
import org.prebid.mobile.addendum.AdViewUtils
import org.prebid.mobile.addendum.PbFindSizeError
import org.prebid.mobile.api.data.InitializationStatus
import kotlin.coroutines.resume


class PrebidPlugin : MediationPlugin {

    companion object {
        private const val TAG = "Prebid Ads"
        private const val WIDTH = 320
        private const val HEIGHT = 50
        private const val PREBID_CONFIG_ID = "1001-sreq-test-320x50-imp-1"
    }

    private var adUnit: BannerAdUnit? = null

    override fun initSdk(context: Context) {
        PrebidMobile.setPrebidServerAccountId("1001")
        PrebidMobile.setPrebidServerHost(Host.RUBICON)
        PrebidMobile.initializeSdk(context) { status ->
            if (status == InitializationStatus.SUCCEEDED) {
                Log.d(TAG, "SDK initialized successfully!")
            } else {
                Log.e(TAG, "SDK initialization error: " + status.description)
            }
        }
        PrebidMobile.setShareGeoLocation(true)
    }

    override suspend fun makeBannerRequest(adRequest: AdManagerAdRequest): AdManagerAdRequest? {
        return suspendCancellableCoroutine { continuation ->
            adUnit = BannerAdUnit(PREBID_CONFIG_ID, WIDTH, HEIGHT)
            adUnit?.fetchDemand(adRequest) {
                if (it != ResultCode.SUCCESS) {
                    Log.e(TAG, "Bad result code: ${it.name}")
                }
                continuation.resume(adRequest)
            }
        }
    }

    override fun onAdLoaded(adView: AdManagerAdView) {
        AdViewUtils.findPrebidCreativeSize(
            adView,
            object : AdViewUtils.PbFindSizeListener {
                override fun success(width: Int, height: Int) {
                    adView.setAdSizes(AdSize(width, height))
                }

                override fun failure(error: PbFindSizeError) {}
            },
        )
    }

}
