package com.obriylabs.currencyandroid.presentation.viewmodel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.obriylabs.currencyandroid.data.model.Exchangers
import com.obriylabs.currencyandroid.domain.interactor.GetExchangers
import com.obriylabs.currencyandroid.domain.interactor.UseCase
import javax.inject.Inject

class MapsViewModel @Inject constructor(private val getExchangers: GetExchangers) : BaseViewModel() {

    private var listExchangers: MutableLiveData<List<Exchangers>> = MutableLiveData()

    fun getListExchangersFromDb() {
        if (listExchangers.value == null) {
            getExchangers(UseCase.None()) { it.result(::handleFailure, ::handleResult) }
        }
    }

    private fun handleResult(exchangers: List<Exchangers>) {
        listExchangers.postValue(exchangers)
    }

    fun listExchangers(): LiveData<List<Exchangers>> = listExchangers

}
