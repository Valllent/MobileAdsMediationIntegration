package com.valllent.ads

import android.app.Application
import com.valllent.ads.admanager.AdvertisementManager
import com.valllent.ads.admanager.plugins.AmazonPlugin
import com.valllent.ads.admanager.plugins.PrebidPlugin

class TheApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        AdvertisementManager.setPlugins(listOf(AmazonPlugin(), PrebidPlugin()))
        AdvertisementManager.init(this)
    }

}