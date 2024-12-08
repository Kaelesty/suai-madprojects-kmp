package com.kaelesty.data.auth

private const val addedSymbol = '\u0011'
fun String.aesPrepare(): String {
    if (length % 2 != 0) {
        return this + addedSymbol
    }
    return this
}

fun String.fromAes(): String {
    if (last() == addedSymbol) {
        return dropLast(1)
    }
    return this
}