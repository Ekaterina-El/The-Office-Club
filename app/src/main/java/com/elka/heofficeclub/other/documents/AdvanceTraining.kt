package com.elka.heofficeclub.other.documents

import com.elka.heofficeclub.other.toDocFormat
import java.util.*

data class AdvanceTraining(
  val dateStart: Date = Date(0),
  val dateEnd: Date = Date(0),
  val type: String = "",
  val institute: String = "",
  val typeDoc: String = "",
  val numberDoc: String = "",
  val dateDoc: Date = Date(0),
  val doc: String = ""
) : java.io.Serializable {
  val dateStartS get() = dateStart.toDocFormat()
  val dateEndS get() = dateEnd.toDocFormat()
  val dateDocS get() = dateDoc.toDocFormat()
}