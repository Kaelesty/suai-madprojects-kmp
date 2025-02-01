package com.kaelesty.madprojects.view.extensions

fun <T> List<T>.copyWith(new: T): List<T> {
    return this.toMutableList()
        .apply { add(new) }
        .toList()
}