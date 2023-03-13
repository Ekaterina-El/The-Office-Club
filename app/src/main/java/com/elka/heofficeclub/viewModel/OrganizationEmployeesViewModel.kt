package com.elka.heofficeclub.viewModel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.elka.heofficeclub.other.Work
import com.elka.heofficeclub.service.model.Organization
import com.elka.heofficeclub.service.model.OrganizationPosition
import com.elka.heofficeclub.service.model.filterBy
import com.elka.heofficeclub.service.repository.OrganizationPositionRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class OrganizationEmployeesViewModel(application: Application) : BaseViewModel(application) {
  private val _orgPositions = MutableLiveData<List<OrganizationPosition>>(listOf())

  private val _orgPositionsFiltered = MutableLiveData<List<OrganizationPosition>>(listOf())
  val orgPositionsFiltered get() = _orgPositionsFiltered

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
        filterPositions()
      }
      removeWork(work)
    }
  }

  // region Search
  val searchPositions = MutableLiveData("")

  fun filterPositions() {
    val positions = _orgPositions.value!!
    val search = searchPositions.value!!

    _orgPositionsFiltered.value = when(search) {
      "" -> positions
      else -> positions.filterBy(search)
    }
  }

  fun clearPositionSearch() {
    searchPositions.value = ""
    filterPositions()
  }
  // endregion
}