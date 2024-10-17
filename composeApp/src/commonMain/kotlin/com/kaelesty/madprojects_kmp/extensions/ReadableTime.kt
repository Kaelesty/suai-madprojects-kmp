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
		seconds < 60 -> "$seconds секунд${if (seconds > 1) "ы" else ""} назад"
		minutes < 60 -> "$minutes минут${if (minutes > 1) "ы" else ""} назад"
		hours < 24 -> "$hours час${if (hours > 1) "а" else ""} назад"
		days < 30 -> "$days дн${if (days > 1) "я" else ""} назад"
		months < 12 -> "$months месяц${if (months > 1) "а" else ""} назад"
		else -> {
			val date = Date(this)
			val sdf = SimpleDateFormat("d MMMM в HH:mm", Locale("ru"))
			sdf.format(date)
		}
	}
}