package com.elka.heofficeclub.view.ui.documentScreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.activityViewModels
import com.elka.heofficeclub.R
import com.elka.heofficeclub.databinding.DocumentT11FragmentBinding
import com.elka.heofficeclub.databinding.DocumentT1FragmentBinding
import com.elka.heofficeclub.databinding.DocumentT5FragmentBinding
import com.elka.heofficeclub.databinding.DocumentT6FragmentBinding
import com.elka.heofficeclub.view.ui.BaseDocumentScreen
import com.elka.heofficeclub.view.ui.BaseFragmentWithOrganization
import com.elka.heofficeclub.viewModel.OrganizationDocsViewModel
import java.util.*

class DocumentT6Fragment : BaseDocumentScreen() {
  private lateinit var binding: DocumentT6FragmentBinding

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    binding = DocumentT6FragmentBinding.inflate(layoutInflater, container, false)
    binding.apply {
      lifecycleOwner = viewLifecycleOwner
    }

    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    val arg = DocumentT6FragmentArgs.fromBundle(requireArguments())
    binding.t6 = arg.t6
  }

  override fun download() {
    val t6 = binding.t6 ?: return
    val url = t6.fileUrl ?: return
    val fullName = t6.employer?.T2Local?.fullName ?: return
    val fileName = getString(R.string.t6_title, fullName) + "_${Calendar.getInstance().time}"
    downloadFileByUrl(url, fileName)
  }
}