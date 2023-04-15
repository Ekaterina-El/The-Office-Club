package com.elka.heofficeclub.service.model.documents.forms

import com.elka.heofficeclub.other.documents.FormType
import com.elka.heofficeclub.other.toWords
import com.elka.heofficeclub.service.model.Division
import com.elka.heofficeclub.service.model.Employer
import com.elka.heofficeclub.service.model.Organization
import com.elka.heofficeclub.service.model.OrganizationPosition
import java.util.*

class T11(
  override val type: FormType = FormType.T11,
  override var fileUrl: String? = null,
  override var number: Int = 0,
  override val dataCreated: Date = Calendar.getInstance().time,
  val organization: Organization? = null,

  val employer: Employer? = null,
  val division: Division? = null,
  val position: OrganizationPosition? = null,

  val description: String = "",
  val giftType: String = "",

  val sum: Double = 0.0,

  val reason: String = ""

) : DocForm(
  fileUrl = fileUrl,
  number = number,
  orgId = organization?.id ?: "",
  orgName = organization?.fullName ?: "",
  codeOKPO = organization?.okpo ?: "",
  dataCreated = dataCreated,
  type = type
) {
  var sumS:
      String
    get() = sum.toInt().toWords()
    set(v) {}
}