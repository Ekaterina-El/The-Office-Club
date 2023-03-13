package com.elka.heofficeclub.viewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.elka.heofficeclub.other.Action
import com.elka.heofficeclub.other.Role
import com.elka.heofficeclub.other.Work
import com.elka.heofficeclub.service.model.Organization
import com.elka.heofficeclub.service.model.OrganizationPosition
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
        removeWork(work)
        _profile.value = profile
      }
      if (_error.value != null) removeWork(work)
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

  fun reloadCurrentOrganization() {
    val organizationId = organization.value?.id ?: return
    loadOrganization(organizationId)
  }

  fun updateOrganization(organization: Organization?) {
    _organization.value = organization
  }

  private fun changeListOfDivisionsId(id: String, action: Action) {
    val divisions = _organization.value!!.divisionsId.toMutableList()

    when (action) {
      Action.ADD -> divisions.add(id)
      Action.REMOVE -> divisions.remove(id)
      else -> return
    }

    _organization.value!!.divisionsId = divisions
  }

  fun addDivisionId(id: String) {
    changeListOfDivisionsId(id, Action.ADD)
  }

  fun removeDivisionId(id: String) {
    changeListOfDivisionsId(id, Action.REMOVE)
  }

  private val _showBottomMenu = MutableLiveData(true)
  val showBottomMenu get() = _showBottomMenu

  fun setBottomMenuStatus(show: Boolean) {
    Log.d("setBottomMenuStatus", "status: $show")
    _showBottomMenu.value = show
  }

  fun changeHead(headRole: Role, id: String) {
    val organizationId = organization.value?.id ?: return

    val currentHeadId = when(headRole) {
      Role.ORGANIZATION_HEAD -> organization.value?.organizationHeadId
      Role.HUMAN_RESOURCES_DEPARTMENT_HEAD -> organization.value?.humanResourcesDepartmentHeadId
      else -> null
    } ?: return

    val work = Work.CHANGE_HEAD
    addWork(work)

    viewModelScope.launch {
      _error.value = OrganizationRepository.changeHead(organizationId, headRole, currentHeadId, id) {
        reloadCurrentOrganization()
      }
      removeWork(work)
    }
  }

  fun addPosition(organizationPosition: OrganizationPosition) {
    val org = organization.value!!
    val positions = org.positions.toMutableList()
    positions.add(organizationPosition.id)

    organization.value = org.copy(positions = positions)
  }
}