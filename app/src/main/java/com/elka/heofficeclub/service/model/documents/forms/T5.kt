package com.elka.heofficeclub.service.model.documents.forms

import android.util.Log
import com.elka.heofficeclub.R
import com.elka.heofficeclub.other.documents.FormType
import com.elka.heofficeclub.other.documents.TypeOfChangeDivision
import com.elka.heofficeclub.other.getDaysBetween
import com.elka.heofficeclub.other.toWords
import com.elka.heofficeclub.service.model.Division
import com.elka.heofficeclub.service.model.Employer
import com.elka.heofficeclub.service.model.Organization
import com.elka.heofficeclub.service.model.OrganizationPosition
import com.ibm.icu.text.RuleBasedNumberFormat
import java.util.*

class T5(
  override val type: FormType = FormType.T6,
  override var fileUrl: String? = null,
  override var number: Int = 0,
  override val dataCreated: Date = Calendar.getInstance().time,
  val organization: Organization? = null,

  val employer: Employer? = null,

  val oldDivision: Division? = null,
  val oldPosition: OrganizationPosition? = null,

  val newDivision: Division? = null,
  val newPosition: OrganizationPosition? = null,

  val typeOfChangeDivision: TypeOfChangeDivision = TypeOfChangeDivision.PERMANENT,
  val premium: Double = 0.0,

  var contractData: Date? = null,
  var contractNumber: String = "",
  val foundation: String = "",
) : DocForm(
  fileUrl = fileUrl,
  number = number,
  orgId = organization!!.id,
  orgName = organization.fullName,
  codeOKPO = organization.okpo,
  dataCreated = dataCreated,
  type = type
)