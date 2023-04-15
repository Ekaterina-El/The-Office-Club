package com.elka.heofficeclub.view.databinding

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.elka.heofficeclub.R
import com.elka.heofficeclub.other.Constants.rolesChangeAboutOrganization
import com.elka.heofficeclub.other.Constants.rolesChangeEditor
import com.elka.heofficeclub.other.ErrorApp
import com.elka.heofficeclub.other.Role
import com.elka.heofficeclub.other.documents.DateType
import com.elka.heofficeclub.other.documents.FormType
import com.elka.heofficeclub.other.toDocFormat
import com.elka.heofficeclub.service.model.documents.forms.*
import com.google.android.material.textfield.TextInputEditText
import java.util.*

@BindingAdapter("app:showError")
fun error(textView: TextView, errorApp: ErrorApp?) {
  if (errorApp == null) {
    textView.visibility = View.GONE
  } else {
    textView.visibility = View.VISIBLE
    textView.text = textView.context.getString(errorApp.messageRes)
  }
}

@BindingAdapter("app:canAddEditor")
fun canAddEditor(imageView: ImageView, role: Role?) {
  val canChange = role != null && rolesChangeEditor.contains(role)
  imageView.visibility = if (canChange) View.VISIBLE else View.GONE
}

@BindingAdapter("app:canEditAbout")
fun canEditAbout(imageView: ImageView, role: Role?) {
  val canEditAbout = role != null && rolesChangeAboutOrganization.contains(role)
  imageView.visibility = if (canEditAbout) View.VISIBLE else View.GONE
}

@BindingAdapter("app:canEditInputAbout")
fun canEditInputAbout(layout: TextInputEditText, role: Role?) {
  val canEditAbout = role != null && rolesChangeAboutOrganization.contains(role)
  layout.isEnabled = canEditAbout
}

@BindingAdapter("app:date")
fun showDate(textView: TextView, date: Date?) {
  textView.text = date?.toDocFormat() ?: "??/??/????"
}



@BindingAdapter("app:docTypeImage")
fun showDocType(imageView: ImageView, type: FormType?) {
  if (type == null) return

  val imageRes = when (type) {
    FormType.T1 -> R.drawable.t1
    FormType.T2 -> R.drawable.t2
    FormType.T3 -> R.drawable.t3
    FormType.T5 -> R.drawable.t5
    FormType.T6 -> R.drawable.t6
    FormType.T7 -> R.drawable.t7
    FormType.T8 -> R.drawable.t8
    FormType.T11 -> R.drawable.t11
  }

  imageView.setImageResource(imageRes)
}

@BindingAdapter("app:docType")
fun showDocType(textView: TextView, docForm: DocForm?) {
  if (docForm == null) return

  val ctx = textView.context
  var message = ""
  when (docForm.type) {
    FormType.T1 -> {
      val t1 = docForm as T1
      message = ctx.getString(R.string.t1_title, "")
    }
    FormType.T2 -> {
      val t2 = docForm as T2
      message = ctx.getString(R.string.t2_title, t2.fullName)
    }
    FormType.T3 -> {
      message = ctx.getString(R.string.t3_title)
    }
    FormType.T5 -> {
      val t5 = docForm as T5
      message = ctx.getString(R.string.t5_title, t5.employer?.T2Local?.fullName ?: "")
    }
    FormType.T6 -> {
      val t6 = docForm as T6
      message = ctx.getString(R.string.t6_title, t6.employer?.T2Local?.fullName ?: "")
    }
    FormType.T7 -> {
      val t7 = docForm as T7
      message = ctx.getString(R.string.t7_title, t7.yearOfGraphic.toString())
    }
    FormType.T8 -> {
      val t8 = docForm as T8
      message = ctx.getString(R.string.t8_title, t8.employer.T2Local?.fullName ?: "")
    }
    FormType.T11 -> {
      val t11 = docForm as T11
      message = ctx.getString(R.string.t11_title, t11.employer?.T2Local?.fullName ?: "")
    }
    else -> {
      message = ctx.getString(R.string.unknown_doc)
    }
  }
  textView.text = message
}

@BindingAdapter("app:countOfDaysVacation")
fun countOfDaysVacation(textView: TextView, days: Int?) {
  if (days == null) return

  textView.text = textView.context.getString(R.string.days_of_vacation, days)
}

@BindingAdapter("app:vacationTotal")
fun vacationTotal(textView: TextView, t6: T6?) {
  if (t6 == null) return

  val start = t6.vacationStart?.toDocFormat() ?: "??/??/????"
  val end = t6.vacationEnd?.toDocFormat() ?: "??/??/????"
  val days = t6.vacationADays + t6.vacationBDays
  textView.text = textView.context.getString(R.string.vacation_total, start, end, days)
}

@BindingAdapter(value = ["app:isCreation", "app:t8", "app:viewMode"], requireAll = false)
fun enableToChange(view: View, isCreation: Boolean, t8: T8?, viewMode: Boolean) {
  val access = t8 == null && isCreation
  if (viewMode) {
    view.visibility = if (access) View.VISIBLE else View.GONE
  } else {
    view.isEnabled = access
  }
}

@BindingAdapter("app:createEmployerStageTitle")
fun showStateTitle(textView: TextView, stage: Int) {
  textView.text = ""

  val textRes = when (stage) {
    1 -> R.string.general_information
    2 -> R.string.education
    3 -> R.string.family
    4 -> R.string.military
    5 -> R.string.exp
    6 -> R.string.enterToWork
    else -> return
  }

  textView.text = textView.context.getString(textRes)
}



