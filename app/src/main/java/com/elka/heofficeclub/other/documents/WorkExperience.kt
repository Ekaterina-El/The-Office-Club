package com.elka.heofficeclub.other.documents

import java.util.*

data class WorkExperience(
  val date: Date = Date(0),
  var divisionName: String = "",
  var place: String = "",
  var salary: String = "",
  var doc: String = "",
)