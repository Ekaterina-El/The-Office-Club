package com.elka.heofficeclub.other

import android.content.Context
import android.os.Environment
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
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.InputStream
import java.util.Calendar

class DocumentCreator(private val context: Context) {

  fun createFormT1(doc: T1) {
    val time = Calendar.getInstance().time.time
    T1Creator(context).create(doc, "t1_$time")
  }

}