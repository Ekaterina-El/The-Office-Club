package com.elka.heofficeclub.viewModel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.elka.heofficeclub.other.Work
import com.elka.heofficeclub.service.model.Organization
import com.elka.heofficeclub.service.model.User
import com.elka.heofficeclub.service.repository.UsersRepository
import kotlinx.coroutines.delay
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

  private fun filterEditors() {
    val editors = _editors.value!!
    val search = ""

    _filteredEditors.value = editors
  }

  fun clear() {
    _filteredEditors.value = listOf()
    _externalAction.value = null
    _error.value = null
    clearWork()
  }

  fun setOrganization(organization: Organization) {
    loadEditors(organization.editors)
  }
}