package com.kaelesty.madprojects_kmp.extensions

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.toLocalDateTime

fun Long.toReadableTime(): String {
    val currentTime = Clock.System.now()
    val elapsedTime = currentTime - Instant.fromEpochMilliseconds(this)

    val seconds = elapsedTime.inWholeSeconds
    val minutes = seconds / 60
    val hours = minutes / 60
    val days = hours / 24
    val months = days / 30
    val years = days / 365

    return when {
        seconds < 60 -> "$seconds сек."
        minutes < 60 -> "$minutes мин."
        hours < 24 -> "$hours ч."
        days < 30 -> "$days дн."
        months < 12 -> "$months мес."
        else -> {
            val instant = Instant.fromEpochMilliseconds(this)
            val localDateTime = instant.toLocalDateTime(TimeZone.currentSystemDefault())
            val format = LocalDateTime.Format {
                dayOfMonth()
                chars(".")
                monthNumber()
                chars(".")
                year()
            }
            format.format(localDateTime)
        }
    }
}