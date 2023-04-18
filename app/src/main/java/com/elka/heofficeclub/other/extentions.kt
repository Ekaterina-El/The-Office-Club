package com.elka.heofficeclub.other

import android.content.Context
import com.ibm.icu.text.RuleBasedNumberFormat
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit


val local: Locale by lazy { Locale.getDefault() }
val sdf: SimpleDateFormat by lazy { SimpleDateFormat("dd/MM/yyyy", local) }
val docSdf: SimpleDateFormat by lazy { SimpleDateFormat("dd.MM.yyyy", local) }
val docSdfWithTime: SimpleDateFormat by lazy { SimpleDateFormat("dd.MM.yyyy HH:mm:ss", local) }
val downloadDocSdf: SimpleDateFormat by lazy { SimpleDateFormat("dd_MM_yyyy___HH_mm_ss", local) }

fun Date.format(): String = sdf.format(this)
fun Date.toDocFormat(): String = docSdf.format(this)
fun Date.toDocFormatWithTime(): String = docSdfWithTime.format(this)
fun String.fromDocFormatWithTime(): Date? = if (this == "") null else docSdfWithTime.parse(this)
fun Date.toDownloadDocFormat(): String = downloadDocSdf.format(this)

fun String.fromDocFormatToDate(): Date? = if (this == "") null else docSdf.parse(this)

fun getDaysBetween(d1: Date?, d2: Date?): Int {
  if (d1 == null || d2 == null) return 0

  val diff = d2.time - d1.time
  return TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS).toInt() + 1
}

fun Double.toRub(): String = this.toString().split(".")[0]
fun String.toIntOrEmpty() = if (this == "") 0 else this.toInt()

fun Double.toCent(): String = this.toString().split(".")[1]

fun getCurrentTime() = Calendar.getInstance().time

fun getDMY2(context: Context, date: Date): Array<String> {
  val c = Calendar.getInstance()
  val currentTime = c.time

  c.time = date
  val d = c.get(Calendar.DAY_OF_MONTH).toString()
  val y = c.get(Calendar.YEAR).toString().substring(2, 4)
  val m = context.getString(Constants.months[c.get(Calendar.MONTH)])

  c.time = currentTime
  return arrayOf(d, m, y)
}

fun getYearOfDate(date: Date): Int {
  val c = Calendar.getInstance()
  val currentTime = c.time

  c.time = date

  val year = c.get(Calendar.YEAR)
  c.time = currentTime

  return year
}


val convector by lazy {
  RuleBasedNumberFormat(
    Locale.forLanguageTag("ru"),
    RuleBasedNumberFormat.SPELLOUT
  )
}

fun Int.toWords(): String {
  return convector.format(this)
}