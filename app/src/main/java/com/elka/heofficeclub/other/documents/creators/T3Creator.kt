package com.elka.heofficeclub.other.documents.creators

import android.annotation.SuppressLint
import android.content.Context
import com.elka.heofficeclub.other.FieldType
import com.elka.heofficeclub.other.documents.DocField
import com.elka.heofficeclub.other.documents.FormCreator
import com.elka.heofficeclub.other.toDocFormat
import com.elka.heofficeclub.service.model.documents.forms.DocForm
import com.elka.heofficeclub.service.model.documents.forms.T3
import com.itextpdf.forms.fields.PdfFormField
import com.itextpdf.forms.fields.PdfTextFormField
import com.itextpdf.kernel.colors.DeviceRgb
import com.itextpdf.kernel.geom.Rectangle
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.canvas.draw.SolidLine
import com.itextpdf.layout.Document
import com.itextpdf.layout.element.*
import com.itextpdf.layout.property.HorizontalAlignment
import com.itextpdf.layout.property.TextAlignment
import com.itextpdf.layout.property.VerticalAlignment
import java.util.*

class T3Creator(private val context: Context) : FormCreator(context) {
  override val assetName = "T_3.pdf"
  override val postfix: String = "T3/"

  override fun getFields(outputDoc: PdfDocument, docField: DocForm): List<PdfFormField> {
    val value = docField as T3

    val list = mutableListOf(
      // org name
      DocField(
        Rectangle(27f, 469.32f, 652.92f, 12f), FieldType.ORG_NAME, value.orgName, DocField.CENTER
      ),

      // org okpo
      DocField(
        Rectangle(730.2f, 469.32f, 84.48f, 11f),
        FieldType.CODE_OKPO,
        value.codeOKPO,
        DocField.CENTER
      ),

      // doc number
      DocField(
        Rectangle(333.84f, 410.28f, 90.96f, 10f),
        FieldType.DOC_NUMBER,
        value.number.toString(),
        DocField.CENTER
      ),

      // date of created
      DocField(
        Rectangle(425.88f, 410.28f, 91.08f, 10f),
        FieldType.DOC_NUMBER,
        value.dataCreated.toDocFormat(),
        DocField.CENTER
      ),

      // countOfEmployees
      DocField(
        Rectangle(623.74f, 370.2f, 141.83f, 12.6f),
        FieldType.DOC_NUMBER,
        value.countOfEmployees.toString(),
        DocField.CENTER
      )
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

    val table = Table(columnWidth)

    // draw header
    val p1 = createParagraphCenter("Структурное подразделение", columnWidth[0])
    table.addCell(Cell().add(p1).setVerticalAlignment(VerticalAlignment.MIDDLE))

    val p2 = createParagraphCenter(
      "Должность (специальность, профессия), разряд, класс (категория) квалификации", columnWidth[1]
    )
    table.addCell(Cell().add(p2).setVerticalAlignment(VerticalAlignment.MIDDLE))

    val p3 = createParagraphCenter("Количество штатных единиц", columnWidth[2])
    table.addCell(Cell().add(p3).setVerticalAlignment(VerticalAlignment.MIDDLE))

    val p4 = createParagraphCenter("Тарифная ставка (оклад) и пр., руб.", columnWidth[3])
    table.addCell(Cell().add(p4).setVerticalAlignment(VerticalAlignment.MIDDLE))

    val p5 = createParagraphCenter("Надбавка (общая), руб.", columnWidth[4])
    table.addCell(Cell().add(p5).setVerticalAlignment(VerticalAlignment.MIDDLE))

    val p6 = createParagraphCenter("Всего, руб.", columnWidth[5])
    table.addCell(Cell().add(p6).setVerticalAlignment(VerticalAlignment.MIDDLE))

    val p7 = createParagraphCenter("Примечание", columnWidth[6])
    table.addCell(Cell().add(p7).setVerticalAlignment(VerticalAlignment.MIDDLE))

    // draw numbers of columns
    for (i in 1..7) {
      val paragraph = createParagraphCenter(i.toString(), columnWidth[i - 1])
      table.addCell(Cell().add(paragraph))
    }

    // draw rows
    val rows = value.rows
    for (row in rows) {
      row.apply {

        listOf(
          division, position, countOfEmployees, salary, premium, totalMonth, ""
        ).forEachIndexed { idx, item ->
          val paragraph =
            if (centerColumns.contains(idx)) createParagraphCenter(item, columnWidth[idx])
            else createParagraph(item, columnWidth[idx])

          val cell = Cell().add(paragraph).setVerticalAlignment(VerticalAlignment.MIDDLE)
          table.addCell(cell)
        }
      }
    }

    table.addCell(Cell().setBorder(null))

    table.addCell(
      Cell().add(
        createParagraph(
          "Итого", columnWidth[1]
        ).setTextAlignment(TextAlignment.RIGHT)
      ).setBorder(null)
    )
    table.addCell(createParagraphCenter(value.countOfEmployees.toString(), columnWidth[2]))

    table.addCell(Cell())
    table.addCell(Cell())

    table.addCell(createParagraphCenter(value.totalOfMonth.toString(), columnWidth[5]))

    table.setHorizontalAlignment(HorizontalAlignment.CENTER)
    table.setMarginTop(200f)
    doc.add(table)

    val line = SolidLine(0.3f)
    line.color = DeviceRgb(0, 0, 0)
    val lineSeparator =
      LineSeparator(line).setHeight(12f).setHorizontalAlignment(HorizontalAlignment.CENTER)

    // region table 2
    val table2 = Table(columnWidth2)

    val signTitle = Paragraph("Руководитель кадровой службы").setFont(font).setFontSize(fontSize)
    table2.addCell(Cell(2, 1).add(signTitle).setBorder(null))

    table2.addCell(Cell().add(lineSeparator).setBorder(null))
    table2.addCell(Cell().add(lineSeparator).setBorder(null))
    table2.addCell(Cell().add(lineSeparator).setBorder(null))

    val position = createParagraphCenter("должность", columnWidth2[1]).setFontSize(fontSizeSmall)
    table2.addCell(Cell().add(position).setBorder(null))

    val sign = createParagraphCenter("личная подпись", columnWidth2[2]).setFontSize(fontSizeSmall)
    table2.addCell(Cell().add(sign).setBorder(null))

    val fullName =
      createParagraphCenter("расшифровка подписи", columnWidth2[3]).setFontSize(fontSizeSmall)
    table2.addCell(Cell().add(fullName).setBorder(null))

    table2.setMarginTop(15f)
    doc.add(table2)
    // endregion

    // region table 3
    val table3 = Table(columnWidth3)

    val signTitle2 = createParagraph("Главный бухгалтер", columnWidth3[0])
    table3.addCell(Cell(2, 1).add(signTitle2).setBorder(null))

    table3.addCell(Cell().add(lineSeparator).setBorder(null))
    table3 .addCell(Cell().add(lineSeparator).setBorder(null))

    val sign2 = createParagraphCenter("личная подпись", columnWidth3[1]).setFontSize(fontSizeSmall)
    table3.addCell(Cell().add(sign2).setBorder(null))

    val fullName2 =
      createParagraphCenter("расшифровка подписи", columnWidth3[2]).setFontSize(fontSizeSmall)
    table3.addCell(Cell().add(fullName2).setBorder(null))

    table3.setMarginTop(15f)
    doc.add(table3)
    // endregion
  }

  private fun createParagraph(text: String, width: Float) =
    Paragraph(text).setFont(font).setFontSize(fontSize).setWidth(width)

  private fun createParagraphCenter(text: String, width: Float) =
    createParagraph(text, width).setTextAlignment(TextAlignment.CENTER)

  companion object {
    val columnWidth = floatArrayOf(135f, 135f, 55f, 80f, 80f, 110f, 100f)
    val columnWidth2 = floatArrayOf(155f, 200f, 105f, 240f)
    val columnWidth3 = floatArrayOf(155f, 200f, 345f)

    val centerColumns = intArrayOf(2, 3, 4, 5)
  }
}

