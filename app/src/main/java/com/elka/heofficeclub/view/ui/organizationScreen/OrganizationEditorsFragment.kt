package com.elka.heofficeclub.view.ui.organizationScreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import com.elka.heofficeclub.R
import com.elka.heofficeclub.databinding.OrganizationEditorsFragmentBinding
import com.elka.heofficeclub.other.UserStatus
import com.elka.heofficeclub.other.Work
import com.elka.heofficeclub.service.model.Organization
import com.elka.heofficeclub.service.model.User
import com.elka.heofficeclub.service.model.sortByNameAndStatus
import com.elka.heofficeclub.view.dialog.ConfirmDialog
import com.elka.heofficeclub.view.dialog.InformDialog
import com.elka.heofficeclub.view.dialog.RegistrationEditorDialog
import com.elka.heofficeclub.view.list.editors.EditorsAdapter
import com.elka.heofficeclub.view.list.editors.EditorsViewHolder
import com.elka.heofficeclub.view.ui.BaseFragmentWithOrganization
import com.elka.heofficeclub.viewModel.OrganizationEditorsViewModel

class OrganizationEditorsFragment : BaseFragmentWithOrganization() {
  private lateinit var binding: OrganizationEditorsFragmentBinding
  private val viewModel by activityViewModels<OrganizationEditorsViewModel>()
  private lateinit var editorsAdapter: EditorsAdapter

  private val editorsAdapterListener by lazy {
    object: EditorsViewHolder.Companion.Listener {
      override fun onChangeBlockStatus(editor: User) {
        confirmBlockingDialog(editor)
      }
    }
  }

  private val profileObserver = Observer<User?> {
    if (it == null) return@Observer
    editorsAdapter.updateViewerRole(it.role)
  }

  private val filteredEditorsObserver = Observer<List<User>> {
    editorsAdapter.setItems(it.sortByNameAndStatus())
  }

  private val works = listOf(
    Work.REGISTRATION_EDITOR,
    Work.LOAD_EDITOR,
    Work.CHANGE_BLOCKING_STATUS,
    Work.LOAD_ORGANIZATION
  )

  private val organizationObserver = Observer<Organization?> {
    if (it == null) return@Observer
    viewModel.setOrganization(it)
  }

  override val workObserver = Observer<List<Work>> {
    val w1 = organizationViewModel.work.value!!
    val w = viewModel.work.value!!.toMutableList()
    w.addAll(w1)

    val isLoad =
      when {
        w.isEmpty() -> false
        else -> w.map { item -> if (works.contains(item)) 1 else 0 }
          .reduce { a, b -> a + b } > 0
      }

    binding.swipeRefreshLayout.isRefreshing = isLoad
    binding.swipeRefreshLayout2.isRefreshing = isLoad
  }

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    editorsAdapter = EditorsAdapter(editorsAdapterListener)

    binding = OrganizationEditorsFragmentBinding.inflate(layoutInflater, container, false)
    binding.apply {
      master = this@OrganizationEditorsFragment
      lifecycleOwner = viewLifecycleOwner
      viewModel = this@OrganizationEditorsFragment.viewModel
      adapter = this@OrganizationEditorsFragment.editorsAdapter
      organizationViewModel = this@OrganizationEditorsFragment.organizationViewModel
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
    val credentials = getCredentials()
    if (credentials != null) {
      registrationEditorDialog.open(organizationViewModel.organization.value!!.id, credentials.email, credentials.password)
    }
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    binding.swipeRefreshLayout.setColorSchemeColors(activity.getColor(R.color.accent))
    binding.swipeRefreshLayout2.setColorSchemeColors(activity.getColor(R.color.accent))

    binding.swipeRefreshLayout.setOnRefreshListener { organizationViewModel.reloadCurrentOrganization() }
    binding.swipeRefreshLayout2.setOnRefreshListener { organizationViewModel.reloadCurrentOrganization() }

    val decorator = DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
    binding.recyclerView.addItemDecoration(decorator)

    binding.layoutNoFound.message.text = getString(R.string.editor_no_found)
  }

  override fun onResume() {
    super.onResume()
    viewModel.filteredEditors.observe(viewLifecycleOwner, filteredEditorsObserver)
    viewModel.work.observe(viewLifecycleOwner, workObserver)
    viewModel.error.observe(viewLifecycleOwner, errorObserver)

    organizationViewModel.profile.observe(viewLifecycleOwner, profileObserver)
    organizationViewModel.work.observe(viewLifecycleOwner, workObserver)
    organizationViewModel.organization.observe(viewLifecycleOwner, organizationObserver)
    organizationViewModel.error.observe(viewLifecycleOwner, errorObserver)
  }

  override fun onStop() {
    super.onStop()
    viewModel.filteredEditors.removeObserver(filteredEditorsObserver)
    viewModel.work.removeObserver(workObserver)
    viewModel.error.removeObserver(errorObserver)

    organizationViewModel.profile.removeObserver(profileObserver)
    organizationViewModel.work.removeObserver(workObserver)
    organizationViewModel.organization.removeObserver(organizationObserver)
    organizationViewModel.error.removeObserver(errorObserver)
  }

  private fun confirmBlockingDialog(editor: User) {

    val fullName = editor.fullName
    val title = if (editor.status == UserStatus.UNBLOCKED) getString(R.string.blocking_editor_title) else getString(R.string.unblocking_editor_title)
    val message = if (editor.status == UserStatus.UNBLOCKED) getString(R.string.blocking_editor_message, fullName) else getString(R.string.unblocking_editor_message, fullName)

    val listener = object : ConfirmDialog.Companion.Listener {
      override fun agree() {
        viewModel.changeBlockingStatusOfEditor(editor)
        confirmDialog.close()
      }

      override fun disagree() {
        confirmDialog.close()
      }
    }

    confirmDialog.open(title, message, listener)
  }
}