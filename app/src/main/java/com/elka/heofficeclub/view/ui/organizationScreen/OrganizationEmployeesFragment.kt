package com.elka.heofficeclub.view.ui.organizationScreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import com.elka.heofficeclub.R
import com.elka.heofficeclub.databinding.OrganizationEmplyeesFragmentBinding
import com.elka.heofficeclub.other.Action
import com.elka.heofficeclub.other.Work
import com.elka.heofficeclub.service.model.Employer
import com.elka.heofficeclub.service.model.Organization
import com.elka.heofficeclub.service.model.OrganizationPosition
import com.elka.heofficeclub.view.dialog.ConfirmDialog
import com.elka.heofficeclub.view.dialog.OrganizationPositionDialog
import com.elka.heofficeclub.view.list.employees.EmployeesAdapter
import com.elka.heofficeclub.view.list.employees.EmployeesViewHolder
import com.elka.heofficeclub.view.list.organizationPositions.OrgPositionsAdapter
import com.elka.heofficeclub.view.list.organizationPositions.OrgPositionsViewHolder
import com.elka.heofficeclub.view.ui.BaseFragmentWithOrganization
import com.elka.heofficeclub.viewModel.CreateEmployerViewModel
import com.elka.heofficeclub.viewModel.DivisionsViewModel
import com.elka.heofficeclub.viewModel.OrganizationEmployeesViewModel

class OrganizationEmployeesFragment : BaseFragmentWithOrganization() {
  private val createEmployerViewModel by activityViewModels<CreateEmployerViewModel>()
  private val divisionsViewModel by activityViewModels<DivisionsViewModel>()


  private val organizationObserver = Observer<Organization?> {
    if (it == null) return@Observer

    organizationEmployeesViewModel.setOrganization(it)
    divisionsViewModel.setOrganization(it)
  }

  override val externalActionObserver = Observer<Action?> {
    if (it == null) return@Observer

    if (it == Action.REMOVED_POSITION) organizationViewModel.reloadCurrentOrganization()
    else super.externalActionObserver.onChanged(it)
  }

  private val works = listOf(
    Work.LOAD_POSITIONS,
    Work.REMOVE_POSITION,
    Work.LOAD_ORGANIZATION,
    Work.LOAD_DIVISIONS,
    Work.LOAD_EMPLOYERS
  )

  private val hasLoads: Boolean
    get() {
      val w1 = organizationViewModel.work.value!!
      val w2 = divisionsViewModel.work.value!!
      val w = organizationEmployeesViewModel.work.value!!.toMutableList()
      w.addAll(w1)
      w.addAll(w2)

      return when {
        w.isEmpty() -> false
        else -> w.map { item -> if (works.contains(item)) 1 else 0 }
          .reduce { a, b -> a + b } > 0
      }
    }

  override val workObserver = Observer<List<Work>> {
    binding.swiper1.isRefreshing = hasLoads
  }

  private lateinit var binding: OrganizationEmplyeesFragmentBinding
  private lateinit var organizationEmployeesViewModel: OrganizationEmployeesViewModel

  private lateinit var employeesAdapter: EmployeesAdapter
  private val employeesListener by lazy {
    object : EmployeesViewHolder.Companion.Listener {
      override fun onSelect(employer: Employer) {
        goToEmployerScreen(employer)
      }
    }
  }

  private val employeesObserver = Observer<List<Employer>> {
    employeesAdapter.setItems(it)
  }

  private fun goToEmployerScreen(employer: Employer) {
    val dir = OrganizationEmployeesFragmentDirections.actionOrganizationEmployeesFragmentToEmployerFragment(employer)
    navController.navigate(dir)
  }

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    employeesAdapter = EmployeesAdapter(employeesListener)

    organizationEmployeesViewModel =
      ViewModelProvider(this)[OrganizationEmployeesViewModel::class.java]

    binding = OrganizationEmplyeesFragmentBinding.inflate(layoutInflater, container, false)
    binding.apply {
      viewModelEmployees = this@OrganizationEmployeesFragment.organizationEmployeesViewModel
      lifecycleOwner = viewLifecycleOwner
      master = this@OrganizationEmployeesFragment
      employeesAdapter = this@OrganizationEmployeesFragment.employeesAdapter
    }

    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    val decorator = DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
    binding.employeesList.addItemDecoration(decorator)

    // Swipe Refreshers
    binding.swiper1.setOnRefreshListener { organizationViewModel.reloadCurrentOrganization() }

    val color = requireContext().getColor(R.color.accent)
    binding.swiper1.setColorSchemeColors(color)

  }

  override fun onResume() {
    super.onResume()

    organizationViewModel.organization.observe(this, organizationObserver)
    organizationViewModel.work.observe(this, workObserver)
    organizationViewModel.error.observe(this, errorObserver)

    organizationEmployeesViewModel.work.observe(this, workObserver)
    organizationEmployeesViewModel.error.observe(this, errorObserver)
    organizationEmployeesViewModel.externalAction.observe(this, externalActionObserver)
    organizationEmployeesViewModel.employeesFiltered.observe(this, employeesObserver)
  }

  override fun onStop() {
    super.onStop()

    organizationViewModel.organization.removeObserver(organizationObserver)
    organizationViewModel.work.removeObserver(workObserver)
    organizationViewModel.error.removeObserver(errorObserver)

    organizationEmployeesViewModel.work.removeObserver(workObserver)
    organizationEmployeesViewModel.error.removeObserver(errorObserver)
    organizationEmployeesViewModel.externalAction.removeObserver(externalActionObserver)
    organizationEmployeesViewModel.employeesFiltered.removeObserver(employeesObserver)
  }

  fun addEmployer() {
    if (hasLoads) return

    if (!createDocumentPermissionGranted) {
      createDocumentPermissionLauncher.launch(arrayOf(permissionRead, permissionWrite))
      return
    }

    createEmployerViewModel.setPositions(organizationEmployeesViewModel.positions.value!!)
    createEmployerViewModel.setDivisions(divisionsViewModel.divisions.value!!)
    createEmployerViewModel.setOrganization(organizationViewModel.organization.value!!)

    val direction =
      OrganizationEmployeesFragmentDirections.actionOrganizationEmployeesFragmentToCreateEmployerFragment()
    navController.navigate(direction)
  }
}