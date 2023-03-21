package com.elka.heofficeclub.other.documents.creators

import android.content.Context
import com.elka.heofficeclub.other.FieldType
import com.elka.heofficeclub.other.documents.DocField
import com.elka.heofficeclub.other.documents.FormCreator
import com.elka.heofficeclub.other.toDocFormat
import com.elka.heofficeclub.service.model.documents.forms.DocForm
import com.elka.heofficeclub.service.model.documents.forms.T8
import com.itextpdf.forms.fields.PdfFormField
import com.itextpdf.forms.fields.PdfTextFormField
import com.itextpdf.kernel.geom.Rectangle
import com.itextpdf.kernel.pdf.PdfDocument

class T8Creator(context: Context) : FormCreator(context) {
  override val assetName: String = "T_8.pdf"
  override val postfix: String = "T8/"

  override fun getFields(outputDoc: PdfDocument, docField: DocForm): List<PdfFormField> {
    val value = docField as T8

    val month = ""
    val year = ""

    val list = listOf(
      DocField(
        Rectangle(56f, 729f, 386.56f, 12.24f),
        FieldType.ORG_NAME,
        value.orgName,
        DocField.CENTER
      ),

      DocField(
        Rectangle(496.68f, 729.84f, 69.84f, 11f),
        FieldType.CODE_OKPO,
        value.codeOKPO,
        DocField.CENTER
      ),

      DocField(
        Rectangle(343.8f, 652.32f, 82.2f, 13.2f),
        FieldType.DOC_NUMBER,
        value.number.toString(),
        DocField.CENTER
      ),

      DocField(
        Rectangle(427.32f, 652.32f, 89.32f, 13.2f),
        FieldType.DOC_CREATED,
        value.dataCreated.toDocFormat(),
        DocField.CENTER
      ),

      DocField(
        Rectangle(290f, 587.64f, 20.04f, 12.6f),
        FieldType.CONTRACT_DAY,
        ((value.contractData?.day ?: (0 + 1))).toString(),
        DocField.CENTER
      ),

      DocField(
        Rectangle(324.36f, 587.64f, 103.8f, 12.6f),
        FieldType.CONTRACT_MONTH,
        month,
        DocField.CENTER
      ),

      DocField(
        Rectangle(440.28f, 587.64f, 29f, 12.6f),
        FieldType.CONTRACT_YEAR,
        year,
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