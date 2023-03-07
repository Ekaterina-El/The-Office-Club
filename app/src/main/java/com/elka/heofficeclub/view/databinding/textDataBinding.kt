package com.elka.heofficeclub.view.databinding

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.elka.heofficeclub.other.ErrorApp
import com.elka.heofficeclub.other.Role

@BindingAdapter("app:showError")
fun error(textView: TextView, errorApp: ErrorApp?) {
  if (errorApp == null) {
    textView.visibility = View.GONE
  } else {
    textView.visibility = View.VISIBLE
    textView.text = textView.context.getString(errorApp.messageRes)
  }
}

val rolesChangeEditor = listOf(Role.ORGANIZATION_HEAD, Role.HUMAN_RESOURCES_DEPARTMENT_HEAD)
@BindingAdapter("app:canAddEditor")
fun error(imageView: ImageView, role: Role?) {
  val canChange = role != null && rolesChangeEditor.contains(role)
  imageView.visibility = if (canChange) View.VISIBLE else View.GONE
}
