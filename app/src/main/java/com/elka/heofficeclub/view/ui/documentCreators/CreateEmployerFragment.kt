package com.elka.heofficeclub.view.ui.documentCreators

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import com.elka.heofficeclub.other.Action
import com.elka.heofficeclub.service.model.Employer
import com.elka.heofficeclub.view.ui.BaseFragmentEmployer

class CreateEmployerFragment : BaseFragmentEmployer() {
  override val isCreation = true

  override val externalActionObserver = Observer<Action?> {
    if (it == null) return@Observer

    if (it == Action.GO_HOME) {
      viewModel.clear()
      navController.popBackStack()
    }
  }

  override fun onResume() {
    super.onResume()
    viewModel.externalAction.observe(this, externalActionObserver)
  }

  override fun onStop() {
    super.onStop()
    viewModel.externalAction.removeObserver(externalActionObserver)
  }
  
  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    viewModel.setEmployer(Employer())
  }
}
