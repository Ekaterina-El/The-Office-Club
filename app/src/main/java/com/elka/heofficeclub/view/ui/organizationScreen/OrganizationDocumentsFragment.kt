package com.elka.heofficeclub.view.ui.organizationScreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import com.elka.heofficeclub.R
import com.elka.heofficeclub.databinding.OrganizationDocumentsFragmentBinding
import com.elka.heofficeclub.other.Work
import com.elka.heofficeclub.other.documents.FormType
import com.elka.heofficeclub.service.model.Organization
import com.elka.heofficeclub.service.model.documents.forms.DocForm
import com.elka.heofficeclub.service.model.documents.forms.T1
import com.elka.heofficeclub.view.list.docs.DocumentViewHolder
import com.elka.heofficeclub.view.list.docs.DocumentsAdapter
import com.elka.heofficeclub.view.ui.BaseFragmentWithOrganization
import com.elka.heofficeclub.viewModel.OrganizationDocsViewModel

class OrganizationDocumentsFragment : BaseFragmentWithOrganization() {
  private lateinit var binding: OrganizationDocumentsFragmentBinding
  private val documentsViewModel by activityViewModels<OrganizationDocsViewModel>()

  private val works = listOf(
    Work.LOAD_DOCS
  )

  private val hasLoads: Boolean
    get() {
      val w1 = organizationViewModel.work.value!!.toMutableList()
      val w2 = documentsViewModel.work.value!!
      w1.addAll(w2)

      return when {
        w1.isEmpty() -> false
        else -> w1
          .map { item -> if (works.contains(item)) 1 else 0 }
          .reduce { a, b -> a + b } > 0
      }
    }

  private val documentsListener by lazy {
    object : DocumentViewHolder.Companion.Listener {
      override fun onSelect(docForm: DocForm) {
        if (hasLoads) return

        if (docForm.type == FormType.T1) {
          organizationViewModel.setBottomMenuStatus(false)

          val dir =
            OrganizationDocumentsFragmentDirections.actionOrganizationDocumentsFragmentToDocumentT1Fragment(
              docForm as T1
            )
          navController.navigate(dir)
        }
      }
    }
  }

  private val documentsAdapter by lazy { DocumentsAdapter(documentsListener) }

  private val organizationObserver = Observer<Organization?> {
    documentsViewModel.setOrganization(it)
  }

  private val filteredDocsObserver = Observer<List<DocForm>> {
    documentsAdapter.setItems(it)
  }


  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
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
    super.onViewCreated(view, savedInstanceState)

    val decorator = DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
    binding.documentsList.addItemDecoration(decorator)

    binding.swiper1.setOnRefreshListener { documentsViewModel.reloadDocs() }

    val color = requireContext().getColor(R.color.accent)
    binding.swiper1.setColorSchemeColors(color)
  }

  override fun onResume() {
    super.onResume()

    organizationViewModel.organization.observe(this, organizationObserver)
    documentsViewModel.docsFiltered.observe(this, filteredDocsObserver)
  }

  override fun onStop() {
    super.onStop()

    organizationViewModel.organization.removeObserver(organizationObserver)
    documentsViewModel.docsFiltered.removeObserver(filteredDocsObserver)
  }


}