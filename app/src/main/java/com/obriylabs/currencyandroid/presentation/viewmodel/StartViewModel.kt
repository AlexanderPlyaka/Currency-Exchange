package com.obriylabs.currencyandroid.presentation.viewmodel

import android.arch.lifecycle.MutableLiveData
import com.obriylabs.currencyandroid.domain.ReceivedData
import com.obriylabs.currencyandroid.domain.entity.DataEntity
import com.obriylabs.currencyandroid.domain.entity.ExchangersEntity
import com.obriylabs.currencyandroid.domain.interactor.GetDataExchangers
import com.obriylabs.currencyandroid.domain.interactor.GetExchangers
import com.obriylabs.currencyandroid.domain.interactor.GetResponseBody
import com.obriylabs.currencyandroid.domain.interactor.UseCase
import javax.inject.Inject

class StartViewModel
@Inject constructor(private val getDataExchangers: GetDataExchangers,
                    private val getResponseBody: GetResponseBody,
                    private val getExchangers: GetExchangers) : BaseViewModel() {

    var responseBody: MutableLiveData<ReceivedData> = MutableLiveData()
    var listOfExchangers: MutableLiveData<List<ExchangersEntity.Result>> = MutableLiveData()

    fun loadDataExchangers() = getDataExchangers(UseCase.None()) { it.result(::handleFailure, ::handleDate) }

    private fun handleDate(data: DataEntity) {
        getResponseBody(GetResponseBody.Params(data)) { it.result(::handleFailure, ::handleResponse) }
    }

    private fun handleResponse(receivedData: ReceivedData) {
        responseBody.value = receivedData
    }

    fun loadListExchangers() = responseBody.value?.let { getExchangers(GetExchangers.Params(it)) { result -> result.result(::handleFailure, ::handleResult) } }

    private fun handleResult(exchangers: ExchangersEntity) {
        listOfExchangers.value = exchangers.results
    }
}
