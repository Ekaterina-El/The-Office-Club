package com.elka.heofficeclub.other.documents

import java.util.*

data class ProfTraining(
  val dataStart: Date = Date(0),
  val dataEnd: Date = Date(0),
  val type: String = "",
  val institute: String = "",
  val docType: String = "",
  val docSerialNumber: String = "",
  val docDate: Date = Date(),
  val doc: String = "",
): java.io.Serializable
