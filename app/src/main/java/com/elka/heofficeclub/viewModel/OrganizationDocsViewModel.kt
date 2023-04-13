package com.elka.heofficeclub.viewModel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.elka.heofficeclub.other.Work
import com.elka.heofficeclub.service.model.Employer
import com.elka.heofficeclub.service.model.Organization
import com.elka.heofficeclub.service.model.documents.forms.DocForm
import com.elka.heofficeclub.service.model.documents.forms.filterBy
import com.elka.heofficeclub.service.model.filterBy
import com.elka.heofficeclub.service.model.splitByT8
import com.elka.heofficeclub.service.repository.DocumentsRepository
import com.elka.heofficeclub.service.repository.EmployeesRepository
import kotlinx.coroutines.launch

class OrganizationDocsViewModel(application: Application) : BaseViewModel(application) {
  private var _organization: Organization? = null

  fun setOrganization(organization: Organization) {
    _organization = organization
    loadDocs(organization.docs)
  }

  private val _docs = MutableLiveData<List<DocForm>>(listOf())
  val docs get() = _docs

  private val _docsFiltered = MutableLiveData<List<DocForm>>(listOf())
  val docsFiltered get() = _docsFiltered

  private fun loadDocs(docsIdx: List<String>) {
    val work = Work.LOAD_DOCS
    addWork(work)

    viewModelScope.launch {
      _error.value = DocumentsRepository.loadDocs(docsIdx) { docs ->
        _docs.value = docs
        filterDocs()
        removeWork(work)
      }

      if (_error.value != null) removeWork(work)
    }
  }

  // region Search employer
  val searchDocs = MutableLiveData("")

  fun filterDocs() {
    val items = _docsFiltered.value!!
    val search = searchDocs.value!!

    _docsFiltered.value = when (search) {
      "" -> items
      else -> items.filterBy(search)
    }
  }

}