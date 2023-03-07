package com.elka.heofficeclub.view.ui.organizationScreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.elka.heofficeclub.R
import com.elka.heofficeclub.databinding.OrganizationEditorsFragmentBinding
import com.elka.heofficeclub.service.model.User
import com.elka.heofficeclub.view.dialog.InformDialog
import com.elka.heofficeclub.view.dialog.RegistrationEditorDialog
import com.elka.heofficeclub.view.ui.BaseFragmentWithOrganization

class OrganizationEditorsFragment : BaseFragmentWithOrganization() {
  private lateinit var binding: OrganizationEditorsFragmentBinding

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    binding = OrganizationEditorsFragmentBinding.inflate(layoutInflater, container, false)
    binding.apply {
      master = this@OrganizationEditorsFragment
      lifecycleOwner = viewLifecycleOwner
    }

    return binding.root
  }


  private val registrationEditorDialog: RegistrationEditorDialog by lazy {
    val listener = object : RegistrationEditorDialog.Companion.Listener {
      override fun afterAdded(user: User, password: String) {
        registrationEditorDialog.disagree()
        showEditorCredentials(user, password)

      }
    }

    RegistrationEditorDialog(
      requireContext(),
      this,
      viewLifecycleOwner,
      listener
    )
  }

  private val editorCredentialsDialogListener by lazy {
    object: InformDialog.Companion.Listener {
      override fun copyMessage(message: String) {
        copyToClipboard(message)
      }
    }
  }

  fun showEditorCredentials(user: User, password: String) {
    val title = getString(R.string.editor_added)
    val message = getString(R.string.editor_auth_data, user.email, password)
    val hint = getString(R.string.editor_added_hint)

    activity.informDialog.open(title, message, hint, editorCredentialsDialogListener, "${user.email} | $password")
  }

  fun showAddEditorDialog() {
    registrationEditorDialog.open(organizationViewModel.organization.value!!.id, "ivanov@gmail.com", "pass1234")
  }
}