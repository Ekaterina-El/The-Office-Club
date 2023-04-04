package com.elka.heofficeclub.other.documents.creators

import android.content.Context
import com.elka.heofficeclub.other.FieldType
import com.elka.heofficeclub.other.documents.DocField
import com.elka.heofficeclub.other.documents.FormCreator
import com.elka.heofficeclub.other.getDMY2
import com.elka.heofficeclub.other.toDocFormat
import com.elka.heofficeclub.service.model.documents.forms.DocForm
import com.elka.heofficeclub.service.model.documents.forms.T11
import com.elka.heofficeclub.service.model.documents.forms.T6
import com.itextpdf.forms.fields.PdfFormField
import com.itextpdf.forms.fields.PdfTextFormField
import com.itextpdf.kernel.geom.Rectangle
import com.itextpdf.kernel.pdf.PdfDocument
import java.util.*

class T11Creator(private val context: Context) : FormCreator(context) {
  override val assetName = "T-11.pdf"
  override val postfix: String = "T11/"

  override fun getFields(outputDoc: PdfDocument, docField: DocForm): List<PdfFormField> {
    val value = docField as T11

    val employer = value.employer!!.T2Local!!
    val sumD = value.sum.toInt()
    val cents = ((value.sum - sumD.toDouble()) * 100).toInt()

    val list = mutableListOf(
      // org name
      DocField(
        Rectangle(56f, 729.84f, 376.56f, 12f), FieldType.ORG_NAME, value.orgName, DocField.CENTER
      ),

      // org okpo
      DocField(
        Rectangle(485.4f, 729.84f, 81.12f, 12f), FieldType.CODE_OKPO, value.codeOKPO, DocField.CENTER
      ),

      // doc number
      DocField(
        Rectangle(342.24f, 681.84f, 83.04f, 13.2f),
        FieldType.DOC_NUMBER,
        value.number.toString(),
        DocField.CENTER
      ),

      // date of created
      DocField(
        Rectangle(426f, 681.84f, 83.04f, 13.2f),
        FieldType.DOC_NUMBER,
        value.dataCreated.toDocFormat(),
        DocField.CENTER
      ),

      // fill name
      DocField(
        Rectangle(56.28f, 616.87f, 424.29f, 12f),
        FieldType.EMPLOYER_FULL_NAME,
        employer.fullName,
        DocField.CENTER
      ),

      // table number
      DocField(
        Rectangle(485.4f, 617.16f, 81.12f, 12f),
        FieldType.EMPLOYER_TABLE_NUMBER,
        employer.tableNumber.toString(),
        DocField.CENTER
      ),

      // division
      DocField(
        Rectangle(56.64f, 595.92f, 510.48f, 12f),
        FieldType.DIVISION,
        value.division!!.name,
        DocField.CENTER
      ),

      // position
      DocField(
        Rectangle(56.2f, 574.68f, 510.48f, 12f),
        FieldType.DIVISION,
        value.position!!.name,
        DocField.CENTER
      ),

      // description
      DocField(
        Rectangle(56.64f, 531.48f, 510.48f, 12f),
        FieldType.DIVISION,
        value.description,
        DocField.CENTER
      ),

      // gift type
      DocField(
        Rectangle(56.64f, 454.56f, 510.48f, 12f),
        FieldType.DIVISION,
        value.giftType,
        DocField.CENTER
      ),

      // sum (string)
      DocField(
        Rectangle(99.25f, 397.8f, 467.88f, 12f),
        FieldType.DIVISION,
        value.sumS,
        DocField.CENTER
      ),

      // cents 1
      DocField(
        Rectangle(495.36f, 376.56f, 46.2f, 12f),
        FieldType.DIVISION,
        cents.toString(),
        DocField.CENTER
      ),

      // sun (numbers)
      DocField(
        Rectangle(409.68f, 361.2f, 70.92f, 12f),
        FieldType.DIVISION,
        sumD.toString(),
        DocField.CENTER
      ),

      // cents 2
      DocField(
        Rectangle(510.24f, 361.2f, 28.56f, 12f),
        FieldType.DIVISION,
        cents.toString(),
        DocField.CENTER
      ),

      // reasons
      DocField(
        Rectangle(56.64f, 309.84f, 510.48f, 12f),
        FieldType.DIVISION,
        value.reason,
        DocField.CENTER
      ),
    )

    return list.map {
      return@map PdfTextFormField.createText(
          outputDoc,
          it.wrapper,
          it.type.fieldName,
          it.v,
          font,
          10f
        ).setJustification(1)
    }
  }
}

