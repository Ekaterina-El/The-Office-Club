package com.elka.heofficeclub.other

import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit


val local: Locale by lazy { Locale.getDefault() }
val sdf: SimpleDateFormat by lazy { SimpleDateFormat("dd/MM/yyyy", local) }
val docSdf: SimpleDateFormat by lazy { SimpleDateFormat("dd.MM.yyyy", local) }

fun Date.format(): String = sdf.format(this)
fun Date.toDocFormat(): String = docSdf.format(this)

fun String.fromDocFormatToDate(): Date? = docSdf.parse(this)
fun getDaysBetween(d1: Date?, d2: Date?): Long {
  if (d1 == null || d2 == null) return 0L

  val diff = d2.time - d1.time
  return TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS)
}

fun Double.toRub(): String = this.toString().split(".")[0]
fun String.toIntOrEmpty() = if (this == "") 0 else this.toInt()

fun Double.toCent(): String = this.toString().split(".")[1]

fun getCurrentTime() = Calendar.getInstance().time