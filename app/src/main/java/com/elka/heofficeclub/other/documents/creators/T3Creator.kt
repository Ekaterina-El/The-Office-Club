package com.elka.heofficeclub.other.documents.creators

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import androidx.core.graphics.drawable.toBitmap
import com.elka.heofficeclub.R
import com.elka.heofficeclub.other.FieldType
import com.elka.heofficeclub.other.documents.DocField
import com.elka.heofficeclub.other.documents.FormCreator
import com.elka.heofficeclub.other.getYearOfDate
import com.elka.heofficeclub.other.toDocFormat
import com.elka.heofficeclub.service.model.documents.forms.DocForm
import com.elka.heofficeclub.service.model.documents.forms.T3
import com.elka.heofficeclub.service.model.documents.forms.T7
import com.itextpdf.forms.fields.PdfFormField
import com.itextpdf.forms.fields.PdfTextFormField
import com.itextpdf.io.image.ImageDataFactory
import com.itextpdf.kernel.colors.DeviceRgb
import com.itextpdf.kernel.geom.Rectangle
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.canvas.draw.SolidLine
import com.itextpdf.layout.Document
import com.itextpdf.layout.element.*
import com.itextpdf.layout.property.HorizontalAlignment
import com.itextpdf.layout.property.TextAlignment
import com.itextpdf.layout.property.VerticalAlignment
import java.io.ByteArrayOutputStream
import java.util.*

class T3Creator(private val context: Context) : FormCreator(context) {
  override val assetName = "T_3.pdf"
  override val postfix: String = "T3/"

  override fun getFields(outputDoc: PdfDocument, docField: DocForm): List<PdfFormField> {
    val value = docField as T3

    val list = mutableListOf(
      // org name
      DocField(
        Rectangle(21.6f, 469.08f, 653.04f, 12f), FieldType.ORG_NAME, value.orgName, DocField.CENTER
      ),

      // org okpo
      DocField(
        Rectangle(737.76f, 469.08f, 76.92f, 11f),
        FieldType.CODE_OKPO,
        value.codeOKPO,
        DocField.CENTER
      ),

      // doc number
      DocField(
        Rectangle(360.72f, 388.32f, 83.76f, 13.2f),
        FieldType.DOC_NUMBER,
        value.number.toString(),
        DocField.CENTER
      ),

      // date of created
      DocField(
        Rectangle(445.92f, 388.32f, 82.64f, 13.2f),
        FieldType.DOC_NUMBER,
        value.dataCreated.toDocFormat(),
        DocField.CENTER
      )

      // countOfEmployees
    )

    return list.map {
      return@map PdfTextFormField.createText(
        outputDoc, it.wrapper, it.type.fieldName, it.v, font, fontSize
      ).setJustification(1)
    }
  }

  @SuppressLint("UseCompatLoadingForDrawables")
  override fun afterCreateFields(doc: Document, docField: DocForm) {
    super.afterCreateFields(doc, docField)

    val value = docField as T3
    /*
    val table = Table(columnWidth)

    // draw header
    val p1 = createParagraphCentered("Структурное подразделение", 0)
    table.addCell(Cell(3, 1).add(p1).setVerticalAlignment(VerticalAlignment.MIDDLE))

    val p2 =
      createParagraphCentered("Должность (специальность, профессия) по штатному расписанию", 1)
    table.addCell(Cell(3, 1).add(p2).setVerticalAlignment(VerticalAlignment.MIDDLE))

    val p3 = createParagraphCentered("ФИО", 2)
    table.addCell(Cell(3, 1).add(p3).setVerticalAlignment(VerticalAlignment.MIDDLE))

    val p4 = createParagraphCentered("Табельный номер", 3)
    table.addCell(Cell(3, 1).add(p4).setVerticalAlignment(VerticalAlignment.MIDDLE))

    val p5 = createParagraphCentered(
      "ОТПУСК", 4
    ).setWidth(columnWidth[4] + columnWidth[5] + columnWidth[6] + columnWidth[7] + columnWidth[8])
    table.addCell(Cell(1, 5).add(p5).setVerticalAlignment(VerticalAlignment.MIDDLE))

    val p6 = createParagraphCentered("Примечание", 5)
    table.addCell(Cell(3, 1).add(p6).setVerticalAlignment(VerticalAlignment.MIDDLE))

    val p7 = createParagraphCentered("количество календарных дней", 4)
    table.addCell(Cell(2, 1).add(p7).setVerticalAlignment(VerticalAlignment.MIDDLE))

    val p8 = createParagraphCentered("дата", 6).setWidth(columnWidth[5] + columnWidth[6])
    table.addCell(Cell(1, 2).add(p8).setVerticalAlignment(VerticalAlignment.MIDDLE))

    val p9 =
      createParagraphCentered("перенесение отпуска", 8).setWidth(columnWidth[7] + columnWidth[8])
    table.addCell(Cell(1, 2).add(p9).setVerticalAlignment(VerticalAlignment.MIDDLE))

    val p10 = createParagraphCentered("запланированая", 6)
    table.addCell(Cell().add(p10).setVerticalAlignment(VerticalAlignment.MIDDLE))

    val p11 = createParagraphCentered("фактическая", 7)
    table.addCell(Cell().add(p11).setVerticalAlignment(VerticalAlignment.MIDDLE))

    val p12 = createParagraphCentered("основание (документ)", 8)
    table.addCell(Cell().add(p12).setVerticalAlignment(VerticalAlignment.MIDDLE))

    val p13 = createParagraphCentered("дата предполагаемого отпуска", 9)
    table.addCell(Cell().add(p13).setVerticalAlignment(VerticalAlignment.MIDDLE))

    // draw numbers of columns
    for (i in 1..10) {
      val paragraph = createParagraphCentered(i.toString(), i - 1)
      table.addCell(Cell().add(paragraph))
    }

    // draw rows
    val rows = value.rows
    for (row in rows) {
      row.apply {
        val countOfDays = vacations.joinToString("\n") { it.countOfDays }
        val startDates = vacations.joinToString("\n") { it.dateOfStart }

        listOf(
          division, position, fullName, tableNumber, countOfDays, startDates, "", "", "", ""
        ).forEachIndexed { idx, item ->
          val paragraph = if (centerColumns.contains(idx)) createParagraphCentered(
            item, idx
          ) else createParagraph(item, idx)

          val cell = Cell().add(paragraph).setVerticalAlignment(VerticalAlignment.MIDDLE)
          table.addCell(cell)
        }
      }
    }

    table.setHorizontalAlignment(HorizontalAlignment.CENTER)
    table.setMarginTop(200f)
    doc.add(table)


    val table2 = Table(columnWidth2)

    val signTitle = Paragraph("Руководитель кадровой службы").setFont(font).setFontSize(fontSize)
    table2.addCell(Cell(2, 1).add(signTitle).setBorder(null))

    val line = SolidLine(0.3f)
    line.color = DeviceRgb(0, 0, 0)
    val lineSeparator =
      LineSeparator(line).setHeight(12f).setHorizontalAlignment(HorizontalAlignment.CENTER)

    table2.addCell(Cell().add(lineSeparator).setBorder(null))
    table2.addCell(Cell().add(lineSeparator).setBorder(null))
    table2.addCell(Cell().add(lineSeparator).setBorder(null))

    val position =
      Paragraph("(должность)").setFont(font).setFontSize(fontSizeSmall).setWidth(columnWidth2[1])
        .setTextAlignment(TextAlignment.CENTER)
    table2.addCell(Cell().add(position).setBorder(null))


    val sign = Paragraph("(личная подпись)").setFont(font).setFontSize(fontSizeSmall)
      .setWidth(columnWidth2[2]).setTextAlignment(TextAlignment.CENTER)
    table2.addCell(Cell().add(sign).setBorder(null))


    val fullName = Paragraph("(расшифровка подписи)").setFont(font).setFontSize(fontSizeSmall)
      .setWidth(columnWidth2[3]).setTextAlignment(TextAlignment.CENTER)
    table2.addCell(Cell().add(fullName).setBorder(null))

    table2.setMarginTop(20f)
    doc.add(table2)*/
  }

  private fun createParagraph(text: String, idx: Int) =
    Paragraph(text).setFont(font).setFontSize(fontSize).setWidth(columnWidth[idx])

  private fun createParagraphCentered(text: String, idx: Int) =
    createParagraph(text, idx).setTextAlignment(TextAlignment.CENTER)

  companion object {
    val columnWidth = floatArrayOf(86f, 86f, 90f, 50.52f, 60f, 68f, 68f, 68f, 68f, 70f)
    val columnWidth2 = floatArrayOf(155f, 200f, 105f, 240f)
    val centerColumns = intArrayOf(3, 4, 5, 6, 7, 8, 9)
  }
}

