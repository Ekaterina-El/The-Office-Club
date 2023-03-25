package com.elka.heofficeclub.other.documents

import java.util.*

data class Attestation(
  val date: Date = Date(0),
  val solution: String = "",
  val number: String = "",
  val solutionDate: Date = Date(0),
  val doc: String = ""
): java.io.Serializable