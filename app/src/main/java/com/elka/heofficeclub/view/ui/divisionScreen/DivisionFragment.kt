package com.elka.heofficeclub.view.ui.divisionScreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.elka.heofficeclub.R
import com.elka.heofficeclub.databinding.DivisionFragmentBinding
import com.elka.heofficeclub.other.Work
import com.elka.heofficeclub.view.ui.BaseFragmentWithOrganization
import com.elka.heofficeclub.viewModel.DivisionViewModel

class DivisionFragment : BaseFragmentWithOrganization() {
  private lateinit var binding: DivisionFragmentBinding
  private val divisionViewModel by activityViewModels<DivisionViewModel>()

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    binding = DivisionFragmentBinding.inflate(layoutInflater, container, false)
    binding.apply {
      lifecycleOwner = viewLifecycleOwner
      viewModel = this@DivisionFragment.divisionViewModel
    }

    return binding.root
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    organizationViewModel.setBottomMenuStatus(false)
    activity.onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
      override fun handleOnBackPressed() {
        goBack()
      }
    })
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    val arg = DivisionFragmentArgs.fromBundle(requireArguments())
    divisionViewModel.setDivision(arg.division)

    val navController =
      (childFragmentManager.findFragmentById(R.id.divisionContainer) as NavHostFragment)
        .navController
    binding.bottomMenu.setupWithNavController(navController)
  }

  fun goBack() {
    divisionViewModel.clear()
    organizationViewModel.setBottomMenuStatus(true)
    navController.popBackStack()
  }

  override fun onResume() {
    super.onResume()
    organizationViewModel.error.observe(viewLifecycleOwner, errorObserver)
    divisionViewModel.error.observe(viewLifecycleOwner, errorObserver)

  }

  override fun onStop() {
    super.onStop()
    organizationViewModel.error.removeObserver(errorObserver)
    divisionViewModel.error.removeObserver(errorObserver)
  }
}