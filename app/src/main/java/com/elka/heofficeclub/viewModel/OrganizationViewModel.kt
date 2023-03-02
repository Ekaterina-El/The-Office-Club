package com.elka.heofficeclub.viewModel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.elka.heofficeclub.other.Work
import com.elka.heofficeclub.service.model.User
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
}