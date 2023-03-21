package com.elka.heofficeclub.service.model.documents.forms

import com.elka.heofficeclub.other.documents.FormType
import java.util.*

open class DocForm(
  var id: String = "",
  open val fileUrl: String = "",
  open var number: String = "",
  open val orgId: String = "",
  open val orgName: String = "",
  open val codeOKPO: String = "",
  open val dataCreated: Date = Calendar.getInstance().time,
  open val type: FormType
)