package com.elka.heofficeclub.service.model

import com.elka.heofficeclub.other.toDocFormat
import com.elka.heofficeclub.service.model.documents.forms.T1
import com.elka.heofficeclub.service.model.documents.forms.T2
import com.elka.heofficeclub.service.model.documents.forms.T8
import java.text.DecimalFormat
import java.util.*

data class Employer(
  var id: String = "",
  val tableNumber: Int = 0,

  var docs: List<String> = listOf(),

  var T1: String = "",
  var T1Local: T1? = null,

  var T2: String = "",
  var T2Local: T2? = null,

  var T8: String? = null,
  var T8Local: T8? = null,

  var divisionId: String? = "",
  var divisionLocal: Division? = null,

  var divisionTempId: String? = "",
  var divisionTempLocal: Division? = null,

  var organizationId: String = "",

  var positionId: String? = "",
  var positionLocal: OrganizationPosition? = null,

  var positionTempId: String? = "",
  var positionTempLocal: OrganizationPosition? = null,

  var premium: Double = 0.0,
  var tempPremium: Double = 0.0,

  var startWorkTmp: Date? = null,
  var endWorkTmp: Date? = null,
) : java.io.Serializable {
  val startWorkTmpS: String get() = startWorkTmp?.toDocFormat() ?: ""
  val endWorkTmpS: String get() = endWorkTmp?.toDocFormat() ?: ""

  var tableNumberS
    get() = formatter.format(tableNumber)
    set(v) {}

  companion object {
    val formatter = DecimalFormat("0000")
  }
}


fun List<Employer>.filterBy(search: String) = this.filter {
  it.tableNumberS.contains(search, ignoreCase = true) || it.T2Local?.fullName?.contains(
    search, ignoreCase = true
  ) ?: false || it.positionLocal?.name?.contains(
    search, ignoreCase = true
  ) ?: false || it.divisionLocal?.name?.contains(search, ignoreCase = true) ?: false
}

fun List<Employer>.splitByT8(): List<Employer> {
  val dismissalEmployers = this.filter { it.T8Local != null }
  val noDismissalEmployers = this.filter { it.T8Local == null }.toMutableList()
  noDismissalEmployers.addAll(dismissalEmployers)
  return noDismissalEmployers
}