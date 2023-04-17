package com.elka.heofficeclub.view.ui.organizationScreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.NavDirections
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.elka.heofficeclub.databinding.OrganizationDocumentsFragmentBinding
import com.elka.heofficeclub.other.documents.FormType
import com.elka.heofficeclub.service.model.documents.forms.*
import com.elka.heofficeclub.view.ui.BaseDocsFragment

class OrganizationDocumentsFragment : BaseDocsFragment() {
  private lateinit var binding: OrganizationDocumentsFragmentBinding
  override lateinit var documentsList: RecyclerView
  override lateinit var swiper: SwipeRefreshLayout

  override fun reload() {
    organizationViewModel.reloadCurrentOrganization()
  }

  override fun docFormTypeNavigation(docForm: DocForm): NavDirections? {
    val dirs = OrganizationDocumentsFragmentDirections
    val dir = when (docForm.type) {
      FormType.T1 -> dirs.actionOrganizationDocumentsFragmentToDocumentT1Fragment(docForm as T1)
      FormType.T3 -> dirs.actionOrganizationDocumentsFragmentToDocumentT3Fragment(docForm as T3)
      FormType.T5 -> dirs.actionOrganizationDocumentsFragmentToDocumentT5Fragment(docForm as T5)
      FormType.T6 -> dirs.actionOrganizationDocumentsFragmentToDocumentT6Fragment(docForm as T6)
      FormType.T7 -> dirs.actionOrganizationDocumentsFragmentToDocumentT7Fragment(docForm as T7)
      FormType.T8 -> dirs.actionOrganizationDocumentsFragmentToDocumentT8Fragment(docForm as T8)
      FormType.T11 -> dirs.actionOrganizationDocumentsFragmentToDocumentT11Fragment(docForm as T11)
      else -> return null
    }
    return dir
  }

  override val filteredDocsObserver = Observer<List<DocForm>> {
    documentsAdapter.setItems(it)
  }

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
  ): View {
    binding = OrganizationDocumentsFragmentBinding.inflate(layoutInflater, container, false)
    binding.apply {
      lifecycleOwner = viewLifecycleOwner
      documentsAdapter = this@OrganizationDocumentsFragment.documentsAdapter
      documentsViewmodel = this@OrganizationDocumentsFragment.documentsViewModel
      master = this@OrganizationDocumentsFragment
    }

    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    documentsList = binding.documentsList
    swiper = binding.swiper1
    super.onViewCreated(view, savedInstanceState)
  }
}