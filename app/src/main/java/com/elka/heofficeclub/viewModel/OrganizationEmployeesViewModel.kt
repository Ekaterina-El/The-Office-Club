package com.elka.heofficeclub.viewModel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.elka.heofficeclub.other.Work
import com.elka.heofficeclub.service.model.Organization
import com.elka.heofficeclub.service.model.OrganizationPosition
import com.elka.heofficeclub.service.repository.OrganizationPositionRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class OrganizationEmployeesViewModel(application: Application) : BaseViewModel(application) {
  private val _orgPositions = MutableLiveData<List<OrganizationPosition>>(listOf())
  val orgPositions get() = _orgPositions

  fun setOrganization(organization: Organization) {
    loadPositions(organization.positions)
  }

  private fun loadPositions(positionsId: List<String>) {
    val work = Work.LOAD_POSITIONS
    addWork(work)

    viewModelScope.launch {
      delay(1000)
      _error.value = OrganizationPositionRepository.loadPositions(positionsId) { positions ->
        _orgPositions.value = positions
      }
      removeWork(work)
    }
  }
}