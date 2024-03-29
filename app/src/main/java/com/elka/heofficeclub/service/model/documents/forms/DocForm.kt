package com.elka.heofficeclub.service.model.documents.forms

import com.elka.heofficeclub.other.documents.FormType
import com.elka.heofficeclub.other.toDocFormat
import com.elka.heofficeclub.other.toDocFormatWithTime
import java.util.*

open class DocForm(
  open var id: String = "",
  open val fileUrl: String? = null,
  open var number: Int = 0,
  open val orgId: String = "",
  open val orgName: String = "",
  open val codeOKPO: String = "",
  open val dataCreated: Date = Calendar.getInstance().time,
  open val type: FormType = FormType.T1
) : java.io.Serializable {
  var dataCreatedS
    get() = dataCreated.toDocFormat()
    set(v) {}

  var dataCreatedSWithTime
    get() = dataCreated.toDocFormatWithTime()
    set(v) {}
}

fun List<DocForm>.filterBy(search: String) = this.filter {
  it.number.toString().contains(search, true) || it.dataCreatedS.contains(
    search, true
  ) || it.type.text.contains(search, true)
}