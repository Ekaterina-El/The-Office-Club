package com.elka.heofficeclub.service.model.documents.forms

import com.elka.heofficeclub.other.T7Row
import com.elka.heofficeclub.other.documents.FormType
import com.elka.heofficeclub.other.getYearOfDate
import com.elka.heofficeclub.service.model.Organization
import java.util.*

class T7(
  override val type: FormType = FormType.T7,
  override var fileUrl: String? = null,
  override var number: Int = 0,
  override val dataCreated: Date = Calendar.getInstance().time,
  val organization: Organization? = null,
  val rows: List<T7Row> = listOf()
) : DocForm(
  fileUrl = fileUrl,
  number = number,
  orgId = organization?.id ?: "",
  orgName = organization?.fullName ?: "",
  codeOKPO = organization?.okpo ?: "",
  dataCreated = dataCreated,
  type = type
) {
  var yearOfGraphic
    get() = getYearOfDate(dataCreated)
    set(v) {}
}