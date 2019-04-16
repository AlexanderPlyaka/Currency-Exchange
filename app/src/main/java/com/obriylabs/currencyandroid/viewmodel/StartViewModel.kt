package com.obriylabs.currencyandroid.viewmodel

import android.arch.lifecycle.MutableLiveData
import com.obriylabs.currencyandroid.api.DateResponse
import com.obriylabs.currencyandroid.api.ExchangersResponse
import com.obriylabs.currencyandroid.interactor.DatabaseDate
import com.obriylabs.currencyandroid.interactor.Exchangers
import com.obriylabs.currencyandroid.interactor.ExchangersFile
import com.obriylabs.currencyandroid.interactor.UseCase
import okhttp3.ResponseBody
import javax.inject.Inject

class StartViewModel @Inject constructor(private val databaseDate: DatabaseDate,
                                         private val exchangers: Exchangers,
                                         private val exchangersFile: ExchangersFile) : BaseViewModel() {

    private var listOfExchangers: MutableLiveData<List<ExchangersResponse.Result>> = MutableLiveData()

    fun getListOfExchangers() = databaseDate(UseCase.None()) { it.elector(::handleFailure, ::handleDate) }

    private fun handleDate(date: DateResponse) {
        exchangers(date.filePath) { it.elector(::handleFailure, ::handleResponse) }
    }

    private fun handleResponse(response: ResponseBody) {
        exchangersFile(response.bytes()) {it.elector(::handleFailure, ::handleResult)}
    }

    private fun handleResult(exchangers: ExchangersResponse) {
        this.listOfExchangers.value = exchangers.results
    }

}
