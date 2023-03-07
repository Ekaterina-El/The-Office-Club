package com.elka.heofficeclub.view.dialog

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.elka.heofficeclub.databinding.CreateDivisionDialogBinding
import com.elka.heofficeclub.service.model.Division

class CreateDivisionDialog(context: Context, private val listener: Listener) : Dialog(context) {
  private lateinit var binding: CreateDivisionDialogBinding

  init {
    initDialog()
  }

  private fun initDialog() {
    binding = CreateDivisionDialogBinding.inflate(LayoutInflater.from(context))
    binding.apply {
      master = this@CreateDivisionDialog
    }
    setContentView(binding.root)

    window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    setCancelable(true)

  }

  private var currentDivisionLevel = 0

  fun open(currentDivisionLevel: Int) {
    this.currentDivisionLevel = currentDivisionLevel
    show()
  }

  fun close() {
    binding.teName.setText("")
    dismiss()
  }

  fun disagree() {
    listener.disagree()
  }

  fun agree() {
    val divisionName = binding.teName.text.toString()
    val division = Division(name = divisionName)

    listener.agree(division)
  }

  companion object {
    interface Listener {
      fun agree(division: Division)
      fun disagree()
    }
  }
}