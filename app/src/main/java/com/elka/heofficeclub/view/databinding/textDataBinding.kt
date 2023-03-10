package com.elka.heofficeclub.view.databinding

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.elka.heofficeclub.other.Constants.rolesChangeAboutOrganization
import com.elka.heofficeclub.other.Constants.rolesChangeEditor
import com.elka.heofficeclub.other.ErrorApp
import com.elka.heofficeclub.other.Role
import com.google.android.material.textfield.TextInputEditText

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

