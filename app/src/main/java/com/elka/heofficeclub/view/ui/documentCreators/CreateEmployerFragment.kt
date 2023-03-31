package com.elka.heofficeclub.view.ui.documentCreators

import android.os.Bundle
import android.view.View
import com.elka.heofficeclub.service.model.Employer
import com.elka.heofficeclub.view.ui.BaseFragmentEmployer

class CreateEmployerFragment : BaseFragmentEmployer() {
  override val isCreation = true
  
  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    viewModel.setEmployer(Employer())
  }
}
