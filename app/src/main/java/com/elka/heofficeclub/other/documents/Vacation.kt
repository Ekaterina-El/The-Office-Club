package com.elka.heofficeclub.other.documents

import com.elka.heofficeclub.other.getDaysBetween
import java.util.*

data class Vacation(
  val type: String = "",
  val workStart: Date = Date(0),
  val workEnd: Date = Date(0),
  val contOfDays: Date = Date(),
  val vacationStart: Date = Date(0),
  val vacationEnd: Date = Date(0),
  val doc: String = "",
) {
  var countOfDats =  getDaysBetween(vacationStart, vacationEnd)
}