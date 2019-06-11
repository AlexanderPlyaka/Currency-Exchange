package com.obriylabs.currencyandroid.data

import com.obriylabs.currencyandroid.domain.exception.Failure

sealed class Rejection : Failure.FeatureFailure() {

    object DateEquals : Rejection()

}