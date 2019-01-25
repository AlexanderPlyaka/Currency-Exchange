package com.obriylabs.currencyandroid.extension

import android.util.Log
import java.lang.Exception

fun Any.logD(message: String) {
    Log.d("Log -> " + this::class.java.simpleName, message)
}

fun Any.logE(message: String, exception: Exception?) {
    Log.e("Log -> " + this::class.java.simpleName, message, exception)
}

fun logE(tag: String, message: String?) {
    Log.e(tag, message)
}