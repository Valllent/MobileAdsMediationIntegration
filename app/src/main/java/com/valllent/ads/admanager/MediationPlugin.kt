package com.valllent.ads.admanager

import android.content.Context
import com.google.android.gms.ads.admanager.AdManagerAdRequest
import com.google.android.gms.ads.admanager.AdManagerAdView


interface MediationPlugin {

    /**
     * SDK initialization
     */
    fun initSdk(context: Context)

    /**
     * SDK specific operation after ad loaded.
     */
    fun onAdLoaded(adView: AdManagerAdView) {}

    /**
     * @param adRequest ad request for downloading ad with GAM
     * @return modified after mediation request or null if request failed
     */
    suspend fun makeBannerRequest(adRequest: AdManagerAdRequest): AdManagerAdRequest?

}