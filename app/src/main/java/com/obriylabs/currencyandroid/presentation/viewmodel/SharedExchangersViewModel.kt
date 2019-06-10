package com.obriylabs.currencyandroid.presentation.viewmodel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.obriylabs.currencyandroid.data.model.DataOfExchangers
import com.obriylabs.currencyandroid.data.model.Exchangers
import com.obriylabs.currencyandroid.data.model.ExchangersResult
import com.obriylabs.currencyandroid.data.model.ReceivedExchangers
import com.obriylabs.currencyandroid.domain.interactor.*
import javax.inject.Inject

class SharedExchangersViewModel
@Inject constructor(private val getDataOfExchangers: GetDataOfExchangers,
                    private val getExchangersDb: GetExchangersDb,
                    private val getExchangersResult: GetExchangersResult,
                    private val getExchangers: GetExchangers): BaseViewModel() {

    private var exchangers: MutableLiveData<ReceivedExchangers> = MutableLiveData()
    private var listExchangers: MutableLiveData<List<Exchangers>> = MutableLiveData()

    fun loadDataOfExchangers() {
        if (exchangers.value == null) {
            getDataOfExchangers(UseCase.None()) { it.result(::handleFailure, ::handleDate) }
        }
    }

    private fun handleDate(data: DataOfExchangers) {
        getExchangersDb(GetExchangersDb.Params(data)) { it.result(::handleFailure, ::handleResult) }
    }

    private fun handleResult(receivedExchangers: ReceivedExchangers) {
        exchangers.value = receivedExchangers
    }

    fun loadListExchangers() {
        if (listExchangers.value == null) {
            exchangers.value?.run {
                getExchangersResult(GetExchangersResult.Params(this)) {
                    it.result(::handleFailure, ::handleResult)
                }
            }
        }
    }

    private fun handleResult(exchangers: ExchangersResult?) {
        if (exchangers != null) {
            getExchangers(GetExchangers.Params(exchangers.results)) {
                it.result(::handleFailure, ::handleResult)
            }
        }
    }

    private fun handleResult(exchangers: List<Exchangers>) {
        listExchangers.value = exchangers
    }

    fun observableResponse(): LiveData<ReceivedExchangers> = exchangers

    fun observableListExchangers(): LiveData<List<Exchangers>> = listExchangers

}