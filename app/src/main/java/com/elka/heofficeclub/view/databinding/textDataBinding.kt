package com.elka.heofficeclub.view.databinding

import android.view.View
import android.widget.ImageView
import android.widget.TextClock
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.elka.heofficeclub.R
import com.elka.heofficeclub.other.Constants.rolesChangeAboutOrganization
import com.elka.heofficeclub.other.Constants.rolesChangeEditor
import com.elka.heofficeclub.other.ErrorApp
import com.elka.heofficeclub.other.Role
import com.elka.heofficeclub.other.format
import com.elka.heofficeclub.other.toDocFormat
import com.elka.heofficeclub.service.model.documents.forms.T6
import com.elka.heofficeclub.service.model.documents.forms.T8
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

  val textRes = when(stage) {
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



