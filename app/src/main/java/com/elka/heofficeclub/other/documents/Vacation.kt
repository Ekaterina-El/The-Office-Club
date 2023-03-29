package com.elka.heofficeclub.other.documents

import com.elka.heofficeclub.other.getDaysBetween
import com.elka.heofficeclub.other.toDocFormat
import java.util.*

data class Vacation(
  val type: String = "",
  val workStart: Date = Date(0),
  val workEnd: Date = Date(0),
  val vacationStart: Date = Date(0),
  val vacationEnd: Date = Date(0),
  val doc: String = "",
): java.io.Serializable {
  var countOfDays =  getDaysBetween(vacationStart, vacationEnd)

  val workStartS get() = workStart.toDocFormat()
  val workEndS get() = workEnd.toDocFormat()
  val vacationStartS get() = vacationStart.toDocFormat()
  val vacationEndS get() = vacationEnd.toDocFormat()
}