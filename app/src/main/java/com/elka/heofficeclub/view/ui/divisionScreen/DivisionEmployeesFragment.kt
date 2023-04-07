package com.elka.heofficeclub.view.ui.divisionScreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import com.elka.heofficeclub.R
import com.elka.heofficeclub.databinding.DivisionEmployeesFragmentBinding
import com.elka.heofficeclub.other.Work
import com.elka.heofficeclub.service.model.Employer
import com.elka.heofficeclub.view.list.employees.EmployeesAdapter
import com.elka.heofficeclub.view.list.employees.EmployeesViewHolder
import com.elka.heofficeclub.view.ui.BaseFragmentWithOrganization
import com.elka.heofficeclub.view.ui.organizationScreen.OrganizationEmployeesFragmentDirections
import com.elka.heofficeclub.viewModel.DivisionViewModel
import com.elka.heofficeclub.viewModel.DivisionsViewModel
import com.elka.heofficeclub.viewModel.EmployerViewModel

class DivisionEmployeesFragment : BaseFragmentWithOrganization() {
  private lateinit var binding: DivisionEmployeesFragmentBinding
  private val divisionsViewModel by activityViewModels<DivisionsViewModel>()
  private val divisionViewModel by activityViewModels<DivisionViewModel>()
  private val viewModel by activityViewModels<EmployerViewModel>()

  private val works = listOf(
    Work.LOAD_EMPLOYERS,
    Work.LOAD_DIVISION,
    Work.LOAD_POSITIONS,
    )

  private val hasLoads: Boolean
    get() {
      val w = organizationViewModel.work.value!!.toMutableList()
      val w1 = divisionViewModel.work.value!!
      w.addAll(w1)

      return when {
        w.isEmpty() -> false
        else -> w.map { item -> if (works.contains(item)) 1 else 0 }.reduce { a, b -> a + b } > 0
      }
    }

  override val workObserver = Observer<List<Work>> {
    binding.swiper1.isRefreshing = hasLoads
  }

  private val employeesAdapter by lazy { EmployeesAdapter(employeesListener) }
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

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
  ): View {
    binding = DivisionEmployeesFragmentBinding.inflate(layoutInflater, container, false)
    binding.apply {
      lifecycleOwner = viewLifecycleOwner
      viewModel = this@DivisionEmployeesFragment.divisionViewModel
      master = this@DivisionEmployeesFragment
      employeesAdapter = this@DivisionEmployeesFragment.employeesAdapter
    }

    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    val decorator = DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
    binding.employeesList.addItemDecoration(decorator)

    binding.swiper1.setOnRefreshListener { divisionViewModel.reloadCurrentDivision() }

    val color = requireContext().getColor(R.color.accent)
    binding.swiper1.setColorSchemeColors(color)
  }

  override fun onResume() {
    super.onResume()
    organizationViewModel.setBottomMenuStatus(false)
    divisionViewModel.setBottomMenuStatus(true)

    organizationViewModel.work.observe(this, workObserver)
    divisionViewModel.work.observe(this, workObserver)
    divisionViewModel.employeesFiltered.observe(this, employeesObserver)
  }

  override fun onStop() {
    super.onStop()
    organizationViewModel.work.removeObserver(workObserver)
    divisionViewModel.work.removeObserver(workObserver)
    divisionViewModel.employeesFiltered.removeObserver(employeesObserver)
  }

  private fun goToEmployerScreen(employer: Employer) {
    if (!createDocumentPermissionGranted) {
      createDocumentPermissionLauncher.launch(arrayOf(permissionRead, permissionWrite))
      return
    }

    divisionViewModel.setBottomMenuStatus(false)

    viewModel.setPositions(divisionViewModel.positions.value!!)
    viewModel.setDivisions(divisionsViewModel.divisions.value!!)
    viewModel.setOrganization(organizationViewModel.organization.value!!)

    val dir = DivisionEmployeesFragmentDirections.actionDivisionEmployeesFragmentToEmployerFragment(employer)
    navController.navigate(dir)
  }


  fun addEmployer() {
    if (hasLoads) return

    if (!createDocumentPermissionGranted) {
      createDocumentPermissionLauncher.launch(arrayOf(permissionRead, permissionWrite))
      return
    }
    divisionViewModel.setBottomMenuStatus(false)

    viewModel.setPositions(divisionViewModel.positions.value!!)
    viewModel.setDivisions(divisionsViewModel.divisions.value!!)
    viewModel.setOrganization(organizationViewModel.organization.value!!)

    val direction =
      DivisionEmployeesFragmentDirections.actionDivisionEmployeesFragmentToCreateEmployerFragment()
    navController.navigate(direction)
  }
}