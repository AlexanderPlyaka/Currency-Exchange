package com.obriylabs.currencyandroid.domain

/**
 * Represents a value of one of two possible types (a disjoint union).
 * Instances of [Result] are result an instance of [Error] or [Success].
 * FP Convention dictates that [Error] is used for "failure"
 * and [Success] is used for "success".
 *
 * @see Error
 * @see Success
 */
sealed class Result<out E, out S> {

    /** * Represents the error side of [Result] class which by convention is a "Failure". */
    data class Error<out E>(val value: E) : Result<E, Nothing>()
    /** * Represents the success side of [Result] class which by convention is a "Success". */
    data class Success<out S>(val value: S) : Result<Nothing, S>()

    val isError get() = this is Error<E>
    val isSuccess get() = this is Success<S>

    fun <E> error(value: E) = Error(value)
    fun <S> success(value: S) = Success(value)

    fun result(fnE: (E) -> Any, fnS: (S) -> Any): Any =
            when (this) {
                is Error -> fnE(value)
                is Success -> fnS(value)
            }
}
