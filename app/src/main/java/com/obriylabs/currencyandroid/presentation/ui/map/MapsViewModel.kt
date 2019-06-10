package com.obriylabs.currencyandroid.presentation.ui.map

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.google.android.gms.maps.model.LatLngBounds
import com.google.maps.android.clustering.Cluster
import com.google.maps.android.clustering.ClusterManager
import com.obriylabs.currencyandroid.data.model.Exchangers
import com.obriylabs.currencyandroid.data.model.ExchangersItem
import com.obriylabs.currencyandroid.domain.interactor.GetSavedExchangers
import com.obriylabs.currencyandroid.domain.interactor.UseCase
import com.obriylabs.currencyandroid.presentation.viewmodel.BaseViewModel
import javax.inject.Inject

class MapsViewModel @Inject constructor(getExchangers: GetSavedExchangers) : BaseViewModel() {

    private var listExchangers: MutableLiveData<List<Exchangers>> = MutableLiveData()

    init {
        if (listExchangers.value == null) {
            getExchangers(UseCase.None()) { it.result(::handleFailure, ::handleResult) }
        }
    }

    private fun handleResult(exchangers: List<Exchangers>) {
        listExchangers.value = exchangers
    }

    fun readItems(listExchangers: List<Exchangers>?, clusterManager: ClusterManager<ExchangersItem>?) {
        listExchangers?.let {
            for (item in it) {
                val position = item.currencyLocation
                val lat = position.latitude
                val lng = position.longitude
                val workingTime = item.currencyWorkingTime
                val offsetItem = ExchangersItem(lat, lng, workingTime)
                clusterManager?.addItem(offsetItem)
            }
        }
    }

    fun getPositionsOfMarkersFromCluster(cluster: Cluster<ExchangersItem>?): LatLngBounds? {
        val builder = LatLngBounds.builder()
        val exchangerMarkers = cluster?.items
        // Providing the positions of all the markers from the cluster
        exchangerMarkers?.let {
            for (item in it) {
                val exchangerPosition = item.position
                builder.include(exchangerPosition)
            }
        }
        // Building bounds of the area where we should point the camera.
        return builder.build()
    }

    fun observableListExchangers(): LiveData<List<Exchangers>> = listExchangers

}
