package com.elka.heofficeclub.other.documents

import android.content.Context
import android.net.Uri
import com.elka.heofficeclub.other.documents.creators.T1Creator
import com.elka.heofficeclub.other.documents.creators.T6Creator
import com.elka.heofficeclub.service.model.Division
import com.elka.heofficeclub.service.model.OrganizationPosition
import com.elka.heofficeclub.service.model.documents.forms.T1
import com.elka.heofficeclub.service.model.documents.forms.T6
import java.util.Calendar

class DocumentCreator(private val context: Context) {
  private val t1 by lazy { T1Creator(context) }
  private val t6 by lazy { T6Creator(context) }

  private fun getNewName(pref: String): String {
    val time = Calendar.getInstance().time.time
    return "${pref}_$time"
  }

  fun createFormT1(doc: T1): Uri {
    return t1.create(doc, getNewName("t1"))
  }

  fun createFormT6(doc: T6): Uri {
    return t6.create(doc, getNewName("t6"))
  }

}