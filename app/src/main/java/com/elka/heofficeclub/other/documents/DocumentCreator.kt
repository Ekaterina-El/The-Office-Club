package com.elka.heofficeclub.other.documents

import android.content.Context
import android.net.Uri
import com.elka.heofficeclub.other.documents.creators.*
import com.elka.heofficeclub.service.model.Division
import com.elka.heofficeclub.service.model.OrganizationPosition
import com.elka.heofficeclub.service.model.documents.forms.*
import java.util.Calendar

class DocumentCreator(private val context: Context) {
  private val t1 by lazy { T1Creator(context) }
  private val t5 by lazy { T5Creator(context) }
  private val t6 by lazy { T6Creator(context) }
  private val t8 by lazy { T8Creator(context) }
  private val t11 by lazy { T11Creator(context) }

  private fun getNewName(pref: String): String {
    val time = Calendar.getInstance().time.time
    return "${pref}_$time"
  }

  fun createFormT1(doc: T1): Uri {
    return t1.create(doc, getNewName("t1"))
  }

  fun createFormT5(doc: T5): Uri {
    return t5.create(doc, getNewName("t5"))
  }

  fun createFormT6(doc: T6): Uri {
    return t6.create(doc, getNewName("t6"))
  }

  fun createFormT11(doc: T11): Uri {
    return t11.create(doc, getNewName("t11"))
  }

  fun createFormT8(doc: T8): Uri {
    return t8.create(doc, getNewName("t8"))
  }

}