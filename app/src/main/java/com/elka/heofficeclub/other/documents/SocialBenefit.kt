package com.elka.heofficeclub.other.documents

import com.elka.heofficeclub.other.toDocFormat
import java.util.*

data class SocialBenefit(
  val name: String = "",
  val docSerialNumber: String = "",
  val docDate: Date = Date(),
  val doc: String = "",
): java.io.Serializable {
  val docDateS get() = docDate.toDocFormat()

}