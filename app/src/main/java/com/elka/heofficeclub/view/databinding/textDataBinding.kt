package com.elka.heofficeclub.view.databinding

import android.view.View
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.elka.heofficeclub.other.ErrorApp

@BindingAdapter("app:showError")
fun error(textView: TextView, errorApp: ErrorApp?) {
  if (errorApp == null) {
    textView.visibility = View.GONE
  } else {
    textView.visibility = View.VISIBLE
    textView.text = textView.context.getString(errorApp.messageRes)
  }
}