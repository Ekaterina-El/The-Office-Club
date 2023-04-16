package com.elka.heofficeclub.other.documents.creators

import android.content.Context
import com.elka.heofficeclub.other.FieldType
import com.elka.heofficeclub.other.documents.DocField
import com.elka.heofficeclub.other.documents.FormCreator
import com.elka.heofficeclub.other.getDMY2
import com.elka.heofficeclub.other.toDocFormat
import com.elka.heofficeclub.service.model.documents.forms.DocForm
import com.elka.heofficeclub.service.model.documents.forms.T5
import com.itextpdf.forms.fields.PdfFormField
import com.itextpdf.forms.fields.PdfTextFormField
import com.itextpdf.kernel.geom.Rectangle
import com.itextpdf.kernel.pdf.PdfDocument

class T5Creator(private val context: Context) : FormCreator(context) {
  override val assetName = "T-5.pdf"
  override val postfix: String = "T5/"

  override fun getFields(outputDoc: PdfDocument, docField: DocForm): List<PdfFormField> {
    val value = docField as T5

    val typeOfWork = context.getString(value.typeOfChangeWork.str)

    val salary = value.newPosition?.salary ?: 0.0
    val salaryR = salary.toInt()
    val salaryC = ((salary - salaryR) * 100).toInt()

    val premium = value.premium
    val premiumR = premium.toInt()
    val premiumC = ((premium - premiumR) * 100).toInt()


    val list = mutableListOf(
      // org name
      DocField(
        Rectangle(55.92f, 729.84f, 383.64f, 13.3f),
        FieldType.DIVISION,
        value.orgName,
        DocField.CENTER
      ),

      // okpo
      DocField(
        Rectangle(496.68f, 729.84f, 69.84f, 11.3f),
        FieldType.DIVISION,
        value.codeOKPO,
        DocField.CENTER
      ),

      // number
      DocField(
        Rectangle(343.8f, 681.84f, 90.72f, 13.3f),
        FieldType.DIVISION,
        value.number.toString(),
        DocField.CENTER
      ),

      // created date
      DocField(
        Rectangle(435.84f, 681.84f, 90.72f, 13.3f),
        FieldType.DIVISION,
        value.dataCreated.toDocFormat(),
        DocField.CENTER
      ),

      // transfer start
      DocField(
        Rectangle(461.4f, 610.56f, 105f, 12.3f),
        FieldType.DIVISION,
        value.transferStart?.toDocFormat() ?: "",
        DocField.CENTER
      ),

      // transfer end
      DocField(
        Rectangle(461.4f, 597.72f, 105f, 12.3f),
        FieldType.DIVISION,
        value.transferEnd?.toDocFormat() ?: "",
        DocField.CENTER
      ),

      // full name
      DocField(
        Rectangle(54.4f, 561.6f, 402f, 13.3f),
        FieldType.DIVISION,
        value.employer?.T2Local?.fullName ?: "",
        DocField.CENTER
      ),

      // table number
      DocField(
        Rectangle(461.28f, 561.6f, 105.24f, 12.3f),
        FieldType.DIVISION,
        value.employer?.T2Local?.tableNumberS ?: "",
        DocField.CENTER
      ),

      // type of work
      DocField(
        Rectangle(54.4f, 539.52f, 513.36f, 13.3f),
        FieldType.DIVISION,
        typeOfWork,
        DocField.CENTER
      ),

      // old division
      DocField(
        Rectangle(135.717f, 511.2486f, 431.4f, 13.3f),
        FieldType.DIVISION,
        value.oldDivision?.name ?: "",
        DocField.CENTER
      ),

      // old position
      DocField(
        Rectangle(135.717f, 489.96f, 431.4f, 13.3f),
        FieldType.DIVISION,
        value.oldPosition?.name ?: "",
        DocField.CENTER
      ),

      // reason
      DocField(
        Rectangle(54.5f, 461.76f, 513.36f, 13.3f),
        FieldType.DIVISION,
        value.transferReason,
        DocField.CENTER
      ),

      // new division
      DocField(
        Rectangle(135.717f, 427.25f, 431.4f, 13.3f),
        FieldType.DIVISION,
        value.newDivision?.name ?: "",
        DocField.CENTER
      ),

      // new position
      DocField(
        Rectangle(135.717f, 406.68f, 431.4f, 13.3f),
        FieldType.DIVISION,
        value.newPosition?.name ?: "",
        DocField.CENTER
      ),

      // salary (rub)
      DocField(
        Rectangle(262.2f, 369.48f, 219.84f, 12.3f),
        FieldType.DIVISION,
        salaryR.toString(),
        DocField.CENTER
      ),

      // salary (cents)
      DocField(
        Rectangle(510.25f, 369.48f, 28.56f, 12.3f),
        FieldType.DIVISION,
        salaryC.toString(),
        DocField.CENTER
      ),

      // premium (rub)
      DocField(
        Rectangle(261.2f, 348.36f, 219.48f, 12.3f),
        FieldType.DIVISION,
        if (premiumC != 0 || premiumR != 0) premiumR.toString() else "",
        DocField.CENTER
      ),

      // premium (cents)
      DocField(
        Rectangle(510.25f, 348.36f, 28.56f, 13.3f),
        FieldType.DIVISION,
        if (premiumC != 0 || premiumR != 0) premiumC.toString() else "",
        DocField.CENTER
      ),

      // contract number
      DocField(
        Rectangle(401.76f, 291.6f, 57.6f, 13.3f),
        FieldType.DIVISION,
        value.contractNumber,
        DocField.CENTER
      ),

      // other document
      DocField(
        Rectangle(131.76f, 279.6f, 348.84f, 12.3f),
        FieldType.DIVISION,
        value.foundation,
        DocField.CENTER
      ),
    )

    if (value.contractData != null) {
      val (contractD, contractM, contractY) = getDMY2(context, value.contractData!!)
      val items = listOf(
        // contract day
        DocField(
          Rectangle(222.52f, 291.6f, 18.77f, 13.3f),
          FieldType.DIVISION,
          contractD,
          DocField.CENTER
        ),

        // contract month
        DocField(
          Rectangle(251.52f, 291.6f, 86.04f, 13.3f),
          FieldType.DIVISION,
          contractM,
          DocField.CENTER
        ),

        // contract year
        DocField(
          Rectangle(353.24f, 291.6f, 18.7f, 13.3f),
          FieldType.DIVISION,
          contractY,
          DocField.CENTER
        ),
      )
      list.addAll(items)
    }

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

