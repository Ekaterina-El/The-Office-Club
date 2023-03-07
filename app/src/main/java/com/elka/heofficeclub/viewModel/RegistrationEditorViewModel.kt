package com.elka.heofficeclub.viewModel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.elka.heofficeclub.other.*
import com.elka.heofficeclub.service.model.User
import com.elka.heofficeclub.service.repository.OrganizationRepository
import com.elka.heofficeclub.service.repository.UsersRepository
import kotlinx.coroutines.launch

class RegistrationEditorViewModel(application: Application) : BaseViewModelWithFields(application) {
  val fullName = MutableLiveData("")
  val email = MutableLiveData("")

  override val fields: HashMap<Field, MutableLiveData<String>> = hashMapOf(
    Pair(Field.EMAIL, email),
    Pair(Field.FULL_NAME, fullName)
  )

  fun tryRegistration() {
    if (checkFields()) registration()
  }

  private val newEditor
    get() = User(
      fullName = fullName.value!!,
      email = email.value!!,
      organizationId = _organizationId.value!!,
      role = Role.EDITOR
    )

  private var currentUserEmail: String? = null
  private var currentUserPassword: String? = null
  fun setCurrentUserCredentials(email: String, password: String) {
    currentUserEmail = email
    currentUserPassword = password
  }

  var addedEditor: User? = null
  var password: String? = null

  private fun registration() {
    val work = Work.REGISTRATION_EDITOR
    addWork(work)

    viewModelScope.launch {
      val editor = newEditor
      password = Generator.genPassword()
      _error.value = UsersRepository.registrationUser(editor.email, password!!) { uid ->
        editor.id = uid
        _error.value = UsersRepository.addUser(editor) {
          _error.value = OrganizationRepository.addEditor(editor.organizationId, uid) {
            addedEditor = editor
            _externalAction.value = Action.EDITOR_ADDED
          }
        }
      }

      UsersRepository.logout {
        _error.value = UsersRepository.login(currentUserEmail!!, currentUserPassword!!) {}
      }

      removeWork(work)
    }
  }

  private val _organizationId = MutableLiveData<String?>(null)
  fun setOrganizationId(organizationId: String) {
    _organizationId.value = organizationId
  }

  fun clear() {
    _organizationId.value
    _fieldErrors.value = listOf()
    _error.value = null
    addedEditor = null
    _externalAction.value = null
    password = null
    email.value = ""
    fullName.value = ""
    clearWork()
  }
}