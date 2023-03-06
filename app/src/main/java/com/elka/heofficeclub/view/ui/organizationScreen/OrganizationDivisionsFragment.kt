package com.elka.heofficeclub.view.ui.organizationScreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.elka.heofficeclub.R
import com.elka.heofficeclub.databinding.OrganizationDivisionsFragmentBinding
import com.elka.heofficeclub.other.Action
import com.elka.heofficeclub.other.Work
import com.elka.heofficeclub.service.model.Division
import com.elka.heofficeclub.service.model.Organization
import com.elka.heofficeclub.view.dialog.ConfirmDialog
import com.elka.heofficeclub.view.dialog.CreateDivisionDialog
import com.elka.heofficeclub.view.list.divisions.DivisionsAdapter
import com.elka.heofficeclub.view.ui.BaseFragmentWithOrganization
import com.elka.heofficeclub.viewModel.DivisionsViewModel

class OrganizationDivisionsFragment : BaseFragmentWithOrganization() {
  private lateinit var binding: OrganizationDivisionsFragmentBinding
  private lateinit var viewModel: DivisionsViewModel

  private val divisionAdapterListener by lazy {
    object : DivisionsAdapter.Companion.Listener {
      override fun onDelete(division: Division) {
        confirmDeleteDialog(division)
      }
    }
  }
  private lateinit var divisionsAdapter: DivisionsAdapter

  val works = listOf(
    Work.ADD_DIVISION,
    Work.LOAD_DIVISIONS,
    Work.REMOVE_DIVISION,
    Work.LOAD_ORGANIZATION
  )

  override val workObserver = Observer<List<Work>> {
    val w1 = organizationViewModel.work.value!!
    val works = viewModel.work.value!!.toMutableList()
    works.addAll(w1)

    val isLoad =
      when {
        works.isEmpty() -> false
        else -> works.map { item -> if (works.contains(item)) 1 else 0 }
          .reduce { a, b -> a + b } > 0
      }

    binding.swipeRefreshLayout.isRefreshing = isLoad
    binding.swipeRefreshLayout2.isRefreshing = isLoad
  }

  override val externalActionObserver = Observer<Action?> { action ->
    if (action == Action.ADDED_NEW_DIVISION_TO_ORGANIZATION && viewModel.addedDivision != null) {
      organizationViewModel.addDivisionId(viewModel.addedDivision!!.id)
      viewModel.afterNotificationAboutChangedDivisions()
    } else if (action == Action.REMOVED_DIVISION && viewModel.removedDivision != null) {
      organizationViewModel.removeDivisionId(viewModel.removedDivision!!.id)
      viewModel.afterNotificationAboutChangedDivisions()
    }
  }

  private val organizationObserver = Observer<Organization?> { organization ->
    if (organization == null) return@Observer
    viewModel.setOrganization(organization)
  }

  private val divisionsObserver = Observer<List<Division>> { divisions ->
    divisionsAdapter.setItems(divisions)
  }

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    divisionsAdapter = DivisionsAdapter(divisionAdapterListener)
    viewModel = ViewModelProvider(this)[DivisionsViewModel::class.java]
    binding = OrganizationDivisionsFragmentBinding.inflate(layoutInflater, container, false)
    binding.apply {
      lifecycleOwner = viewLifecycleOwner
      master = this@OrganizationDivisionsFragment
      viewModel = this@OrganizationDivisionsFragment.viewModel
      adapter = this@OrganizationDivisionsFragment.divisionsAdapter
    }

    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    val decorator = DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
    binding.recyclerViewDivisions.addItemDecoration(decorator)

    val refresherColor = requireContext().getColor(R.color.accent)
    val swipeRefreshListener =
      SwipeRefreshLayout.OnRefreshListener { organizationViewModel.reloadCurrentOrganization() }
    binding.swipeRefreshLayout.setColorSchemeColors(refresherColor)
    binding.swipeRefreshLayout.setOnRefreshListener(swipeRefreshListener)
    binding.swipeRefreshLayout2.setColorSchemeColors(refresherColor)
    binding.swipeRefreshLayout2.setOnRefreshListener(swipeRefreshListener)

    binding.layoutNoFound.findViewById<TextView>(R.id.message).text =
      getString(R.string.divisions_no_found)

  }

  private val createDivisionListener: CreateDivisionDialog.Companion.Listener by lazy {
    object : CreateDivisionDialog.Companion.Listener {
      override fun agree(division: Division) {
        viewModel.addDivision(division)
        createDivisionDialog.close()
      }

      override fun disagree() {
        createDivisionDialog.close()
      }
    }
  }

  private val createDivisionDialog by lazy {
    CreateDivisionDialog(requireContext(), createDivisionListener)
  }

  fun addDivision() {
    val currentDivisionLevel = viewModel.currentLevel.value!!
    createDivisionDialog.open(currentDivisionLevel)
  }

  override fun onResume() {
    super.onResume()

    organizationViewModel.organization.observe(viewLifecycleOwner, organizationObserver)
    organizationViewModel.work.observe(viewLifecycleOwner, workObserver)
    viewModel.work.observe(viewLifecycleOwner, workObserver)
    viewModel.error.observe(viewLifecycleOwner, errorObserver)
    viewModel.filteredDivisions.observe(viewLifecycleOwner, divisionsObserver)
    viewModel.externalAction.observe(viewLifecycleOwner, externalActionObserver)
  }

  override fun onDestroy() {
    super.onDestroy()

    organizationViewModel.organization.removeObserver(organizationObserver)
    organizationViewModel.work.removeObserver(workObserver)
    viewModel.work.removeObserver(workObserver)
    viewModel.error.removeObserver(errorObserver)
    viewModel.filteredDivisions.removeObserver(divisionsObserver)
    viewModel.externalAction.removeObserver(externalActionObserver)
  }

  private fun confirmDeleteDialog(division: Division) {
    val title = getString(R.string.delete_division_title)
    val message = getString(R.string.delete_division_message, division.name)
    val listener = object : ConfirmDialog.Companion.Listener {
      override fun agree() {
        viewModel.deleteDivision(division)
        confirmDialog.close()
      }

      override fun disagree() {
        confirmDialog.close()
      }
    }
    
    confirmDialog.open(title, message, listener)
  }
}