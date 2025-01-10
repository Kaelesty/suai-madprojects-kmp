package com.kaelesty.madprojects_kmp.extensions

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun Long.toReadableTime(): String {
	val currentTime = System.currentTimeMillis()
	val elapsedTime = currentTime - this

	val seconds = (elapsedTime / 1000).toInt()
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
			val date = Date(this)
			val sdf = SimpleDateFormat("d MMMM в HH:mm", Locale("ru"))
			sdf.format(date)
		}
	}
}