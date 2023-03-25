package com.elka.heofficeclub.view.ui.employerScreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Spinner
import androidx.activity.OnBackPressedCallback
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.elka.heofficeclub.R
import com.elka.heofficeclub.databinding.EmployerFragmentBinding
import com.elka.heofficeclub.databinding.WelcomeFragmentBinding
import com.elka.heofficeclub.other.Action
import com.elka.heofficeclub.other.Field
import com.elka.heofficeclub.service.model.Employer
import com.elka.heofficeclub.view.ui.BaseFragment
import com.elka.heofficeclub.view.ui.BaseFragmentEmployer
import com.elka.heofficeclub.view.ui.BaseFragmentWithOrganization
import com.elka.heofficeclub.viewModel.BaseViewModelEmployer
import com.elka.heofficeclub.viewModel.EmployerViewModel
import java.util.HashMap

class EmployerFragment: BaseFragmentEmployer() {
    private lateinit var binding: EmployerFragmentBinding
    override lateinit var viewModel: BaseViewModelEmployer
    override val fields: HashMap<Field, Any> = hashMapOf()

    override lateinit var positionSpinner: Spinner
    override lateinit var genderSpinner: Spinner
    override lateinit var educationTypeSpinner: Spinner
    override lateinit var postgEducationTypeSpinner: Spinner
    override lateinit var merriedStatusSpinner: Spinner
    override lateinit var divisionsSpinner: Spinner
    override lateinit var recyclerViewMembers: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this)[EmployerViewModel::class.java]
        binding = EmployerFragmentBinding.inflate(layoutInflater, container, false)
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            master = this@EmployerFragment
            viewModel = this@EmployerFragment.viewModel as EmployerViewModel
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args = EmployerFragmentArgs.fromBundle(requireArguments())
        viewModel.setEmployer(args.employer)
    }
}