package com.elka.heofficeclub.other

import android.content.Context
import android.util.Log
import java.io.*

class FileCopier(val context: Context) {
  fun copyFile(inputPath: String, outputPath: String) {

    try {
      val dir = File(outputPath)
      if (!dir.exists()) dir.mkdirs()

      val inp = FileInputStream(inputPath)
      val out = FileOutputStream(outputPath)

      copyByByte(inp, out)
    } catch (fnfe1: FileNotFoundException) {
      Log.e("tag", fnfe1.message ?: "error")
    } catch (e: Exception) {
      Log.e("tag", e.message ?: "error")
    }
  }

  fun copyFile(input: InputStream, output: FileOutputStream) {
    copyByByte(input, output)
  }

  private fun copyByByte(inp: InputStream, out: FileOutputStream) {
    val buffer = ByteArray(1024)
    var read: Int
    while (inp.read(buffer).also { read = it } != -1) out.write(buffer, 0, read)

    inp.close()
    out.flush()
    out.close()
  }
}