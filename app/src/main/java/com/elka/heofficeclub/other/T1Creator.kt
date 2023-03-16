package com.elka.heofficeclub.other

import android.content.Context
import android.os.Environment
import com.elka.heofficeclub.service.model.documents.forms.DocForm
import com.elka.heofficeclub.service.model.documents.forms.T1
import com.itextpdf.forms.PdfAcroForm
import com.itextpdf.forms.fields.PdfTextFormField
import com.itextpdf.io.font.PdfEncodings
import com.itextpdf.kernel.font.PdfFontFactory
import com.itextpdf.kernel.geom.Rectangle
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfReader
import com.itextpdf.kernel.pdf.PdfWriter
import java.io.File
import java.io.FileOutputStream

class T1Creator(context: Context) : FormCreator(context) {
  override val assetName = "T_1.pdf"

  override fun fillOutForm(doc: PdfDocument, form: PdfAcroForm, docField: DocForm) {
    val field = docField as T1

    val companyNameFieldRect = Rectangle(56f, 728f, 383f, 12f)
    val nameField = PdfTextFormField.createText(
      doc, companyNameFieldRect, "company_name", field.fullName, font, 10f
    ).setFont(font).setJustification(1)
    form.addField(nameField)
  }

}

abstract class FormCreator(private val context: Context) {
  abstract val assetName: String

  protected val font by lazy {
    val d = context.assets.open(fontName).readBytes()
    return@lazy PdfFontFactory.createFont(d, PdfEncodings.UTF8)
  }

  companion object {
    const val fontName = "times_new_roman.ttf"

    private val env = Environment.DIRECTORY_DOCUMENTS
    val path = Environment.getExternalStoragePublicDirectory(env).toString()
  }

  fun create(docField: DocForm, outFileName: String) {
    val outFile = File(path, "$outFileName.pdf")
    val tempFileName = "temp_$outFileName.pdf"
    val tempFile = File(path, "$tempFileName.pdf")

    // Copy file from assets to temp
    FileCopier(context).copyFile(context.assets.open(assetName), FileOutputStream(tempFile))

    // Copy pages from temp to output file
    val outputDoc = PdfDocument(PdfWriter(FileOutputStream(outFile)).setSmartMode(true))
    val readerDoc = PdfDocument(PdfReader(tempFile))
    readerDoc.copyPagesTo(1, readerDoc.numberOfPages, outputDoc)

    // Create form
    val form = PdfAcroForm.getAcroForm(outputDoc, true)
    fillOutForm(outputDoc, form, docField)

    outputDoc.close()
    readerDoc.close()

    // Delete temp
    tempFile.delete()
  }

  protected abstract fun fillOutForm(outputDoc: PdfDocument, form: PdfAcroForm, docField: DocForm)
}