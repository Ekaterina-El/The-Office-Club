package com.elka.heofficeclub.view.dialog

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import com.elka.heofficeclub.databinding.OrganizationPositionBinding
import com.elka.heofficeclub.service.model.Division
import com.elka.heofficeclub.service.model.OrganizationPosition
import com.elka.heofficeclub.viewModel.OrganizationPositionViewModel
import com.elka.heofficeclub.viewModel.RegistrationEditorViewModel

class OrganizationPositionDialog(
  context: Context,
  private val viewModelOwner: ViewModelStoreOwner,
  val owner: LifecycleOwner,
  private val listener: Listener
) : Dialog(context) {

  private lateinit var binding: OrganizationPositionBinding
  private lateinit var viewModel: OrganizationPositionViewModel

  init {
    initDialog()
  }

  private fun initDialog() {
    viewModel = ViewModelProvider(viewModelOwner)[OrganizationPositionViewModel::class.java]
    binding = OrganizationPositionBinding.inflate(LayoutInflater.from(context))
    binding.apply {
      master = this@OrganizationPositionDialog
      viewModel = this@OrganizationPositionDialog.viewModel
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
    viewModel.clear()
  }

  fun agree() {
    val position = viewModel.newOrganizationPosition
    listener.agree(position)
    disagree()
  }

  companion object {
    interface Listener {
      fun agree(organizationPosition: OrganizationPosition)
    }
  }
}