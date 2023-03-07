package com.elka.heofficeclub.viewModel

import android.app.Application
import com.elka.heofficeclub.other.Action
import com.elka.heofficeclub.service.repository.UsersRepository

class SplashViewModel(application: Application) : BaseViewModel(application) {
  fun checkLoginStatus() {
    val uid = UsersRepository.currentUid
    _externalAction.value = if (uid == null) Action.GO_LOGIN else Action.GO_ORGANIZATION
  }
}