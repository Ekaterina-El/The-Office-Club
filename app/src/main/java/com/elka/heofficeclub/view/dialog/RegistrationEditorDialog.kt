package com.elka.heofficeclub.view.dialog

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import com.elka.heofficeclub.databinding.ErrorDialogBinding
import com.elka.heofficeclub.databinding.RegistrationEditorBinding
import com.elka.heofficeclub.other.Action
import com.elka.heofficeclub.other.ErrorApp
import com.elka.heofficeclub.other.Field
import com.elka.heofficeclub.other.FieldError
import com.elka.heofficeclub.service.model.Division
import com.elka.heofficeclub.service.model.User
import com.elka.heofficeclub.viewModel.RegistrationEditorViewModel

class RegistrationEditorDialog(
  context: Context,
  private val viewModelOwner: ViewModelStoreOwner,
  val owner: LifecycleOwner,
  private val listener: Listener
) : Dialog(context) {
  private lateinit var binding: RegistrationEditorBinding
  private lateinit var viewModel: RegistrationEditorViewModel

  private val fieldErrorsObserver = Observer<List<FieldError>> { fields ->
    binding.layoutEmail.error = ""
    binding.layoutFullName.error = ""

    if (fields.isEmpty()) return@Observer

    for (field in fields) {
      val view = when (field.field) {
        Field.EMAIL -> binding.layoutEmail
        Field.FULL_NAME -> binding.layoutFullName
        else -> continue
      }

      view.error = context.getString(field.errorType!!.messageRes)
    }
  }

  private val externalActionObserver = Observer<Action?> {
    if (it == Action.EDITOR_ADDED) {
      val editor = viewModel.addedEditor!!
      val password = viewModel.password!!
      viewModel.clear()
      listener.afterAdded(editor, password)
    }
  }

  init {
    initDialog()
  }

  private fun initDialog() {
    viewModel = ViewModelProvider(viewModelOwner)[RegistrationEditorViewModel::class.java]
    binding = RegistrationEditorBinding.inflate(LayoutInflater.from(context))
    binding.apply {
      master = this@RegistrationEditorDialog
      viewModel = this@RegistrationEditorDialog.viewModel
      lifecycleOwner = this@RegistrationEditorDialog.owner
    }
    setContentView(binding.root)

    window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    setCancelable(true)

    setOnDismissListener { disagree() }

  }


  fun open(organizationId: String, currentUserEmail: String, currentUserPassword: String) {
    viewModel.setCurrentUserCredentials(currentUserEmail, currentUserPassword)
    viewModel.setOrganizationId(organizationId)

    viewModel.fieldErrors.observe(owner, fieldErrorsObserver)
    viewModel.externalAction.observe(owner, externalActionObserver)
    show()
  }

  fun disagree() {
    viewModel.fieldErrors.removeObserver(fieldErrorsObserver)
    viewModel.externalAction.removeObserver(externalActionObserver)
    viewModel.clear()
    dismiss()
  }

  fun agree() {
    viewModel.tryRegistration()
  }

  companion object {
    interface Listener {
      fun afterAdded(user: User, password: String)
    }
  }
}