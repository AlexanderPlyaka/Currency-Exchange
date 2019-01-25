package com.obriylabs.currencyandroid.ui.fragments.start

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import com.obriylabs.currencyandroid.api.ExchangersResponse
import com.obriylabs.currencyandroid.repository.ExchangersRepository
import com.obriylabs.currencyandroid.vo.Resource
import javax.inject.Inject

class StartViewModel @Inject constructor(exchangersRepository: ExchangersRepository) : ViewModel() {

    val exchangers: LiveData<Resource<List<ExchangersResponse.Result>>> = exchangersRepository.loadExchangers()

}
