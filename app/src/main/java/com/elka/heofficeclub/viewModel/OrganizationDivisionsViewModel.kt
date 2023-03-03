package com.elka.heofficeclub.viewModel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.elka.heofficeclub.other.Work
import com.elka.heofficeclub.service.model.Division
import kotlinx.coroutines.launch

class OrganizationDivisionsViewModel(application: Application) : BaseViewModel(application) {
  private val _divisions = MutableLiveData<List<Division>>(listOf())
  val division get() = _divisions

  fun loadDivisions() {

  }

  fun addDivision(division: Division) {
    val work = Work.ADD_DIVISION
    addWork(work)

    viewModelScope.launch {
      removeWork(work)
    }
  }
}