package com.elka.heofficeclub.view.dialog

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.elka.heofficeclub.databinding.OrganizationPositionBinding
import com.elka.heofficeclub.service.model.Division
import com.elka.heofficeclub.service.model.OrganizationPosition

class OrganizationPositionDialog(context: Context, private val listener: Listener) : Dialog(context) {
  private lateinit var binding: OrganizationPositionBinding

  init {
    initDialog()
  }

  private fun initDialog() {
    binding = OrganizationPositionBinding.inflate(LayoutInflater.from(context))
    binding.apply {
      master = this@OrganizationPositionDialog
    }
    setContentView(binding.root)

    window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    setCancelable(false)

  }

  private var currentDivisionLevel = 0

  fun open() {
    show()
  }

  fun disagree() {
    dismiss()
  }

  fun agree() {
    val position = OrganizationPosition()
    listener.agree(position)
  }

  companion object {
    interface Listener {
      fun agree(organizationPosition: OrganizationPosition)
    }
  }
}