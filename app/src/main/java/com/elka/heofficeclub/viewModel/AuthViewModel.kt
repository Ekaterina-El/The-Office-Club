package com.elka.heofficeclub.viewModel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.elka.heofficeclub.other.*
import com.elka.heofficeclub.service.repository.UsersRepository
import kotlinx.coroutines.launch

class AuthViewModel(application: Application) : BaseViewModelWithFields(application) {
  override val fields by lazy {
    hashMapOf<Field, MutableLiveData<Any?>>(
      Pair(Field.EMAIL, email as MutableLiveData<Any?>),
      Pair(Field.PASSWORD, password as MutableLiveData<Any?>),
    )
  }

  val email = MutableLiveData("")
  val password = MutableLiveData("")


  fun tryLogin() {
    if (checkFields()) login()
  }

  private fun login() {
    val work = Work.LOGIN
    addWork(work)

    viewModelScope.launch {
      _error.value = UsersRepository.login(email.value!!, password.value!!) {
        _externalAction.value = Action.LOAD_PROFILE
      }
      removeWork(work)
    }
  }

  fun clearFields() {
    clearWork()
    _error.value = null
    _fieldErrors.value = listOf()
    _externalAction.value = null
    email.value = ""
    password.value = ""
  }


}