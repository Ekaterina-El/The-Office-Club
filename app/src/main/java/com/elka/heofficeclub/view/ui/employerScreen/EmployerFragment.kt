package com.elka.heofficeclub.view.ui.employerScreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.elka.heofficeclub.databinding.EmployerFragmentBinding
import com.elka.heofficeclub.view.ui.BaseFragmentEmployer
import com.elka.heofficeclub.viewModel.EmployerViewModel

class EmployerFragment : BaseFragmentEmployer() {
  override val isCreation = false
  private lateinit var binding: EmployerFragmentBinding
  override val viewModel by activityViewModels<EmployerViewModel>()

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {


    binding = EmployerFragmentBinding.inflate(layoutInflater, container, false)
    binding.apply {
      lifecycleOwner = viewLifecycleOwner
      master = this@EmployerFragment
      viewModel = this@EmployerFragment.viewModel
    }

    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    val args = EmployerFragmentArgs.fromBundle(requireArguments())
    viewModel.setEmployer(args.employer)
  }
}