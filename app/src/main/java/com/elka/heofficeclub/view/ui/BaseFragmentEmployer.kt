package com.elka.heofficeclub.view.ui

import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.recyclerview.widget.RecyclerView
import com.elka.heofficeclub.other.*
import com.elka.heofficeclub.other.documents.*
import com.elka.heofficeclub.viewModel.BaseViewModelEmployer
import java.util.*

abstract class BaseFragmentEmployer : BaseFragmentWithDatePicker() {
  abstract val isCreation: Boolean

  abstract val viewModel: BaseViewModelEmployer

  override val externalActionObserver = androidx.lifecycle.Observer<Action?> {
    if (it == null) return@Observer

    when (it) {
      Action.CREATE_FILE_T1 -> {
        val t1 = viewModel.newT1 ?: return@Observer
        val uri = DocumentCreator(requireContext()).createFormT1(t1)

        viewModel.saveT1(t1, uri)
      }

      Action.GO_BACK -> {
        goBack()
      }

      else -> Unit
    }
  }

  fun goNextScreen() {
    if (viewModel.screen.value == 3) viewModel.setFamilyMembers(memberAdapter.getMembers())
    viewModel.goNextScreen()
  }

  fun goBack() {
    memberAdapter.clear()
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