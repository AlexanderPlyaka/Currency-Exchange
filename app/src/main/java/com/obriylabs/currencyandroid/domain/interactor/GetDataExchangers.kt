package com.obriylabs.currencyandroid.domain.interactor

import com.obriylabs.currencyandroid.domain.entity.DataEntity
import com.obriylabs.currencyandroid.data.repository.INetworkRepository
import com.obriylabs.currencyandroid.domain.Result
import com.obriylabs.currencyandroid.domain.exception.Failure
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class GetDataExchangers
@Inject constructor(private val networkRepository: INetworkRepository) : UseCase<DataEntity, UseCase.None> {

    override suspend fun run(params: UseCase.None) : Result<Failure, DataEntity> = networkRepository.data()

    override fun invoke(params: UseCase.None, onResult: (Result<Failure, DataEntity>) -> Unit) {
        GlobalScope.launch(Dispatchers.IO) { onResult(run(params)) }
    }

}