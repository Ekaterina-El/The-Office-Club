package com.elka.heofficeclub.other.documents.creators

import android.content.Context
import com.elka.heofficeclub.other.*
import com.elka.heofficeclub.other.documents.DocField
import com.elka.heofficeclub.other.documents.FormCreator
import com.elka.heofficeclub.service.model.documents.forms.DocForm
import com.elka.heofficeclub.service.model.documents.forms.T1
import com.itextpdf.forms.fields.PdfFormField
import com.itextpdf.forms.fields.PdfTextFormField
import com.itextpdf.kernel.geom.Rectangle
import com.itextpdf.kernel.pdf.PdfDocument

class T1Creator(private val context: Context) : FormCreator(context) {
  override val assetName = "T_1.pdf"
  override val postfix: String = "T1/"

  override fun getFields(outputDoc: PdfDocument, docField: DocForm): List<PdfFormField> {
    val value = docField as T1

    val monthIdx = Constants.months[value.contractData!!.month]
    val month = context.getString(monthIdx)

    val year = value.contractData!!.year.toString().split("").subList(2, 4).joinToString("")

    val list = listOf(
      DocField(
        Rectangle(56f, 729f, 383f, 12f),
        FieldType.ORG_NAME,
        value.orgName,
        DocField.CENTER
      ),

      DocField(
        Rectangle(496f, 729f, 70f, 11f),
        FieldType.CODE_OKPO,
        value.codeOKPO,
        DocField.CENTER
      ),

      DocField(
        Rectangle(343.8f, 681.72f, 90.84f, 13.32f),
        FieldType.DOC_NUMBER,
        value.number.toString(),
        DocField.CENTER
      ),

      DocField(
        Rectangle(435.96f, 681.72f, 90.84f, 13.32f),
        FieldType.DOC_CREATED,
        value.dataCreated.toDocFormat(),
        DocField.CENTER
      ),

      DocField(
        Rectangle(462.84f, 616.56f, 105f, 12f),
        FieldType.T1_START_WORK,
        value.hiredFrom?.toDocFormat() ?: "",
        DocField.CENTER
      ),

      DocField(
        Rectangle(462.84f, 604.56f, 105f, 12f),
        FieldType.T1_TO_WORK,
        value.hiredBy?.toDocFormat() ?: "",
        DocField.CENTER
      ),

      DocField(
        Rectangle(57f, 555.68f, 401.7094f, 17.271f),
        FieldType.EMPLOYER_FULL_NAME,
        value.fullName,
        DocField.CENTER
      ),

      DocField(
        Rectangle(461.4f, 556.56f, 105f, 12f),
        FieldType.EMPLOYER_TABLE_NUMBER,
        value.employerTableNumber,
        DocField.CENTER
      ),

      DocField(
        Rectangle(70f, 523.456f, 496.439f, 14f),
        FieldType.DIVISION,
        value.division?.name ?: "",
        DocField.CENTER
      ),

      DocField(
        Rectangle(56.64f, 502.08f, 510.6f, 11.64f),
        FieldType.JOB_ROLE,
        value.position?.name ?: "",
        DocField.CENTER
      ),

      DocField(
        Rectangle(56.64f, 480.96f, 510.6f, 11.64f),
        FieldType.CONDITION_OF_WORK,
        value.conditionOfWork,
        DocField.CENTER
      ),

      DocField(
        Rectangle(233.88f, 392.52f, 134.88f, 16f),
        FieldType.SALARY_RUB,
        value.position?.salary?.toRub() ?: "",
        DocField.CENTER
      ),

      DocField(
        Rectangle(396.96f, 392.52f, 21.5f, 16f),
        FieldType.SALARY_CENT,
        value.position?.salary?.toCent() ?: "",
        DocField.CENTER
      ),

      DocField(
        Rectangle(233.88f, 359.88f, 134.88f, 16f),
        FieldType.PREM_RUB,
        value.premium.toRub(),
        DocField.CENTER
      ),

      DocField(
        Rectangle(396.96f, 359.88f, 21.5f, 16f),
        FieldType.PREM_CENT,
        value.premium.toCent(),
        DocField.CENTER
      ),

      DocField(
        Rectangle(183.48f, 303.12f, 329.9f, 16f),
        FieldType.TRIAL_PERIOD,
        value.trialPeriod.toString(),
        DocField.CENTER
      ),

      DocField(
        Rectangle(183.48f, 249f, 21f, 14f),
        FieldType.CONTRACT_DAY,
        value.contractData?.day.toString(),
        DocField.CENTER
      ),

      DocField(
        Rectangle(217.56f, 249f, 84f, 14f),
        FieldType.CONTRACT_MONTH,
        month,
        DocField.CENTER
      ),

      DocField(
        Rectangle(318.8f, 249f, 26f, 14f),
        FieldType.CONTRACT_YEAR,
        year,
        DocField.CENTER
      ),

      DocField(
        Rectangle(367.92f, 249f, 64.68f, 14f),
        FieldType.CONTRACT_NUMBER,
        value.contractNumber,
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

