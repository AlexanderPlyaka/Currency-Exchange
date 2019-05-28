package com.obriylabs.currencyandroid.domain.interactor

import com.obriylabs.currencyandroid.data.model.Exchangers
import com.obriylabs.currencyandroid.data.repository.IExchangersRepository
import com.obriylabs.currencyandroid.domain.Result
import com.obriylabs.currencyandroid.domain.entity.ExchangersEntity
import com.obriylabs.currencyandroid.domain.exception.Failure
import javax.inject.Inject

class GetExchangers
@Inject constructor(private val exchangersRepository: IExchangersRepository) : UseCase<List<Exchangers>, GetExchangers.Params> {

    override suspend fun run(params: GetExchangers.Params) : Result<Failure, List<Exchangers>> {
        if (exchangersRepository.fetchExchangersFromDb().isError) {
            exchangersRepository.saveExchangersToDb(mapToExchangers(params.items))
        }
        return exchangersRepository.fetchExchangersFromDb()
    }

    private fun mapToExchangers(items: List<ExchangersEntity.Result>?)
            : List<Exchangers> = items?.map { mapResultToExchangers(it) } ?: emptyList()

    private fun mapResultToExchangers(item: ExchangersEntity.Result): Exchangers = item.toExchangers()

    data class Params(val items: List<ExchangersEntity.Result>)

}