package com.obriylabs.currencyandroid.data.model

import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem

data class ExchangersItem(val lat: Double, val lng: Double, val workingTime: String) : ClusterItem {

    private val mPosition: LatLng = LatLng(lat, lng)

    override fun getPosition(): LatLng {
        return mPosition
    }

    override fun getTitle(): String? {
        return null
    }

    override fun getSnippet(): String? {
        return null
    }

}