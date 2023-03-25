package com.elka.heofficeclub.other.documents

import java.util.*


data class Education(
  val institute: String = "",
  val documentOfEducation: String = "",
  var yearOfGraduation: String = "",
  var dateOfGraduation: Date = Date(0),
  var qualification: String = "",
  var directionCode: String = "",
  var serial: String = "",
  var number: String = "",
): java.io.Serializable