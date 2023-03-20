package com.elka.heofficeclub.other.documents

import java.util.*

data class Gift(
  val name: String = "",
  val docType: String = "",
  val docSerialNumber: String = "",
  val docDate: Date = Date(),
)