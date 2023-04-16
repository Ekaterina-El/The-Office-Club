package com.elka.heofficeclub.other.documents.creators

import android.content.Context
import com.elka.heofficeclub.other.FieldType
import com.elka.heofficeclub.other.documents.DocField
import com.elka.heofficeclub.other.documents.FormCreator
import com.elka.heofficeclub.other.getDMY2
import com.elka.heofficeclub.other.toDocFormat
import com.elka.heofficeclub.service.model.documents.forms.DocForm
import com.elka.heofficeclub.service.model.documents.forms.T8
import com.itextpdf.forms.fields.PdfFormField
import com.itextpdf.forms.fields.PdfTextFormField
import com.itextpdf.kernel.geom.Rectangle
import com.itextpdf.kernel.pdf.PdfDocument

class T8Creator(val context: Context) : FormCreator(context) {
  override val assetName: String = "T-8.pdf"
  override val postfix: String = "T8/"

  override fun getFields(outputDoc: PdfDocument, docField: DocForm): List<PdfFormField> {
    val value = docField as T8

    val (contractD, contractM, contractY) = getDMY2(context, value.contractData!!)
    val (dismissalDateD, dismissalDateM, dismissalDateY) = getDMY2(context, value.dismissalDate!!)

    val list = listOf(
      // org name
      DocField(
        Rectangle(56.64f, 729.84f, 388.56f, 12.24f), FieldType.ORG_NAME, value.orgName, DocField.CENTER
      ),

      // org okpo
      DocField(
        Rectangle(496.68f, 729.84f, 69.84f, 11f),
        FieldType.CODE_OKPO,
        value.codeOKPO,
        DocField.CENTER
      ),

      // doc number
      DocField(
        Rectangle(343.8f, 652.32f, 82.2f, 13.2f),
        FieldType.DOC_NUMBER,
        value.number.toString(),
        DocField.CENTER
      ),

      // doc date
      DocField(
        Rectangle(427.32f, 652.32f, 82.32f, 13.2f),
        FieldType.DOC_CREATED,
        value.contractData!!.toDocFormat(),
        DocField.CENTER
      ),

      // contract D
      DocField(
        Rectangle(290f, 588.30f, 20.04f, 12.6f), FieldType.CONTRACT_DAY, contractD, DocField.CENTER
      ),

      // contract M
      DocField(
        Rectangle(324.36f, 588.30f, 103.8f, 12.6f),
        FieldType.CONTRACT_MONTH,
        contractM,
        DocField.CENTER
      ),

      // contract Y
      DocField(
        Rectangle(443.108f, 588.30f, 18.6f, 12.6f), FieldType.CONTRACT_YEAR, contractY, DocField.CENTER
      ),

      // contract number
      DocField(
        Rectangle(496.08f, 588.30f, 65.4f, 12.6f), FieldType.CONTRACT_YEAR, value.contractNumber!!, DocField.CENTER
      ),


      // dismissal date D
      DocField(
        Rectangle(289.8f, 574.44f, 20.04f, 12.6f),
        FieldType.CONTRACT_DAY,
        dismissalDateD,
        DocField.CENTER
      ),

      // dismissal date M
      DocField(
        Rectangle(323.64f, 574.44f, 104.52f, 12.6f),
        FieldType.CONTRACT_MONTH,
        dismissalDateM,
        DocField.CENTER
      ),

      // dismissal date Y
      DocField(
        Rectangle(443.08f, 574.44f, 18.6f, 12.6f),
        FieldType.CONTRACT_YEAR,
        dismissalDateY,
        DocField.CENTER
      ),

      // fullname
      DocField(
        Rectangle(56.64f, 527.63f, 422.2f, 12.6f),
        FieldType.CONTRACT_YEAR,
        value.employer.T2Local!!.fullName,
        DocField.CENTER
      ),

      // table number
      DocField(
        Rectangle(482.52f, 527.63f, 84f, 12.6f),
        FieldType.CONTRACT_YEAR,
        value.employer.T2Local!!.tableNumberS,
        DocField.CENTER
      ),

      // division
      DocField(
        Rectangle(56.64f, 504.6f, 510.48f, 12.6f),
        FieldType.CONTRACT_YEAR,
        value.division!!.name,
        DocField.CENTER
      ),

      // position
      DocField(
        Rectangle(56.64f, 483.48f, 510.48f, 12.6f),
        FieldType.CONTRACT_YEAR,
        value.position!!.name,
        DocField.CENTER
      ),

      // foundation
      DocField(
        Rectangle(56.64f, 426.12f, 513.36f, 12.6f), FieldType.CONTRACT_YEAR, value.reason, DocField.CENTER
      ),

      // foundation doc, number, date
      DocField(
        Rectangle(155.88f, 348.84f, 411.42f, 12.6f),
        FieldType.CONTRACT_YEAR,
        value.reasonDocNumberDate,
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