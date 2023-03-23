package com.elka.heofficeclub.view.ui.organizationScreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.elka.heofficeclub.view.list.organizationPositions.OrgPositionsAdapter
import com.elka.heofficeclub.view.list.organizationPositions.OrgPositionsViewHolder
import com.elka.heofficeclub.view.ui.BaseFragmentWithOrganization
import com.elka.heofficeclub.viewModel.CreateEmployerViewModel
import com.elka.heofficeclub.viewModel.DivisionsViewModel
import com.elka.heofficeclub.viewModel.OrganizationEmployeesViewModel

class OrganizationEmployeesFragment : BaseFragmentWithOrganization() {

  private val employeesObserver = Observer<List<Employer>> {
    val a = it
  }

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
    binding.swiper2.isRefreshing = hasLoads
  }

  private val orgPositionsObserver = Observer<List<OrganizationPosition>> {
    orgPositionsAdapter.setItems(it)
  }

  private lateinit var binding: OrganizationEmplyeesFragmentBinding
  private lateinit var organizationEmployeesViewModel: OrganizationEmployeesViewModel
  private lateinit var orgPositionsAdapter: OrgPositionsAdapter

  private val createEmployerviewModel by activityViewModels<CreateEmployerViewModel>()
  private val divisionsViewModel by activityViewModels<DivisionsViewModel>()


  private val orgPositionsListener by lazy {
    object : OrgPositionsViewHolder.Companion.Listener {
      override fun onDeletePosition(position: OrganizationPosition) {
        tryDeletePosition(position)
      }
    }
  }

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    orgPositionsAdapter = OrgPositionsAdapter(orgPositionsListener)
    organizationEmployeesViewModel =
      ViewModelProvider(this)[OrganizationEmployeesViewModel::class.java]

    binding = OrganizationEmplyeesFragmentBinding.inflate(layoutInflater, container, false)
    binding.apply {
      viewModel = this@OrganizationEmployeesFragment.organizationViewModel
      viewModelEmployees = this@OrganizationEmployeesFragment.organizationEmployeesViewModel
      lifecycleOwner = viewLifecycleOwner
      master = this@OrganizationEmployeesFragment
      orgPositionsAdapter = this@OrganizationEmployeesFragment.orgPositionsAdapter
    }

    return binding.root
  }

  private val organizationPositionDialogListener by lazy {
    object : OrganizationPositionDialog.Companion.Listener {
      override fun agree(organizationPosition: OrganizationPosition) {
        organizationViewModel.addPosition(organizationPosition)
      }

    }
  }

  private val organizationPositionDialog by lazy {
    OrganizationPositionDialog(
      requireContext(),
      viewModelOwner = this,
      viewLifecycleOwner,
      organizationViewModel.organization.value!!.id,
      organizationPositionDialogListener
    )
  }

  fun addOrganizationPosition() {
    organizationPositionDialog.open()
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    val decorator = DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
    binding.positionsList.addItemDecoration(decorator)

    binding.swiper1.setOnRefreshListener { organizationViewModel.reloadCurrentOrganization() }
    binding.swiper2.setOnRefreshListener { organizationViewModel.reloadCurrentOrganization() }

    val color = requireContext().getColor(R.color.accent)
    binding.swiper1.setColorSchemeColors(color)
    binding.swiper2.setColorSchemeColors(color)

    binding.noFound.message.text = getString(R.string.organization_positions_empty)
    binding.noFoundPositions.message.text = getString(R.string.organization_positions_empty)
  }

  override fun onResume() {
    super.onResume()

    organizationViewModel.organization.observe(this, organizationObserver)
    organizationViewModel.work.observe(this, workObserver)
    organizationViewModel.error.observe(this, errorObserver)

    organizationEmployeesViewModel.work.observe(this, workObserver)
    organizationEmployeesViewModel.orgPositionsFiltered.observe(this, orgPositionsObserver)
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
    organizationEmployeesViewModel.orgPositionsFiltered.removeObserver(orgPositionsObserver)
    organizationEmployeesViewModel.error.removeObserver(errorObserver)
    organizationEmployeesViewModel.externalAction.removeObserver(externalActionObserver)

  }

  private val deletePositionListener by lazy {
    object : ConfirmDialog.Companion.Listener {
      override fun agree() {
        organizationEmployeesViewModel.deletePosition(tmpPositionForDelete!!)
        disagree()
      }

      override fun disagree() {
        confirmDialog.close()
        tmpPositionForDelete = null
      }
    }
  }

  private var tmpPositionForDelete: OrganizationPosition? = null
  private fun tryDeletePosition(position: OrganizationPosition) {
    tmpPositionForDelete = position
    val title = getString(R.string.delete_position_title)
    val message = getString(R.string.delete_position_message, position.name, position.salary)
    confirmDialog.open(title, message, deletePositionListener)
  }


  fun addEmployer() {
    if (hasLoads) return

    if (!createDocumentPermissionGranted) {
      createDocumentPermissionLauncher.launch(arrayOf(permissionRead, permissionWrite))
      return
    }

    createEmployerviewModel.setPositions(organizationEmployeesViewModel.positions.value!!)
    createEmployerviewModel.setDivisions(divisionsViewModel.divisions.value!!)
    createEmployerviewModel.setOrganization(organizationViewModel.organization.value!!)

    val direction =
      OrganizationEmployeesFragmentDirections.actionOrganizationEmployeesFragmentToCreateEmployerFragment()
    navController.navigate(direction)
  }
}