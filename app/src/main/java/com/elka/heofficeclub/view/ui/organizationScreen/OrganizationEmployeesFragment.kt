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
import com.elka.heofficeclub.other.documents.DocumentCreator
import com.elka.heofficeclub.other.documents.FormType
import com.elka.heofficeclub.service.model.Employer
import com.elka.heofficeclub.service.model.Organization
import com.elka.heofficeclub.view.dialog.EmployeesBottomSheetDialog
import com.elka.heofficeclub.view.list.employees.EmployeesAdapter
import com.elka.heofficeclub.view.list.employees.EmployeesViewHolder
import com.elka.heofficeclub.view.ui.BaseFragmentWithOrganization
import com.elka.heofficeclub.viewModel.DivisionsViewModel
import com.elka.heofficeclub.viewModel.EmployerViewModel
import com.elka.heofficeclub.viewModel.OrganizationEmployeesViewModel

class OrganizationEmployeesFragment : BaseFragmentWithOrganization() {
  private val divisionsViewModel by activityViewModels<DivisionsViewModel>()
  private val viewModel by activityViewModels<EmployerViewModel>()

  private val organizationObserver = Observer<Organization?> {
    if (it == null) return@Observer

    organizationEmployeesViewModel.setOrganization(it)
    divisionsViewModel.setOrganization(it)
  }

  override val externalActionObserver = Observer<Action?> {
    if (it == null) return@Observer

    when (it) {
      Action.REMOVED_POSITION -> organizationViewModel.reloadCurrentOrganization()
      Action.GENERATE_T7 -> generateT7()
      Action.AFTER_CREATE_T7 -> afterCreateT7()
      Action.GENERATE_T3 -> generateT3()
      Action.AFTER_CREATE_T3 -> afterCreateT3()
      else -> super.externalActionObserver.onChanged(it)
    }
  }

  private fun afterCreateT7() {
    val message = getString(R.string.t7_created)
    Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
  }

  private fun generateT7() {
    val t7 = organizationEmployeesViewModel.getT7()
    val uri = DocumentCreator(requireContext()).createFormT7(t7)
    organizationEmployeesViewModel.saveT7(t7, uri)
  }

  private fun afterCreateT3() {
    val message = getString(R.string.t3_created)
    Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
  }

  private fun generateT3() {
    val t3 = organizationEmployeesViewModel.getT3()
    val uri = DocumentCreator(requireContext()).createFormT3(t3)
    organizationEmployeesViewModel.saveT3(t3, uri)
  }

  private val works = listOf(
    Work.LOAD_POSITIONS,
    Work.REMOVE_POSITION,
    Work.LOAD_ORGANIZATION,
    Work.LOAD_DIVISIONS,
    Work.LOAD_EMPLOYERS,
    Work.CREATE_T7,
    Work.CREATE_T3
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
    if (hasLoads) return

    if (!createDocumentPermissionGranted) {
      createDocumentPermissionLauncher.launch(arrayOf(permissionRead, permissionWrite))
      return
    }

    viewModel.setPositions(organizationEmployeesViewModel.positions.value!!)
    viewModel.setDivisions(divisionsViewModel.divisions.value!!)
    viewModel.setOrganization(organizationViewModel.organization.value!!)

    val dir =
      OrganizationEmployeesFragmentDirections.actionOrganizationEmployeesFragmentToEmployerFragment(
        employer
      )
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

    viewModel.setPositions(organizationEmployeesViewModel.positions.value!!)
    viewModel.setDivisions(divisionsViewModel.divisions.value!!)
    viewModel.setOrganization(organizationViewModel.organization.value!!)

    val direction =
      OrganizationEmployeesFragmentDirections.actionOrganizationEmployeesFragmentToCreateEmployerFragment()
    navController.navigate(direction)
  }

  fun createT7() {
    if (!createDocumentPermissionGranted) {
      createDocumentPermissionLauncher.launch(arrayOf(permissionRead, permissionWrite))
      return
    }

    organizationEmployeesViewModel.createT7()
  }

  fun createT3() {
    if (!createDocumentPermissionGranted) {
      createDocumentPermissionLauncher.launch(arrayOf(permissionRead, permissionWrite))
      return
    }

    organizationEmployeesViewModel.createT3()
  }

  private val bottomSheetMenuListener: EmployeesBottomSheetDialog.Companion.ItemClickListener by lazy {
    object: EmployeesBottomSheetDialog.Companion.ItemClickListener {
      override fun onItemClick(formType: FormType) {
        when(formType) {
          FormType.T2 -> addEmployer()
          FormType.T3 -> createT3()
          FormType.T7 -> createT7()
          else -> Unit
        }
        bottomSheetMenu.dismiss()
      }
    }
  }
  private val bottomSheetMenu: EmployeesBottomSheetDialog by lazy { EmployeesBottomSheetDialog(bottomSheetMenuListener) }
  fun openBottomSheetMenu() {
    if (hasLoads) return
    bottomSheetMenu.show(activity.supportFragmentManager, "EMPLOYER_BOTTOM_MENU")
  }
}