package com.elka.heofficeclub.other.documents

import java.util.*

data class SocialBenefit(
  val name: String = "",
  val docSerialNumber: String = "",
  val docDate: Date = Date(),
  val doc: String = "",
): java.io.Serializable