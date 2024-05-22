package com.valllent.ads

import android.os.Bundle
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.ads.admanager.AdManagerAdView
import com.valllent.ads.admanager.AdvertisementManager
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val adView = createAdView()
        lifecycleScope.launch {
            AdvertisementManager.loadAd(adView)
        }
    }

    private fun createAdView(): AdManagerAdView {
        val adView = AdManagerAdView(this)
        findViewById<FrameLayout>(R.id.adContainer).addView(adView)
        return adView
    }

}