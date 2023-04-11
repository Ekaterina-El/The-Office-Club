package com.elka.heofficeclub.service.model.documents.forms

import com.elka.heofficeclub.other.documents.FormType
import com.elka.heofficeclub.other.toDocFormat
import com.elka.heofficeclub.service.model.Division
import com.elka.heofficeclub.service.model.Employer
import com.elka.heofficeclub.service.model.Organization
import com.elka.heofficeclub.service.model.OrganizationPosition
import java.util.*

class T8(
  override val type: FormType = FormType.T8,
  override var fileUrl: String? = null,
  override var number: Int = 0,
  override val dataCreated: Date = Calendar.getInstance().time,
  val organization: Organization? = Organization(),

  var employer: Employer = Employer(),

  var dismissalDate: Date? = null,

  var reason: String = "",
  var reasonDoc: String = "",
  var reasonNumber: String = "",
  var reasonDate: Date? = null,
) : DocForm(
  fileUrl = fileUrl,
  number = number,
  orgId = organization!!.id,
  orgName = organization.fullName,
  codeOKPO = organization.okpo,
  dataCreated = dataCreated,
  type = type
) {

  val reasonDocNumberDate: String get() {
    return "$reasonDoc №${reasonNumber} (${reasonDate?.toDocFormat()})"
  }

  val dismissalDateS get() = dismissalDate?.toDocFormat()

  val division get() = employer.divisionLocal
  val position get() = employer.positionLocal

  val contractData get() = employer.T1Local?.contractData
  val contractNumber get() = employer.T1Local?.contractNumber
}