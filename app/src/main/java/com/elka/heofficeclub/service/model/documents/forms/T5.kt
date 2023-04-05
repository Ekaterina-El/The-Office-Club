package com.elka.heofficeclub.service.model.documents.forms

import com.elka.heofficeclub.other.documents.FormType
import com.elka.heofficeclub.other.documents.TypeOfChangeWork
import com.elka.heofficeclub.service.model.Division
import com.elka.heofficeclub.service.model.Employer
import com.elka.heofficeclub.service.model.Organization
import com.elka.heofficeclub.service.model.OrganizationPosition
import java.util.*

class T5(
  override val type: FormType = FormType.T5,
  override var fileUrl: String? = null,
  override var number: Int = 0,
  override val dataCreated: Date = Calendar.getInstance().time,
  val organization: Organization? = null,

  val employer: Employer? = null,

  val transferReason: String= "",
  val oldDivision: Division? = null,
  val oldPosition: OrganizationPosition? = null,

  val newDivision: Division? = null,
  val newPosition: OrganizationPosition? = null,

  val typeOfChangeWork: TypeOfChangeWork = TypeOfChangeWork.PERMANENT,
  val premium: Double = 0.0,

  var contractData: Date? = null,
  var contractNumber: String = "",
  val foundation: String = "",

  val transferStart: Date? = null,
  val transferEnd: Date? = null

  ) : DocForm(
  fileUrl = fileUrl,
  number = number,
  orgId = organization!!.id,
  orgName = organization.fullName,
  codeOKPO = organization.okpo,
  dataCreated = dataCreated,
  type = type
)