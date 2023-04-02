package com.elka.heofficeclub.other.documents.creators

import android.content.Context
import com.elka.heofficeclub.other.Constants
import com.elka.heofficeclub.other.FieldType
import com.elka.heofficeclub.other.documents.DocField
import com.elka.heofficeclub.other.documents.FormCreator
import com.elka.heofficeclub.service.model.documents.forms.DocForm
import com.elka.heofficeclub.service.model.documents.forms.T1
import com.elka.heofficeclub.service.model.documents.forms.T6
import com.itextpdf.forms.fields.PdfFormField
import com.itextpdf.forms.fields.PdfTextFormField
import com.itextpdf.kernel.geom.Rectangle
import com.itextpdf.kernel.pdf.PdfDocument
import java.util.*

class T6Creator(private val context: Context) : FormCreator(context) {
  override val assetName = "T_6.pdf"
  override val postfix: String = "T6/"

  override fun getFields(outputDoc: PdfDocument, docField: DocForm): List<PdfFormField> {
    val value = docField as T6

    // TODO: DO IT!!!
    val list = listOf(
      DocField(
        Rectangle(56f, 729f, 383f, 12f),
        FieldType.ORG_NAME,
        value.orgName,
        DocField.CENTER
      ),
    )

    return list.map {
      return@map PdfTextFormField
        .createText(outputDoc, it.wrapper, it.type.fieldName, it.v, font, 10f)
        .setJustification(1)
    }

  }
}

