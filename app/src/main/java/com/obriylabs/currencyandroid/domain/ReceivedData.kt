package com.obriylabs.currencyandroid.domain

data class ReceivedData(val bytes: ByteArray) {

    companion object {
        fun empty() = ReceivedData(byteArrayOf())
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ReceivedData

        if (!bytes.contentEquals(other.bytes)) return false

        return true
    }

    override fun hashCode(): Int {
        return bytes.contentHashCode()
    }
}