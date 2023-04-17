package com.elka.heofficeclub.service.model.documents.forms

import com.elka.heofficeclub.other.documents.FormType
import com.elka.heofficeclub.other.toDocFormat
import com.elka.heofficeclub.service.model.Employer
import com.elka.heofficeclub.service.model.Organization
import java.util.*

class T8(
  override var id: String = "",
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
  id = id,
  fileUrl = fileUrl,
  number = number,
  orgId = organization?.id ?: "",
  orgName = organization?.fullName ?: "",
  codeOKPO = organization?.okpo ?: "",
  dataCreated = dataCreated,
  type = type
) {

  var reasonDocNumberDate: String
    get() {
      return "$reasonDoc №${reasonNumber} (${reasonDate?.toDocFormat()})"
    }
    set(v) {}

  var dismissalDateS
    get() = dismissalDate?.toDocFormat()
    set(v) {}


  var division
    get() = employer.divisionLocal
    set(v) {}

  var position
    get() = employer.positionLocal
    set(v) {}

  var contractData
    get() = employer.T1Local?.contractData
    set(v) {}

  var contractNumber
    get() = employer.T1Local?.contractNumber
    set(v) {}

}