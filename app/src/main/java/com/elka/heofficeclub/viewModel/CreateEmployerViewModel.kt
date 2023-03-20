package com.elka.heofficeclub.viewModel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.elka.heofficeclub.other.Action
import com.elka.heofficeclub.service.model.Division
import com.elka.heofficeclub.service.model.Organization
import com.elka.heofficeclub.service.model.OrganizationPosition

class CreateEmployerViewModel(application: Application) : BaseViewModel(application) {
  private val _screen = MutableLiveData(1)
  val screen get() = _screen

  fun clear(){
    _screen.value = 1
    _externalAction.value = null
  }

  fun goNextScreen() = changeScreen(next = true)
  fun goPrevScreen() = changeScreen(next = false)


  private fun changeScreen(next: Boolean) {
    if (next && !checkFieldsCurrentScreen()) return

    val screen = _screen.value!!
    val newScreenValue = if (next) screen + 1 else screen - 1

    if (newScreenValue < 1) {
      goBack()
    } else if (newScreenValue > 4) {
      toCreateFile()
    } else {
      _screen.value = newScreenValue
    }
  }

  private fun goBack() {
    _externalAction.value = Action.GO_BACK
  }

  private fun toCreateFile() {
    _externalAction.value = Action.CREATE_FILE
  }

  private fun checkFieldsCurrentScreen(): Boolean {
    return true
  }

  fun setPositions(value: List<OrganizationPosition>) {

  }

  fun setDivisions(value: List<Division>) {

  }

  fun setOrganization(value: Organization) {

  }
}