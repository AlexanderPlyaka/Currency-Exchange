package com.obriylabs.currencyandroid.utils

object SecretKeys {

    init {
        System.loadLibrary("native-lib")
    }

    external fun getPass(): String

    external fun getUrl(): String

}