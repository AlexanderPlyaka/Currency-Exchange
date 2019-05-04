package com.obriylabs.currencyandroid.presentation.viewmodel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.obriylabs.currencyandroid.data.model.ReceivedData
import com.obriylabs.currencyandroid.data.model.DataOfExchangers
import com.obriylabs.currencyandroid.domain.entity.ExchangersEntity
import com.obriylabs.currencyandroid.domain.interactor.*
import javax.inject.Inject

class StartViewModel
@Inject constructor(private val getDataExchangers: GetDataExchangers,
                    private val getReceivedData: GetReceivedData,
                    private val getExchangersEntity: GetExchangersEntity,
                    private val setExchangers: SetExchangers) : BaseViewModel() {

    private var receivedData: MutableLiveData<ReceivedData> = MutableLiveData()

    fun loadDataOfExchangers() {
        if (receivedData.value == null) {
            getDataExchangers(UseCase.None()) { it.result(::handleFailure, ::handleDate) }
        }
    }

    fun loadListExchangers() = receivedData.value?.run {
        getExchangersEntity(GetExchangersEntity.Params(this)) {
            result -> result.result(::handleFailure, ::handleResult)
        }
    }

    private fun handleDate(data: DataOfExchangers) {
        getReceivedData(GetReceivedData.Params(data)) { it.result(::handleFailure, ::handleResponse) }
    }

    private fun handleResponse(receivedData: ReceivedData) {
        this.receivedData.value = receivedData
    }

    private fun handleResult(exchangers: ExchangersEntity) {
        setExchangers(SetExchangers.Params(exchangers.results))
    }

    fun getReceivedData(): LiveData<ReceivedData> = receivedData

}
