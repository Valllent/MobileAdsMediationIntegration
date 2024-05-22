package com.valllent.ads.admanager.plugins

import android.content.Context
import android.util.Log
import com.amazon.device.ads.*
import com.google.android.gms.ads.admanager.AdManagerAdRequest
import com.valllent.ads.admanager.MediationPlugin
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume


class AmazonPlugin : MediationPlugin {

    companion object {
        private const val TAG = "Amazon Ads"
        private const val WIDTH = 320
        private const val HEIGHT = 50
        private const val AMAZON_SLOT_ID = "f77cd0a2-dbcb-4686-aba4-6780929091b7"
    }

    override fun initSdk(context: Context) {
        AdRegistration.getInstance("e00f1bc2-4560-4238-bb48-6e990d16d376", context)
        AdRegistration.setAdNetworkInfo(DTBAdNetworkInfo(DTBAdNetwork.GOOGLE_AD_MANAGER))
        AdRegistration.setMRAIDSupportedVersions(arrayOf("1.0", "2.0", "3.0"))
        AdRegistration.setMRAIDPolicy(MRAIDPolicy.CUSTOM)
        AdRegistration.useGeoLocation(true)
        AdRegistration.enableLogging(true)

        // Must be removed in release version
        AdRegistration.enableTesting(true)
    }

    override suspend fun makeBannerRequest(adRequest: AdManagerAdRequest): AdManagerAdRequest? {
        return suspendCancellableCoroutine { continuation ->
            val loader = DTBAdRequest()
            loader.setSizes(DTBAdSize(WIDTH, HEIGHT, AMAZON_SLOT_ID))
            loader.loadAd(object : DTBAdCallback {
                override fun onSuccess(amazonResponse: DTBAdResponse) {
                    Log.d(TAG, "Amazon Ads received")
                    val modifiedRequest = DTBAdUtil.INSTANCE.loadDTBParams(adRequest, amazonResponse)
                    continuation.resume(modifiedRequest)
                }

                override fun onFailure(adError: AdError) {
                    Log.e(TAG, "Amazon Ads request failed: " + adError.message)
                    continuation.resume(null)
                }
            })
        }
    }

}