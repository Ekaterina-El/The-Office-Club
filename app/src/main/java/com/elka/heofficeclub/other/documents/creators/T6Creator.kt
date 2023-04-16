package com.elka.heofficeclub.other.documents.creators

import android.content.Context
import com.elka.heofficeclub.other.FieldType
import com.elka.heofficeclub.other.documents.DocField
import com.elka.heofficeclub.other.documents.FormCreator
import com.elka.heofficeclub.other.getDMY2
import com.elka.heofficeclub.other.toDocFormat
import com.elka.heofficeclub.service.model.documents.forms.DocForm
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

    val t2 = value.employer!!.T2Local!!

    val (startWorkD, startWorkM, startWorkY) = getDMY2(context, value.startWork!!)
    val (endWorkD, endWorkM, endWorkY) = getDMY2(context, value.endWork!!)


    val (startVacationTotalD, startVacationTotalM, startVacationTotalY) = getDMY2(
      context,
      value.vacationStart!!
    )
    val (endVacationTotalD, endVacationTotalM, endVacationTotalY) = getDMY2(
      context,
      value.vacationEnd!!
    )

    val list = mutableListOf(
      // org name
      DocField(
        Rectangle(56f, 735.84f, 383.64f, 12.24f), FieldType.ORG_NAME, value.orgName, DocField.CENTER
      ),

      // org okpo
      DocField(
        Rectangle(496.68f, 735.84f, 70f, 11f), FieldType.CODE_OKPO, value.codeOKPO, DocField.CENTER
      ),

      // doc number
      DocField(
        Rectangle(343.8f, 675.84f, 80.88f, 13.2f),
        FieldType.DOC_NUMBER,
        value.number.toString(),
        DocField.CENTER
      ),

      // date of created
      DocField(
        Rectangle(426f, 675.84f, 79.44f, 13.2f),
        FieldType.DOC_NUMBER,
        value.dataCreated.toDocFormat(),
        DocField.CENTER
      ),

      // fill name
      DocField(
        Rectangle(56.28f, 598.56f, 410.88f, 14.52f),
        FieldType.EMPLOYER_FULL_NAME,
        t2.fullName,
        DocField.CENTER
      ),

      // table number
      DocField(
        Rectangle(468.48f, 598.56f, 98.04f, 12f),
        FieldType.EMPLOYER_TABLE_NUMBER,
        t2.tableNumberS,
        DocField.CENTER
      ),

      // division
      DocField(
        Rectangle(55.2f, 576.48f, 513.36f, 12.6f),
        FieldType.DIVISION,
        value.division!!.name,
        DocField.CENTER
      ),

      // position
      DocField(
        Rectangle(55.2f, 553.08f, 513.36f, 12.6f),
        FieldType.DIVISION,
        value.position!!.name,
        DocField.CENTER
      ),

      // work interval start D
      DocField(
        Rectangle(152.39f, 529.646f, 18.86f, 11.8f), FieldType.DATE, startWorkD, DocField.CENTER
      ),

      // work interval start M
      DocField(
        Rectangle(180.72f, 530.64f, 71.76f, 11.8f), FieldType.DATE, startWorkM, DocField.CENTER
      ),

      // work interval start Y
      DocField(
        Rectangle(268.38f, 530.12f, 18.9f, 11.8f), FieldType.DATE, startWorkY, DocField.CENTER
      ),

      // work interval end D
      DocField(
        Rectangle(325.87f, 530f, 18.9f, 11.8f), FieldType.DATE, endWorkD, DocField.CENTER
      ),

      // work interval end M
      DocField(
        Rectangle(353.64f, 530.64f, 71.76f, 11.8f), FieldType.DATE, endWorkM, DocField.CENTER
      ),

      // work interval end Y
      DocField(
        Rectangle(441.32f, 530.64f, 18.86f, 11.8f), FieldType.DATE, endWorkY, DocField.CENTER
      ),

      // vacation total days
      DocField(
        Rectangle(156.84f, 324.96f, 111.6f, 19.32f),
        FieldType.DATE,
        (value.vacationADays + value.vacationBDays).toString(),
        DocField.CENTER
      ),

      // vacation total start D
      DocField(
        Rectangle(70.138f, 300.8f, 18.86f, 11.8f),
        FieldType.DATE,
        startVacationTotalD,
        DocField.CENTER
      ),

      // vacation total start M
      DocField(
        Rectangle(100f, 300.8f, 57.5f, 11.8f), FieldType.DATE, startVacationTotalM, DocField.CENTER
      ),

      // vacation total start Y
      DocField(
        Rectangle(173f, 301.8f, 18.86f, 11.8f), FieldType.DATE, startVacationTotalY, DocField.CENTER
      ),

      // vacation total end D
      DocField(
        Rectangle(234.27f, 301.39f, 18.86f, 11.8f),
        FieldType.DATE,
        endVacationTotalD,
        DocField.CENTER
      ),

      // vacation total end M
      DocField(
        Rectangle(262.92f, 300f, 57.6f, 11.8f), FieldType.DATE, endVacationTotalM, DocField.CENTER
      ),

      // vacation total end Y
      DocField(
        Rectangle(337.26f, 301.39f, 18.85f, 11.8f),
        FieldType.DATE,
        endVacationTotalY,
        DocField.CENTER
      ),
    )

    val nullDate = Date(0)
    val startA = value.startVacationA
    val endA = value.endVacationA
    if (startA != null && startA != nullDate && endA != null && endA != nullDate) {
      val (startVacationAD, startVacationAM, startVacationAY) = getDMY2(context, startA)
      val (endVacationAD, endVacationAM, endVacationAY) = getDMY2(context, endA)

      val a = listOf(

        // vacation A days
        DocField(
          Rectangle(284.52f, 480.24f, 111.48f, 19.32f),
          FieldType.DATE,
          value.vacationADays.toString(),
          DocField.CENTER
        ),

        // vacation A start D
        DocField(
          Rectangle(70.1f, 456.33f, 18.86f, 11.8f), FieldType.DATE, startVacationAD, DocField.CENTER
        ),

        // vacation A start M
        DocField(
          Rectangle(100f, 456.72f, 57.56f, 11.8f), FieldType.DATE, startVacationAM, DocField.CENTER
        ),

        // vacation A start Y
        DocField(
          Rectangle(173.09f, 455.81f, 18.85f, 11.8f),
          FieldType.DATE,
          startVacationAY,
          DocField.CENTER
        ),

        // vacation A end D
        DocField(
          Rectangle(233.96f, 455.85f, 18.85f, 11.8f), FieldType.DATE, endVacationAD, DocField.CENTER
        ),

        // vacation A end M
        DocField(
          Rectangle(262.92f, 456.72f, 57.6f, 11.8f), FieldType.DATE, endVacationAM, DocField.CENTER
        ),

        // vacation A end Y
        DocField(
          Rectangle(335.94f, 456.33f, 18.85f, 11.8f), FieldType.DATE, endVacationAY, DocField.CENTER
        ),
      )
      list.addAll(a)
    }

    val startB = value.startVacationB
    val endB = value.endVacationB
    if (startB != null && startB != nullDate && endB != null && endB != nullDate) {
      val (startVacationBD, startVacationBM, startVacationBY) = getDMY2(context, startB)
      val (endVacationBD, endVacationBM, endVacationBY) = getDMY2(context, endB)

      val a = listOf(
        // vacation B description
        DocField(
          Rectangle(67.92f, 421.2f, 500.64f, 11.8f),
          FieldType.DATE,
          value.vacationBDescription,
          DocField.CENTER
        ),

        // vacation B days
        DocField(
          Rectangle(156.84f, 381f, 111.6f, 19.32f),
          FieldType.DATE,
          value.vacationBDays.toString(),
          DocField.CENTER
        ),

        // vacation B start D
        DocField(
          Rectangle(70.25f, 356.8f, 18.86f, 11.8f), FieldType.DATE, startVacationBD, DocField.CENTER
        ),

        // vacation B start M
        DocField(
          Rectangle(100f, 357.48f, 51.76f, 11.8f), FieldType.DATE, startVacationBM, DocField.CENTER
        ),

        // vacation B start Y
        DocField(
          Rectangle(173.24f, 357.27f, 18.86f, 11.8f),
          FieldType.DATE,
          startVacationBY,
          DocField.CENTER
        ),

        // vacation B end D
        DocField(
          Rectangle(234.4f, 356.45f, 18.86f, 11.8f), FieldType.DATE, endVacationBD, DocField.CENTER
        ),

        // vacation B end M
        DocField(
          Rectangle(262.92f, 357.48f, 52.6f, 11.8f), FieldType.DATE, endVacationBM, DocField.CENTER
        ),

        // vacation B end Y
        DocField(
          Rectangle(337.37f, 356.92f, 18.85f, 11.8f), FieldType.DATE, endVacationBY, DocField.CENTER
        ),
      )

      list.addAll(a)
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

