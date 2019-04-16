package com.obriylabs.currencyandroid.utils

/**
 * Represents a value of one of two possible types (a disjoint union).
 * Instances of [Elector] are elector an instance of [Error] or [Success].
 * FP Convention dictates that [Error] is used for "failure"
 * and [Success] is used for "success".
 *
 * @see Error
 * @see Success
 */
sealed class Elector<out E, out S> {
    /** * Represents the error side of [Elector] class which by convention is a "Failure". */
    data class Error<out L>(val a: L) : Elector<L, Nothing>()
    /** * Represents the success side of [Elector] class which by convention is a "Success". */
    data class Success<out R>(val b: R) : Elector<Nothing, R>()

    val isError get() = this is Error<E>
    val isSuccess get() = this is Success<S>

    fun <E> error(a: E) = Error(a)
    fun <S> success(b: S) = Success(b)

    fun elector(fnL: (E) -> Any, fnR: (S) -> Any): Any =
            when (this) {
                is Error -> fnL(a)
                is Success -> fnR(b)
            }
}
