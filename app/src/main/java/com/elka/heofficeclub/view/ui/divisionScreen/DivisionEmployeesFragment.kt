package com.elka.heofficeclub.view.ui.divisionScreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.elka.heofficeclub.databinding.DivisionEmployeesFragmentBinding
import com.elka.heofficeclub.other.Work
import com.elka.heofficeclub.view.ui.BaseFragmentWithOrganization
import com.elka.heofficeclub.viewModel.DivisionViewModel

class DivisionEmployeesFragment : BaseFragmentWithOrganization() {
  private lateinit var binding: DivisionEmployeesFragmentBinding
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
        else -> w.map { item -> if (works.contains(item)) 1 else 0 }.reduce { a, b -> a + b } > 0
      }
    }

  override val workObserver = Observer<List<Work>> {
//    binding.swiper.isRefreshing = hasLoads
  }

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
  ): View {
    binding = DivisionEmployeesFragmentBinding.inflate(layoutInflater, container, false)
    binding.apply {
      lifecycleOwner = viewLifecycleOwner
      viewModel = this@DivisionEmployeesFragment.divisionViewModel
    }

    return binding.root
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