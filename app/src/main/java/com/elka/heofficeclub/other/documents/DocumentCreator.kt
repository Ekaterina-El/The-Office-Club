package com.elka.heofficeclub.other.documents

import android.content.Context
import android.net.Uri
import com.elka.heofficeclub.other.documents.creators.T1Creator
import com.elka.heofficeclub.service.model.Division
import com.elka.heofficeclub.service.model.OrganizationPosition
import com.elka.heofficeclub.service.model.documents.forms.T1
import java.util.Calendar

class DocumentCreator(private val context: Context) {
  private val t1 by lazy { T1Creator(context) }

  private fun getNewName(pref: String): String {
    val time = Calendar.getInstance().time.time
    return "${pref}_$time"
  }

  fun createFormT1(doc: T1): Uri {
    val d = T1(
      orgName = "Microsoft",
      codeOKPO = "531930",
      number = "542918",
      dataCreated = Calendar.getInstance().time,
      hiredFrom = Calendar.getInstance().time,
      hiredBy = Calendar.getInstance().time,
      employerTableNumber = "523",
      fullName = "Ivanov Ivan Ivanovich",
      division = Division(name = "Division №43"),
      position = OrganizationPosition(name = "Chief Engineer", salary = 85000.42),
      conditionOfWork = "Transfer from Division 6",
      premium = 10000.94,
      contractNumber = "154",
      contractData = Calendar.getInstance().time,
    )
    return t1.create(d, getNewName("t1"))
  }

}