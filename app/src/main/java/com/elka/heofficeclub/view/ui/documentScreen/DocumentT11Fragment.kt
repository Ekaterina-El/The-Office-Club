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
import com.elka.heofficeclub.view.ui.BaseDocumentScreen
import com.elka.heofficeclub.view.ui.BaseFragmentWithOrganization
import com.elka.heofficeclub.viewModel.OrganizationDocsViewModel
import java.util.*

class DocumentT11Fragment : BaseDocumentScreen() {
  private lateinit var binding: DocumentT11FragmentBinding

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    binding = DocumentT11FragmentBinding.inflate(layoutInflater, container, false)
    binding.apply {
      lifecycleOwner = viewLifecycleOwner
    }

    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    val arg = DocumentT11FragmentArgs.fromBundle(requireArguments())
    binding.t11 = arg.t11
  }

  override fun download() {
    val t11 = binding.t11 ?: return
    val url = t11.fileUrl ?: return
    val fullName = t11.employer?.T2Local?.fullName ?: return
    val fileName = getString(R.string.t11_title, fullName) + "_${Calendar.getInstance().time}"
    downloadFileByUrl(url, fileName)
  }
}