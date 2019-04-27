package com.obriylabs.currencyandroid.presentation.viewmodel

import android.arch.lifecycle.MutableLiveData
import com.obriylabs.currencyandroid.data.model.InputData
import com.obriylabs.currencyandroid.data.model.SourceDb
import com.obriylabs.currencyandroid.domain.entity.ExchangersEntity
import com.obriylabs.currencyandroid.domain.interactor.GetDataExchangers
import com.obriylabs.currencyandroid.domain.interactor.GetExchangers
import com.obriylabs.currencyandroid.domain.interactor.GetInputData
import com.obriylabs.currencyandroid.domain.interactor.UseCase
import javax.inject.Inject

class StartViewModel
@Inject constructor(private val getDataExchangers: GetDataExchangers,
                    private val getInputData: GetInputData,
                    private val getExchangers: GetExchangers) : BaseViewModel() {

    var responseBody: MutableLiveData<InputData> = MutableLiveData()
    var listOfExchangers: MutableLiveData<List<ExchangersEntity.Result>> = MutableLiveData()

    fun loadDataExchangers() = getDataExchangers(UseCase.None()) { it.result(::handleFailure, ::handleDate) }

    private fun handleDate(data: SourceDb) {
        getInputData(GetInputData.Params(data)) { it.result(::handleFailure, ::handleResponse) }
    }

    private fun handleResponse(inputData: InputData) {
        responseBody.value = inputData
    }

    fun loadListExchangers() = responseBody.value?.let { getExchangers(GetExchangers.Params(it)) { result -> result.result(::handleFailure, ::handleResult) } }

    private fun handleResult(exchangers: ExchangersEntity) {
        listOfExchangers.value = exchangers.results
    }
}
