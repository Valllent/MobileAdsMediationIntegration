# Mobile Ads Mediation Integration

The application implements a good architecture using mediation ad network plugins. 
It uses Google Ad Manager as the primary ad server, with Prebid and Amazon used as mediation ad networks.

This app provides an easy-to-use and extendable architecture for any number of mediation ad networks. 
It automatically cancels requests that take too long and networks that do not call back.

You can simply add any new mediation ad network by implementing MediationPlugin interface 
and passing it to `setPlugins()` function.

### Usage

Initialization

```
    AdvertisementManager.setPlugins(listOf(AmazonPlugin(), PrebidPlugin()))
    AdvertisementManager.init(this)
```

Requesting ad

```
    AdvertisementManager.loadAd(adView)
```


