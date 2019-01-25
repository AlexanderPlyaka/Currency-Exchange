package com.obriylabs.currencyandroid.extension

import android.content.Context
import android.net.ConnectivityManager

val Context.isOnline: Boolean
    get() = (getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager).activeNetworkInfo
            ?.isConnectedOrConnecting ?: false
