package com.obriylabs.currencyandroid.domain.interactor

import com.obriylabs.currencyandroid.domain.Result
import com.obriylabs.currencyandroid.domain.exception.Failure
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

/**
 * Interface for a Use Case (Interactor in terms of Clean Architecture).
 * This abstraction represents an execution unit for different use cases (this means than any use
 * case in the application should implement this contract).
 *
 * By convention each [UseCase] implementation will execute its job in a background thread
 * (kotlin coroutine) and will post the result in the UI thread.
 */
interface UseCase<out Type, in Params> where Type : Any {

    suspend fun run(params: Params): Result<Failure, Type>

    operator fun invoke(params: Params, onResult: (Result<Failure, Type>) -> Unit = {}) {
        val job = GlobalScope.async(Dispatchers.IO) { run(params) }
        GlobalScope.launch(Dispatchers.Main) { onResult(job.await()) }
    }

    interface JustRun<Params> {

        suspend fun run(params: Params)

        operator fun invoke(params: Params) {
            GlobalScope.launch(Dispatchers.IO) { run(params) }
        }
    }

    class None
}
