package com.elka.heofficeclub.view.ui.organizationScreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.elka.heofficeclub.databinding.OrganizationEditorsFragmentBinding
import com.elka.heofficeclub.service.model.User
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
        Toast.makeText(requireContext(), "Added: ${user.email} | $password", Toast.LENGTH_SHORT)
          .show()
      }
    }

    RegistrationEditorDialog(
      requireContext(),
      this,
      viewLifecycleOwner,
      listener
    )
  }

  fun showAddEditorDialog() {
    registrationEditorDialog.open(organizationViewModel.organization.value!!.id, "ivanov@gmail.com", "pass1234")
  }
}