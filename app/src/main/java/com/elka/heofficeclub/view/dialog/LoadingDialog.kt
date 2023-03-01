package com.elka.heofficeclub.view.dialog

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.elka.heofficeclub.R
import com.elka.heofficeclub.databinding.ErrorDialogBinding
import com.elka.heofficeclub.databinding.LoaderDialogBinding

class LoadingDialog(context: Context): Dialog(context, R.style.AppTheme_FullScreenDialog) {
  private lateinit var binding: LoaderDialogBinding

  init {
    initDialog()
  }

  private fun initDialog() {
    binding = LoaderDialogBinding.inflate(LayoutInflater.from(context))
    setContentView(binding.root)

    window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
    setCancelable(false)
  }
}