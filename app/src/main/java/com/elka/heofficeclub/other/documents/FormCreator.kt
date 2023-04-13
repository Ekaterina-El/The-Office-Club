package com.elka.heofficeclub.other.documents

import android.content.Context
import android.net.Uri
import android.os.Environment
import androidx.core.net.toUri
import com.elka.heofficeclub.service.model.documents.forms.DocForm
import com.itextpdf.forms.PdfAcroForm
import com.itextpdf.forms.fields.PdfFormField
import com.itextpdf.io.font.PdfEncodings
import com.itextpdf.kernel.font.PdfFont
import com.itextpdf.kernel.font.PdfFontFactory
import com.itextpdf.kernel.geom.PageSize
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfReader
import com.itextpdf.kernel.pdf.PdfString
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.layout.Document
import java.io.File
import java.io.FileOutputStream


abstract class FormCreator(private val context: Context) {
  abstract val assetName: String
  abstract val postfix: String

  protected val font: PdfFont by lazy {
    val asset = context.assets.open(fontName).readBytes()
    return@lazy PdfFontFactory.createFont(asset, PdfEncodings.IDENTITY_H, true)
  }


  companion object {
    const val fontName = "times_new_roman.ttf"
    const val fontSize = 10f
    const val fontSizeSmall = 7.88f


    private val env = Environment.DIRECTORY_DOCUMENTS
    val path = Environment.getExternalStoragePublicDirectory(env).toString() + "/TheOfficeClub/"

  }

  fun create(docField: DocForm, outFileName: String): Uri {
    val path = path + postfix
    val pathFiles = File(path)
    if (!pathFiles.exists()) pathFiles.mkdirs()

    val outFile = File(path, "$outFileName.pdf")
    val tempFileName = "temp_$outFileName.pdf"
    val tempFile = File(path, "$tempFileName.pdf")

    // Copy file from assets to temp
    FileCopier(context).copyFile(context.assets.open(assetName), FileOutputStream(tempFile))

    // Copy pages from temp to output file
    val outputDoc = PdfDocument(PdfWriter(FileOutputStream(outFile)).setSmartMode(true))
    val readerDoc = PdfDocument(PdfReader(tempFile))
    readerDoc.copyPagesTo(1, readerDoc.numberOfPages, outputDoc)

    outputDoc.catalog.lang = PdfString("ru-RU")
    outputDoc.defaultPageSize = PageSize(outputDoc.firstPage.pageSize)

    // Create form
    val form = PdfAcroForm.getAcroForm(outputDoc, true)
    getFields(outputDoc, docField).forEach {
      it.setVisibility(0)
      form.addField(it)
    }

    val doc = Document(outputDoc)
    afterCreateFields(doc, docField)

    doc.close()
    outputDoc.close()
    readerDoc.close()

    // Delete temp
    tempFile.delete()

    return outFile.toUri()
  }

  open fun afterCreateFields(outputDoc: Document, docField: DocForm) {}

  protected abstract fun getFields(outputDoc: PdfDocument, docField: DocForm): List<PdfFormField>
}