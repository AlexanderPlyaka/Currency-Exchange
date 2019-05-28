package com.obriylabs.currencyandroid.presentation.viewmodel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.obriylabs.currencyandroid.data.model.DataOfExchangers
import com.obriylabs.currencyandroid.data.model.Exchangers
import com.obriylabs.currencyandroid.data.model.ReceivedExchangers
import com.obriylabs.currencyandroid.domain.entity.ExchangersEntity
import com.obriylabs.currencyandroid.domain.interactor.*
import javax.inject.Inject

class SharedViewModel
@Inject constructor(private val getDataOfExchangers: GetDataOfExchangers,
                    private val getExchangersDb: GetExchangersDb,
                    private val getExchangersEntity: GetExchangersEntity,
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
                getExchangersEntity(GetExchangersEntity.Params(this)) {
                    it.result(::handleFailure, ::handleResult)
                }
            }
        }
    }

    private fun handleResult(exchangers: ExchangersEntity?) {
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