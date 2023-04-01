package com.elka.heofficeclub.service.model.documents.forms

import com.elka.heofficeclub.other.documents.FormType
import com.elka.heofficeclub.other.getDaysBetween
import com.elka.heofficeclub.service.model.Division
import com.elka.heofficeclub.service.model.Employer
import com.elka.heofficeclub.service.model.Organization
import com.elka.heofficeclub.service.model.OrganizationPosition
import java.util.*

class T6(
  override val type: FormType = FormType.T6,
  override val fileUrl: String? = null,
  override var number: Int = 0,
  override val dataCreated: Date = Calendar.getInstance().time,

  val employer: Employer? = null,
  val division: Division? = null,
  val position: OrganizationPosition? = null,

  val startWork: Date? = null,
  val endWork: Date? = null,

  val startVacationA: Date? = null,
  val endVacationA: Date? = null,

  val startVacationB: Date? = null,
  val endVacationB: Date? = null,
  val vacationBDescription: String = "",
  val organization: Organization? = null,
) : DocForm(
  fileUrl = fileUrl,
  number = number,
  orgId = organization!!.id,
  orgName = organization.fullName,
  codeOKPO = organization.okpo,
  dataCreated = dataCreated,
  type = type
) {
  val vacationADays get() = getDaysBetween(startVacationA, endVacationA)
  val vacationBDays get() = getDaysBetween(startVacationB, endVacationB)

  val vacationStart: Date?
    get() {
      return if (startVacationB == null) startVacationA
      else if (startVacationA == null) startVacationB
      else if (startVacationA.time < startVacationB.time) startVacationA
      else startVacationB
    }

  val vacationEnd: Date?
    get() {
      return if (endVacationB == null) endVacationA
      else if (endVacationA == null) endVacationB
      else if (endVacationA.time > endVacationB.time) endVacationA
      else endVacationB
    }
}