package com.obriylabs.currencyandroid.domain.exception

/**
 * Base Class for handling errors/failures/exceptions.
 * Every feature specific failure should extend [FeatureFailure] class.
 */
sealed class Failure {

    object NetworkConnection : Failure()
    object ServerError : Failure()
    object FileError : Failure()
    object DatabaseError : Failure()

    /** * Extend this class for feature specific failures.*/
    abstract class FeatureFailure: Failure()
}
