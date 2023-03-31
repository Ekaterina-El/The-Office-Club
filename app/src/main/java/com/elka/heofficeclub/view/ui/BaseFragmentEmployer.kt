package com.elka.heofficeclub.view.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.activityViewModels
import com.elka.heofficeclub.R
import com.elka.heofficeclub.databinding.EmployerFragmentBinding
import com.elka.heofficeclub.other.Action
import com.elka.heofficeclub.other.documents.DocumentCreator
import com.elka.heofficeclub.viewModel.EmployerViewModel

abstract class BaseFragmentEmployer : BaseFragmentWithDatePicker() {
  abstract val isCreation: Boolean
  private lateinit var binding: EmployerFragmentBinding
  protected val viewModel by activityViewModels<EmployerViewModel>()

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
  ): View {
    binding = EmployerFragmentBinding.inflate(layoutInflater, container, false)
    binding.apply {
      lifecycleOwner = viewLifecycleOwner
      master = this@BaseFragmentEmployer
      viewModel = this@BaseFragmentEmployer.viewModel
    }
    return binding.root
  }


  override val externalActionObserver = androidx.lifecycle.Observer<Action?> {
    if (it == null) return@Observer

    when (it) {
      Action.CREATE_FILE_T1 -> {
        val t1 = viewModel.newT1 ?: return@Observer
        val uri = DocumentCreator(requireContext()).createFormT1(t1)
        viewModel.saveT1(t1, uri)
      }

      Action.GO_BACK -> goBack()

      else -> Unit
    }
  }

  fun goPrevScreen() {
    viewModel.goPrevScreen()
  }

  fun goNextScreen() {
    viewModel.goNextScreen()
  }

  private fun goBack() {
    viewModel.clear()
    organizationViewModel.setBottomMenuStatus(true)
    navController.popBackStack()
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    viewModel.setViewStatus(isCreation)

    organizationViewModel.setBottomMenuStatus(false)
    activity.onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
      override fun handleOnBackPressed() {
        viewModel.goPrevScreen()
      }
    })
  }

  override fun onResume() {
    super.onResume()
    viewModel.error.observe(this, errorObserver)
    viewModel.externalAction.observe(this, externalActionObserver)
    viewModel.work.observe(this, workObserver)
  }

  override fun onStop() {
    super.onStop()
    viewModel.error.removeObserver(errorObserver)
    viewModel.externalAction.removeObserver(externalActionObserver)
    viewModel.work.removeObserver(workObserver)
  }
}