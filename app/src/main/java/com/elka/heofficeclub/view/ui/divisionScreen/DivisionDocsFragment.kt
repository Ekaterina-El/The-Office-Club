package com.elka.heofficeclub.view.ui.divisionScreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavDirections
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.elka.heofficeclub.R
import com.elka.heofficeclub.databinding.DivisionDocsFragmentBinding
import com.elka.heofficeclub.other.Work
import com.elka.heofficeclub.other.documents.FormType
import com.elka.heofficeclub.service.model.Division
import com.elka.heofficeclub.service.model.Organization
import com.elka.heofficeclub.service.model.documents.forms.*
import com.elka.heofficeclub.view.list.docs.DocumentViewHolder
import com.elka.heofficeclub.view.list.docs.DocumentsAdapter
import com.elka.heofficeclub.view.ui.BaseDocsFragment
import com.elka.heofficeclub.view.ui.BaseFragmentWithOrganization
import com.elka.heofficeclub.view.ui.organizationScreen.OrganizationDocumentsFragmentDirections
import com.elka.heofficeclub.viewModel.DivisionViewModel
import com.elka.heofficeclub.viewModel.OrganizationDocsViewModel

class DivisionDocsFragment : BaseDocsFragment() {
  private lateinit var binding: DivisionDocsFragmentBinding
  private val divisionViewModel by activityViewModels<DivisionViewModel>()

  override lateinit var documentsList: RecyclerView
  override lateinit var swiper: SwipeRefreshLayout

  override val filteredDocsObserver = Observer<List<DocForm>> {
    val divisionDocs = divisionViewModel.division.value?.docs ?: return@Observer
    val filtered = it.filter { doc -> divisionDocs.contains(doc.id) }
    documentsAdapter.setItems(filtered)
  }

  val divisionObserver = Observer<Division?> {
    if (it == null) return@Observer

    val divisionDocs = it.docs
    val filtered = documentsViewModel.docsFiltered.value!!.filter { doc -> divisionDocs.contains(doc.id) }
    documentsAdapter.setItems(filtered)
  }

  override fun reload() {
    divisionViewModel.reloadCurrentDivision {
      organizationViewModel.reloadCurrentOrganization()
    }
  }

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
  ): View {
    binding = DivisionDocsFragmentBinding.inflate(layoutInflater, container, false)
    binding.apply {
      lifecycleOwner = viewLifecycleOwner
      master = this@DivisionDocsFragment
      documentsAdapter = this@DivisionDocsFragment.documentsAdapter
      documentsViewmodel = this@DivisionDocsFragment.documentsViewModel
    }

    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    swiper = binding.swiper1
    documentsList = binding.documentsList
    super.onViewCreated(view, savedInstanceState)
  }

  override fun docFormTypeNavigation(docForm: DocForm): NavDirections? {
    val dirs = DivisionDocsFragmentDirections
    val dir = when (docForm.type) {
      FormType.T1 -> dirs.actionDivisionDocsFragmentToDocumentT1Fragment(docForm as T1)
      FormType.T3 -> dirs.actionDivisionDocsFragmentToDocumentT3Fragment(docForm as T3)
      FormType.T5 -> dirs.actionDivisionDocsFragmentToDocumentT5Fragment(docForm as T5)
      FormType.T6 -> dirs.actionDivisionDocsFragmentToDocumentT6Fragment(docForm as T6)
      FormType.T7 -> dirs.actionDivisionDocsFragmentToDocumentT7Fragment(docForm as T7)
      FormType.T8 -> dirs.actionDivisionDocsFragmentToDocumentT8Fragment(docForm as T8)
      FormType.T11 -> dirs.actionDivisionDocsFragmentToDocumentT11Fragment(docForm as T11)
      else -> return null
    }
    return dir
  }

  override fun onResume() {
    super.onResume()
    divisionViewModel.division.observe(this, divisionObserver)
    organizationViewModel.setBottomMenuStatus(false)
  }

  override fun onStop() {
    super.onStop()
    divisionViewModel.division.removeObserver(divisionObserver)
  }
}