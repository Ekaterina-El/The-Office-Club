package com.elka.heofficeclub.view.ui.organizationScreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import com.elka.heofficeclub.databinding.OrganizationEmplyeesFragmentBinding
import com.elka.heofficeclub.other.Work
import com.elka.heofficeclub.service.model.Organization
import com.elka.heofficeclub.service.model.OrganizationPosition
import com.elka.heofficeclub.view.dialog.OrganizationPositionDialog
import com.elka.heofficeclub.view.list.organizationPositions.OrgPositionsAdapter
import com.elka.heofficeclub.view.ui.BaseFragmentWithOrganization
import com.elka.heofficeclub.viewModel.OrganizationEmployeesViewModel

class OrganizationEmployeesFragment : BaseFragmentWithOrganization() {

  private val organizationObserver = Observer<Organization?> {
    if (it == null) return@Observer

    organizationEmployeesViewModel.setOrganization(it)
  }

  private val works = listOf(
    Work.LOAD_POSITIONS,
    Work.REMOVE_POSITION,
    Work.LOAD_ORGANIZATION
  )

  override val workObserver = Observer<List<Work>> {
    val w1 = organizationViewModel.work.value!!
    val w = organizationEmployeesViewModel.work.value!!.toMutableList()
    w.addAll(w1)

    val isLoad =
      when {
        w.isEmpty() -> false
        else -> w.map { item -> if (works.contains(item)) 1 else 0 }
          .reduce { a, b -> a + b } > 0
      }

//    binding.swipeRefreshLayout.isRefreshing = isLoad
//    binding.swipeRefreshLayout2.isRefreshing = isLoad
  }

  private val orgPositionsObserver = Observer<List<OrganizationPosition>> {
    orgPositionsAdapter.setItems(it)
  }

  private lateinit var binding: OrganizationEmplyeesFragmentBinding
  private lateinit var organizationEmployeesViewModel: OrganizationEmployeesViewModel
  private lateinit var orgPositionsAdapter: OrgPositionsAdapter

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    orgPositionsAdapter = OrgPositionsAdapter()
    organizationEmployeesViewModel = ViewModelProvider(this)[OrganizationEmployeesViewModel::class.java]

    binding = OrganizationEmplyeesFragmentBinding.inflate(layoutInflater, container, false)
    binding.apply {
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
  }

  override fun onResume() {
    super.onResume()

    organizationViewModel.organization.observe(this, organizationObserver)
    organizationViewModel.work.observe(this, workObserver)
    organizationViewModel.error.observe(this, errorObserver)

    organizationEmployeesViewModel.work.observe(this, workObserver)
    organizationEmployeesViewModel.orgPositions.observe(this, orgPositionsObserver)
    organizationEmployeesViewModel.error.observe(this, errorObserver)

  }

  override fun onStop() {
    super.onStop()

    organizationViewModel.organization.removeObserver(organizationObserver)
    organizationViewModel.work.removeObserver(workObserver)
    organizationViewModel.error.removeObserver(errorObserver)

    organizationEmployeesViewModel.work.removeObserver(workObserver)
    organizationEmployeesViewModel.orgPositions.removeObserver(orgPositionsObserver)
    organizationEmployeesViewModel.error.removeObserver(errorObserver)
  }
}