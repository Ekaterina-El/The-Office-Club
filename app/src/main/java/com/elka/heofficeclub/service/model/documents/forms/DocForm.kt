package com.elka.heofficeclub.service.model.documents.forms

import java.util.*

open class DocForm(
  open val fileUrl: String = "",
  open var number: String = "",
  open val orgId: String = "",
  open val orgName: String = "",
  open val codeOKPO: String = "",
  open val dataCreated: Date = Calendar.getInstance().time,
)