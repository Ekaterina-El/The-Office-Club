package com.elka.heofficeclub.view.dialog

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import com.elka.heofficeclub.databinding.OrganizationPositionBinding
import com.elka.heofficeclub.other.ErrorApp
import com.elka.heofficeclub.other.Field
import com.elka.heofficeclub.other.FieldError
import com.elka.heofficeclub.service.model.OrganizationPosition
import com.elka.heofficeclub.viewModel.OrganizationPositionViewModel

class OrganizationPositionDialog(
  context: Context,
  private val viewModelOwner: ViewModelStoreOwner,
  private val owner: LifecycleOwner,
  private val organizationId: String,
  private val listener: Listener
) : Dialog(context) {

  private val errorObserver = Observer<ErrorApp?> { error ->
    if (error == null) return@Observer

    val message = context.getString(error.messageRes)
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
  }

  private val fieldErrorsObserver = Observer<List<FieldError>> { errors ->
    showErrors(errors)
  }

  private val positionObserver = Observer<OrganizationPosition?> { position ->
    if (position == null) return@Observer

    listener.agree(position)
    disagree()
  }

  private lateinit var binding: OrganizationPositionBinding
  private lateinit var viewModel: OrganizationPositionViewModel

  init {
    initDialog()
  }

  private fun initDialog() {
    viewModel = ViewModelProvider(viewModelOwner)[OrganizationPositionViewModel::class.java]
    viewModel.setOrganizationId(organizationId)

    binding = OrganizationPositionBinding.inflate(LayoutInflater.from(context))
    binding.apply {
      master = this@OrganizationPositionDialog
      viewModel = this@OrganizationPositionDialog.viewModel
      lifecycleOwner = this@OrganizationPositionDialog.owner
    }
    setContentView(binding.root)

    window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    setCancelable(false)

  }

  fun open() {
    viewModel.addedPosition.observe(owner, positionObserver)
    viewModel.fieldErrors.observe(owner, fieldErrorsObserver)
    viewModel.error.observe(owner, errorObserver)

    show()
  }

  fun disagree() {
    viewModel.clear()
    viewModel.addedPosition.removeObserver(positionObserver)
    viewModel.fieldErrors.removeObserver(fieldErrorsObserver)
    viewModel.error.removeObserver(errorObserver)

    dismiss()
  }

  fun agree() {
    viewModel.tryCreatePosition()
  }

  private fun showErrors(errors: List<FieldError>) {
    binding.layoutShortName.error = ""
    binding.layoutSalary.error = ""

    if (errors.isEmpty()) return

    for (error in errors) {
      val layout = when (error.field) {
        Field.SALARY -> binding.layoutSalary
        Field.NAME -> binding.layoutShortName
        else -> continue
      }
      layout.error = context.getString(error.errorType!!.messageRes)
    }
  }

  companion object {
    interface Listener {
      fun agree(organizationPosition: OrganizationPosition)
    }
  }
}