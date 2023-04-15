package com.elka.heofficeclub.view.ui.documentScreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.activityViewModels
import com.elka.heofficeclub.databinding.DocumentT1FragmentBinding
import com.elka.heofficeclub.databinding.DocumentT5FragmentBinding
import com.elka.heofficeclub.view.ui.BaseDocumentScreen
import com.elka.heofficeclub.view.ui.BaseFragmentWithOrganization
import com.elka.heofficeclub.viewModel.OrganizationDocsViewModel

class DocumentT5Fragment : BaseDocumentScreen() {
  private lateinit var binding: DocumentT5FragmentBinding

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    binding = DocumentT5FragmentBinding.inflate(layoutInflater, container, false)
    binding.apply {
      lifecycleOwner = viewLifecycleOwner
    }

    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    val arg = DocumentT5FragmentArgs.fromBundle(requireArguments())
    binding.t5 = arg.t5
  }
}