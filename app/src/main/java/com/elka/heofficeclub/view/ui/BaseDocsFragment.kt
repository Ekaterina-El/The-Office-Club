package com.elka.heofficeclub.view.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavDirections
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.elka.heofficeclub.R
import com.elka.heofficeclub.other.Work
import com.elka.heofficeclub.service.model.Organization
import com.elka.heofficeclub.service.model.documents.forms.DocForm
import com.elka.heofficeclub.view.list.docs.DocumentViewHolder
import com.elka.heofficeclub.view.list.docs.DocumentsAdapter
import com.elka.heofficeclub.viewModel.OrganizationDocsViewModel

abstract class BaseDocsFragment : BaseFragmentWithOrganization() {
  protected val documentsViewModel by activityViewModels<OrganizationDocsViewModel>()

  private val organizationObserver = Observer<Organization?> {
    val docsFromLocalDb = activity.appDatabase.getDocumentsDao().getIds().map { doc -> doc.id }
    val orgIds = it.docs
    val delta = orgIds.filter { docId -> !docsFromLocalDb.contains(docId) }

    if (docsFromLocalDb.isNotEmpty()) {
      val items = activity.appDatabase.getAllDocs()
      if (items != documentsViewModel.docs) documentsViewModel.setLocalDocuments(items)
    }

    if (delta.isNotEmpty()) {
      activity.appDatabase.addDeltaIds(delta)
      documentsViewModel.loadDocs(delta)
    }
  }

  val loadedDocsObserver = Observer<List<DocForm>> {
    if (it == null) return@Observer
    activity.appDatabase.addDocs(it)
  }


  abstract val filteredDocsObserver: Observer<List<DocForm>>

  private val works = listOf(
    Work.LOAD_DOCS, Work.LOAD_ORGANIZATION
  )

  protected val hasLoads: Boolean
    get() {
      val w1 = organizationViewModel.work.value!!.toMutableList()
      val w2 = documentsViewModel.work.value!!
      w1.addAll(w2)

      return when {
        w1.isEmpty() -> false
        else -> w1.map { item -> if (works.contains(item)) 1 else 0 }.reduce { a, b -> a + b } > 0
      }
    }

  abstract fun docFormTypeNavigation(docForm: DocForm): NavDirections?

  private val documentsListener by lazy {
    object : DocumentViewHolder.Companion.Listener {
      override fun onSelect(docForm: DocForm) {
        if (hasLoads) return

        val dir = docFormTypeNavigation(docForm) ?: return
        organizationViewModel.setBottomMenuStatus(false)
        navController.navigate(dir)
      }
    }
  }
  protected val documentsAdapter by lazy { DocumentsAdapter(documentsListener) }


  protected abstract val documentsList: RecyclerView
  protected abstract val swiper: SwipeRefreshLayout

  override val workObserver = Observer<List<Work>> {
    swiper.isRefreshing = hasLoads
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    val decorator = DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
    documentsList.addItemDecoration(decorator)

    val color = requireContext().getColor(R.color.accent)
    swiper.setColorSchemeColors(color)
    swiper.setOnRefreshListener { reload() }
  }

  abstract fun reload()

  override fun onResume() {
    super.onResume()

    organizationViewModel.organization.observe(this, organizationObserver)
    documentsViewModel.docsFiltered.observe(this, filteredDocsObserver)
    organizationViewModel.work.observe(this, workObserver)
    documentsViewModel.work.observe(this, workObserver)

    documentsViewModel.loadedDocs.observe(this, loadedDocsObserver)


    if (documentsViewModel.docs.value!!.isEmpty()) organizationViewModel.reloadCurrentOrganization()
  }

  override fun onStop() {
    super.onStop()

    organizationViewModel.organization.removeObserver(organizationObserver)
    documentsViewModel.docsFiltered.removeObserver(filteredDocsObserver)
    organizationViewModel.work.removeObserver(workObserver)
    documentsViewModel.work.removeObserver(workObserver)

    documentsViewModel.loadedDocs.removeObserver(loadedDocsObserver)
  }

}