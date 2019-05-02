package com.obriylabs.currencyandroid.domain.interactor

import com.obriylabs.currencyandroid.data.model.Exchangers
import com.obriylabs.currencyandroid.data.repository.INetworkRepository
import com.obriylabs.currencyandroid.domain.entity.ExchangersEntity
import javax.inject.Inject

class SetExchangers
@Inject constructor(private val networkRepository: INetworkRepository) : UseCase.JustRun<SetExchangers.Params> {

    override suspend fun run(params: Params) {
        networkRepository.saveExchangersToDb(mapToExchangers(params.items))
    }

    private fun mapToExchangers(items: List<ExchangersEntity.Result>?)
            : List<Exchangers> = items?.map { mapResultToExchangers(it) } ?: emptyList()

    private fun mapResultToExchangers(item: ExchangersEntity.Result): Exchangers = item.toExchangers()

    data class Params(val items: List<ExchangersEntity.Result>)

}