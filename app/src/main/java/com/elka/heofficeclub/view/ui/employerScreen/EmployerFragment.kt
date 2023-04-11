package com.elka.heofficeclub.view.ui.employerScreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.elka.heofficeclub.databinding.EmployerFragmentBinding
import com.elka.heofficeclub.view.ui.BaseFragmentEmployer
import com.elka.heofficeclub.viewModel.EmployerViewModel

class EmployerFragment : BaseFragmentEmployer() {
  override val isCreation = false

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    val args = EmployerFragmentArgs.fromBundle(requireArguments())
    viewModel.setEmployer(args.employer)
  }
}