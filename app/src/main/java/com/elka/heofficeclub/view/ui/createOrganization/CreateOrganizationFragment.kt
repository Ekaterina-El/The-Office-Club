package com.elka.heofficeclub.view.ui.createOrganization

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.elka.heofficeclub.databinding.CreateOrganizationFragmentBinding
import com.elka.heofficeclub.other.Field
import com.elka.heofficeclub.other.FieldError
import com.elka.heofficeclub.view.ui.BaseFragment
import com.elka.heofficeclub.viewModel.CreateOrganizationViewModel

class CreateOrganizationFragment: BaseFragment() {
    private lateinit var binding: CreateOrganizationFragmentBinding
    private lateinit var viewModel: CreateOrganizationViewModel

    private var fieldErrorsObserver = Observer<List<FieldError>> { errors ->
        showErrors(errors)
    }

    private fun showErrors(errors: List<FieldError>) {
        binding.layoutFullName.error = ""
        binding.layoutShortName.error = ""
        binding.layoutNameOfOrganizationHead.error = ""
        binding.layoutNameOfHumanResourcesDepartmentHead.error = ""
        binding.layoutCity.error = ""
        binding.layoutStreet.error = ""
        binding.layoutHouse.error = ""
        binding.layoutBuilding.error = ""
        binding.layoutPostcode.error = ""
        binding.layoutEmail.error = ""
        binding.layoutPassword.error = ""

        if (errors.isEmpty()) return

        for (error in errors) {
            val field = when (error.field) {
                Field.FULL_NAME -> binding.layoutFullName
                Field.SHORT_NAME -> binding.layoutShortName
                Field.NAME_OF_HUMAN_RESOURCES_DEPARTMENT_HEAD -> binding.layoutNameOfHumanResourcesDepartmentHead
                Field.NAME_OF_ORGANIZATION_HEAD -> binding.layoutNameOfOrganizationHead
                Field.CITY -> binding.layoutCity
                Field.STREET -> binding.layoutStreet
                Field.HOUSE -> binding.layoutHouse
                Field.BUILDING -> binding.layoutBuilding
                Field.POSTCODE -> binding.layoutPostcode
                Field.EMAIL -> binding.layoutEmail
                Field.PASSWORD -> binding.layoutPassword
                else -> return
            }
            field.error = getString(error.errorType!!.messageRes)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this)[CreateOrganizationViewModel::class.java]

        binding = CreateOrganizationFragmentBinding.inflate(layoutInflater, container, false)
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            master = this@CreateOrganizationFragment
            viewModel = this@CreateOrganizationFragment.viewModel
        }

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        viewModel.fieldErrors.observe(viewLifecycleOwner, fieldErrorsObserver)
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.fieldErrors.removeObserver(fieldErrorsObserver)
    }

    fun goBack() {
        navController.popBackStack()
        viewModel.clearFields()
    }

    fun tryRegistration() {
        viewModel.tryCreateOrganization()
    }
}