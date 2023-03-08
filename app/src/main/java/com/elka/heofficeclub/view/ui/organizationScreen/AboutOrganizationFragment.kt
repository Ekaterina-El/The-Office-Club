package com.elka.heofficeclub.view.ui.organizationScreen

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.elka.heofficeclub.R
import com.elka.heofficeclub.databinding.AboutOrganizationFragmentBinding
import com.elka.heofficeclub.databinding.WelcomeFragmentBinding
import com.elka.heofficeclub.other.Action
import com.elka.heofficeclub.other.Role
import com.elka.heofficeclub.other.Work
import com.elka.heofficeclub.service.model.Organization
import com.elka.heofficeclub.service.model.User
import com.elka.heofficeclub.view.dialog.ChangeHeadDialog
import com.elka.heofficeclub.view.dialog.ConfirmDialog
import com.elka.heofficeclub.view.list.users.SpinnerUsersAdapter
import com.elka.heofficeclub.view.ui.BaseFragment
import com.elka.heofficeclub.view.ui.BaseFragmentWithOrganization
import com.elka.heofficeclub.viewModel.OrganizationAboutViewModel
import com.elka.heofficeclub.viewModel.OrganizationEditorsViewModel
import com.elka.heofficeclub.viewModel.OrganizationViewModel

class AboutOrganizationFragment : BaseFragmentWithOrganization() {
  private lateinit var binding: AboutOrganizationFragmentBinding
  private lateinit var viewModel: OrganizationAboutViewModel
  private val organizationEditorsViewModel by activityViewModels<OrganizationEditorsViewModel>()

  private val works = listOf(
    Work.LOAD_PROFILE,
    Work.LOAD_ORGANIZATION,
    Work.LOGOUT,
    Work.SAVE_CHANGES,
    Work.LOAD_EDITOR,
  )
  override val workObserver = Observer<List<Work>> {
    val w1 = organizationViewModel.work.value!!
    val w2 = organizationEditorsViewModel.work.value!!
    val w = viewModel.work.value!!.toMutableList()

    w.addAll(w1)
    w.addAll(w2)

    val isLoad =
      when {
        w.isEmpty() -> false
        else -> w.map { item -> if (works.contains(item)) 1 else 0 }
          .reduce { a, b -> a + b } > 0
      }

    binding.swipeRefreshLayout.isRefreshing = isLoad
  }

  private val organizationObserver = Observer<Organization?> { organization ->
    if (organization == null) return@Observer
    viewModel.setOrganization(organization)
    organizationEditorsViewModel.setOrganization(organization)
  }

  override val externalActionObserver = Observer<Action?> { action ->
    if (action == Action.ORGANIZATION_UPDATED) {
      activity.currentFocus?.clearFocus()
      organizationViewModel.updateOrganization(viewModel.organization.value)
    }
  }

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    viewModel = ViewModelProvider(this)[OrganizationAboutViewModel::class.java]
    binding = AboutOrganizationFragmentBinding.inflate(layoutInflater, container, false)
    binding.apply {
      lifecycleOwner = viewLifecycleOwner
      viewModel = this@AboutOrganizationFragment.viewModel
      master = this@AboutOrganizationFragment
      organizationViewModel = this@AboutOrganizationFragment.organizationViewModel
    }

    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    binding.swipeRefreshLayout.setColorSchemeColors(requireContext().getColor(R.color.accent))
    binding.swipeRefreshLayout.setOnRefreshListener {
      organizationViewModel.reloadCurrentOrganization()
    }
  }

  override fun onResume() {
    super.onResume()
    organizationViewModel.organization.observe(viewLifecycleOwner, organizationObserver)
    organizationViewModel.work.observe(viewLifecycleOwner, workObserver)
    organizationEditorsViewModel.work.observe(viewLifecycleOwner, workObserver)
    viewModel.work.observe(viewLifecycleOwner, workObserver)
    viewModel.externalAction.observe(viewLifecycleOwner, externalActionObserver)

  }

  override fun onStop() {
    super.onStop()
    organizationViewModel.organization.removeObserver(organizationObserver)
    organizationViewModel.work.removeObserver(workObserver)
    organizationEditorsViewModel.work.removeObserver(workObserver)
    viewModel.work.removeObserver(workObserver)
    viewModel.externalAction.removeObserver(externalActionObserver)
  }

  private val saveOrganizationListener by lazy {
    object : ConfirmDialog.Companion.Listener {
      override fun agree() {
        viewModel.trySaveChanges()
        confirmDialog.close()
      }

      override fun disagree() {
        confirmDialog.close()
      }
    }
  }

  fun trySaveChanges() {
    val title = getString(R.string.save_organization_title)
    val message = getString(R.string.save_organization_message)
    confirmDialog.open(title, message, saveOrganizationListener)
  }

  private val changeHeadDialog by lazy {
    ChangeHeadDialog(requireContext())
  }

  private val changeOrganizationHeadListener by lazy {
    object: ChangeHeadDialog.Companion.Listener {
      override fun agree(user: User) {
        Toast.makeText(requireContext(), "change org.head to: ${user.fullName}", Toast.LENGTH_SHORT).show()
      }
    }
  }

  private val changeHRDHeadListener by lazy {
    object: ChangeHeadDialog.Companion.Listener {
      override fun agree(user: User) {
        Toast.makeText(requireContext(), "change hrdh to: ${user.fullName}", Toast.LENGTH_SHORT).show()
      }
    }
  }

  fun tryChangeOrganizationHead() {
    val spinnerEditorsAdapter = SpinnerUsersAdapter(requireContext(), organizationEditorsViewModel.editors.value!!)
    changeHeadDialog.open(Role.ORGANIZATION_HEAD, changeOrganizationHeadListener, spinnerEditorsAdapter)
  }

  fun tryChangeHRDHead() {
    val spinnerEditorsAdapter = SpinnerUsersAdapter(requireContext(), organizationEditorsViewModel.editors.value!!)
    changeHeadDialog.open(Role.HUMAN_RESOURCES_DEPARTMENT_HEAD, changeHRDHeadListener, spinnerEditorsAdapter)
  }

  fun youCantChangeHeader() {
    Toast.makeText(requireContext(), getString(R.string.youCantChangeHeader), Toast.LENGTH_SHORT).show()
  }
}
