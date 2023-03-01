package com.elka.heofficeclub.view.dialog

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.elka.heofficeclub.databinding.ErrorDialogBinding

class ErrorDialog(context: Context) : Dialog(context) {
  private lateinit var binding: ErrorDialogBinding

  init {
    initDialog()
  }

  private fun initDialog() {
    binding = ErrorDialogBinding.inflate(LayoutInflater.from(context))
    setContentView(binding.root)

    window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
//    window!!.setWindowAnimations(R.style.Slide)
    setCancelable(true)

    binding.buttonContinue.setOnClickListener { dismiss() }
  }

  fun open(message: String) {
    binding.textViewMessage.text = message
    show()
  }

  fun close() {
    binding.textViewMessage.text = ""
    dismiss()
  }

}