package com.obriylabs.currencyandroid.interactor

import com.obriylabs.currencyandroid.utils.Elector
import com.obriylabs.currencyandroid.exception.Failure
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

/**
 * Abstract class for a Use Case (Interactor in terms of Clean Architecture).
 * This abstraction represents an execution unit for different use cases (this means than any use
 * case in the application should implement this contract).
 *
 * By convention each [UseCase] implementation will execute its job in a background thread
 * (kotlin coroutine) and will post the result in the UI thread.
 */
interface UseCase<out Type, in Params> where Type : Any {

    suspend fun run(params: Params): Elector<Failure, Type>

    operator fun invoke(params: Params, onResult: (Elector<Failure, Type>) -> Unit = {}) {
        val job = GlobalScope.async(Dispatchers.IO) { run(params) }
        GlobalScope.launch(Dispatchers.Main) { onResult(job.await()) }
    }

    class None
}
