package com.elka.heofficeclub.view.ui.organizationScreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import com.elka.heofficeclub.R
import com.elka.heofficeclub.databinding.OrganizationDivisionsFragmentBinding
import com.elka.heofficeclub.other.Action
import com.elka.heofficeclub.service.model.Division
import com.elka.heofficeclub.service.model.Organization
import com.elka.heofficeclub.view.dialog.CreateDivisionDialog
import com.elka.heofficeclub.view.list.divisions.DivisionsAdapter
import com.elka.heofficeclub.view.ui.BaseFragmentWithOrganization
import com.elka.heofficeclub.viewModel.DivisionsViewModel

class OrganizationDivisionsFragment: BaseFragmentWithOrganization() {
    private lateinit var binding: OrganizationDivisionsFragmentBinding
    private lateinit var viewModel: DivisionsViewModel

    private lateinit var divisionsAdapter: DivisionsAdapter

    override val externalActionObserver = Observer<Action?> { action ->
        if (action == Action.ADDED_NEW_DIVISION_TO_ORGANIZATION && viewModel.addedDivision != null) {
            organizationViewModel.addDivisionId(viewModel.addedDivision!!.id)
            viewModel.afterNotificationAboutAddedDivision()
        }
    }

    private val organizationObserver = Observer<Organization?> { organization ->
        binding.swipeRefreshLayout.isRefreshing = false

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
        divisionsAdapter = DivisionsAdapter()
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

        binding.swipeRefreshLayout.setColorSchemeColors(
            requireContext().getColor(R.color.accent),
        )

        binding.swipeRefreshLayout.setOnRefreshListener {
            organizationViewModel.reloadCurrentOrganization()
        }

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
        viewModel.work.observe(viewLifecycleOwner, workObserver)
        viewModel.error.observe(viewLifecycleOwner, errorObserver)
        viewModel.division.observe(viewLifecycleOwner, divisionsObserver)
        viewModel.externalAction.observe(viewLifecycleOwner, externalActionObserver)
    }

    override fun onDestroy() {
        super.onDestroy()

        organizationViewModel.organization.removeObserver(organizationObserver)
        viewModel.work.removeObserver(workObserver)
        viewModel.error.removeObserver(errorObserver)
        viewModel.division.removeObserver(divisionsObserver)
        viewModel.externalAction.removeObserver(externalActionObserver)
    }
}