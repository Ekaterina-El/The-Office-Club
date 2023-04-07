package com.elka.heofficeclub.view.ui.divisionScreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.elka.heofficeclub.R
import com.elka.heofficeclub.databinding.DivisionFragmentBinding
import com.elka.heofficeclub.databinding.WelcomeFragmentBinding
import com.elka.heofficeclub.other.Work
import com.elka.heofficeclub.view.ui.BaseFragment
import com.elka.heofficeclub.view.ui.BaseFragmentWithOrganization
import com.elka.heofficeclub.viewModel.DivisionViewModel

class DivisionFragment : BaseFragmentWithOrganization() {
  private lateinit var binding: DivisionFragmentBinding
  private val divisionViewModel by activityViewModels<DivisionViewModel>()

  private val works = listOf(
    Work.LOAD_EMPLOYERS
  )


  private val hasLoads: Boolean
    get() {
      val w = organizationViewModel.work.value!!.toMutableList()
      val w1 = divisionViewModel.work.value!!
      w.addAll(w1)

      return when {
        w.isEmpty() -> false
        else -> w.map { item -> if (works.contains(item)) 1 else 0 }
          .reduce { a, b -> a + b } > 0
      }
    }

  override val workObserver = Observer<List<Work>> {
//    binding.swiper.isRefreshing = hasLoads
  }



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
  }

  fun goBack() {
    divisionViewModel.clear()
    organizationViewModel.setBottomMenuStatus(true)
    navController.popBackStack()
  }

  override fun onResume() {
    super.onResume()
    organizationViewModel.work.observe(this, workObserver)
    divisionViewModel.work.observe(this, workObserver)
  }

  override fun onStop() {
    super.onStop()
    organizationViewModel.work.removeObserver(workObserver)
    divisionViewModel.work.removeObserver(workObserver)
  }
}