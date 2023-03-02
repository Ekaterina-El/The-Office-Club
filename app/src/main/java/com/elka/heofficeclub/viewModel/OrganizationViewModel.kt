package com.elka.heofficeclub.viewModel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.elka.heofficeclub.other.Action
import com.elka.heofficeclub.other.Work
import com.elka.heofficeclub.service.model.Organization
import com.elka.heofficeclub.service.model.User
import com.elka.heofficeclub.service.repository.OrganizationRepository
import com.elka.heofficeclub.service.repository.UsersRepository
import kotlinx.coroutines.launch

class OrganizationViewModel(application: Application) : BaseViewModel(application) {
  private val _profile = MutableLiveData<User?>(null)
  val profile get() = _profile

  fun loadProfile() {
    val work = Work.LOAD_PROFILE
    addWork(work)

    viewModelScope.launch {
      _error.value = UsersRepository.loadCurrentUserProfile { profile ->
        _profile.value = profile
      }

      removeWork(work)
    }
  }

  fun logout() {
    val work = Work.LOGOUT
    addWork(work)

    viewModelScope.launch {
      UsersRepository.logout {
        _externalAction.value = Action.RESTART
        clear()
      }
      removeWork(work)
    }
  }

  private val _organization = MutableLiveData<Organization?>(null)
  val organization get() = _organization

  fun loadOrganization(organizationId: String) {
    val work = Work.LOAD_ORGANIZATION
    addWork(work)

    viewModelScope.launch {
      _error.value = OrganizationRepository.loadOrganization(organizationId) { organization ->
        _organization.value = organization
      }
      removeWork(work)
    }
  }

  private fun clear() {
    _profile.value = null
    _organization.value = null
  }
}