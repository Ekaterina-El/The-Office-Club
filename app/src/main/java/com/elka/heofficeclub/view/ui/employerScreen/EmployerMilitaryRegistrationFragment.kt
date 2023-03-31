package com.elka.heofficeclub.view.ui.employerScreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.elka.heofficeclub.databinding.EmployerMilitaryRegistrationBinding
import com.elka.heofficeclub.view.ui.BaseEmployerFragment

class EmployerMilitaryRegistrationFragment : BaseEmployerFragment() {
  override val currentScreen: Int = 4
  private lateinit var binding: EmployerMilitaryRegistrationBinding

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    binding = EmployerMilitaryRegistrationBinding.inflate(layoutInflater, container, false)
    binding.apply {
      lifecycleOwner = viewLifecycleOwner
      master = this@EmployerMilitaryRegistrationFragment
      viewModel = this@EmployerMilitaryRegistrationFragment.viewModel
    }

    return binding.root
  }
}