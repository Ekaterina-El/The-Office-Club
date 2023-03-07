package com.elka.heofficeclub.viewModel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.elka.heofficeclub.other.Work
import com.elka.heofficeclub.service.model.Organization
import com.elka.heofficeclub.service.model.User
import com.elka.heofficeclub.service.model.filterBy
import com.elka.heofficeclub.service.repository.OrganizationRepository
import com.elka.heofficeclub.service.repository.UsersRepository
import kotlinx.coroutines.launch

class OrganizationEditorsViewModel(application: Application) : BaseViewModel(application) {
  private val _editors = MutableLiveData<List<User>>(listOf())
  private val _filteredEditors = MutableLiveData<List<User>>(listOf())
  val filteredEditors get() = _filteredEditors

  private fun loadEditors(editors: List<String>) {
    val work = Work.LOAD_EDITOR
    addWork(work)

    viewModelScope.launch {
      _error.value = UsersRepository.loadUsersById(editors) { users ->
        _editors.value = users
        filterEditors()
      }
      removeWork(work)
    }

  }

  val filter = MutableLiveData("")
  fun filterEditors() {
    val editors = _editors.value!!
    val search = filter.value!!

    _filteredEditors.value = when {
      search.isEmpty() -> editors
      else -> editors.filterBy(search)
    }
  }

  fun clearFilter() {
    filter.value = ""
    filterEditors()
  }

  fun clear() {
    _filteredEditors.value = listOf()
    _externalAction.value = null
    _error.value = null
    filter.value = ""
    clearWork()
  }

  private var organizationId: String? = null

  fun setOrganization(organization: Organization) {
    organizationId = organization.id
    loadEditors(organization.editors)
  }

  fun changeBlockingStatusOfEditor(editor: User) {
    val work = Work.CHANGE_BLOCKING_STATUS
    addWork(work)

    viewModelScope.launch {
      _error.value = UsersRepository.changeBlockingStatus(editor) { newStatus ->
        _editors.value = _editors.value!!.map {
          if (it.id == editor.id) {
            it.status = newStatus
          }
          return@map it
        }
        filterEditors()
      }
      removeWork(work)
    }
  }


}