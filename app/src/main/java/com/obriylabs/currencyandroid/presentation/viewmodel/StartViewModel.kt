package com.obriylabs.currencyandroid.presentation.viewmodel

import android.arch.lifecycle.MutableLiveData
import com.obriylabs.currencyandroid.data.model.InputData
import com.obriylabs.currencyandroid.data.model.SourceDb
import com.obriylabs.currencyandroid.domain.entity.ExchangersEntity
import com.obriylabs.currencyandroid.domain.interactor.*
import javax.inject.Inject

class StartViewModel
@Inject constructor(private val getDataExchangers: GetDataExchangers,
                    private val getInputData: GetInputData,
                    private val getExchangersEntity: GetExchangersEntity,
                    private val setExchangers: SetExchangers) : BaseViewModel() {

    var responseBody: MutableLiveData<InputData> = MutableLiveData()

    //var listOfExchangers: MutableLiveData<List<ExchangersEntity.Result>> = MutableLiveData()

    fun loadDataExchangers() = getDataExchangers(UseCase.None()) { it.result(::handleFailure, ::handleDate) }

    fun loadListExchangers() = responseBody.value?.let { getExchangersEntity(GetExchangersEntity.Params(it)) { result -> result.result(::handleFailure, ::handleResult) } }

    private fun handleDate(data: SourceDb) {
        getInputData(GetInputData.Params(data)) { it.result(::handleFailure, ::handleResponse) }
    }

    private fun handleResponse(inputData: InputData) {
        responseBody.value = inputData
    }

    private fun handleResult(exchangers: ExchangersEntity) {
        setExchangers(SetExchangers.Params(exchangers.results))
        //listOfExchangers.value = exchangers.results
    }

}
