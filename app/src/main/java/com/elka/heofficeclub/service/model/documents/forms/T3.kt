package com.elka.heofficeclub.service.model.documents.forms

import com.elka.heofficeclub.other.T3Row
import com.elka.heofficeclub.other.documents.FormType
import com.elka.heofficeclub.service.model.Organization
import java.util.*

class T3(
  override val type: FormType = FormType.T3,
  override var fileUrl: String? = null,
  override var number: Int = 0,
  override val dataCreated: Date = Calendar.getInstance().time,
  val organization: Organization? = null,
  val rows: List<T3Row> = listOf()
) : DocForm(
  fileUrl = fileUrl,
  number = number,
  orgId = organization?.id ?: "",
  orgName = organization?.fullName ?: "",
  codeOKPO = organization?.okpo ?: "",
  dataCreated = dataCreated,
  type = type
) {
  var totalOfMonth
    get() = rows.sumOf { it.totalMonth.toDouble() }
    set(v) {}

  var countOfEmployees
    get() = rows.sumOf { it.countOfEmployees.toInt() }
    set(v) {}

}