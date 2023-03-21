package com.elka.heofficeclub.service.model.documents

import com.elka.heofficeclub.other.documents.FormType
import com.elka.heofficeclub.service.model.documents.forms.DocForm

data class Document(
  var id: String = "",
  val fileUrl: String = "",
  val orgId: String = "",
  var docForm: DocForm? = null,
  var formType: FormType = FormType.T1
)