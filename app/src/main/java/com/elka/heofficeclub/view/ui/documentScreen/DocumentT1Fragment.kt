package com.elka.heofficeclub.view.ui.documentScreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.activityViewModels
import com.elka.heofficeclub.R
import com.elka.heofficeclub.databinding.DocumentT1FragmentBinding
import com.elka.heofficeclub.other.documents.FormType
import com.elka.heofficeclub.view.ui.BaseDocumentScreen
import com.elka.heofficeclub.view.ui.BaseFragmentWithOrganization
import com.elka.heofficeclub.viewModel.OrganizationDocsViewModel
import java.util.Calendar

class DocumentT1Fragment : BaseDocumentScreen() {
  private lateinit var binding: DocumentT1FragmentBinding

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    binding = DocumentT1FragmentBinding.inflate(layoutInflater, container, false)
    binding.apply {
      lifecycleOwner = viewLifecycleOwner
      master = this@DocumentT1Fragment
    }

    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    val arg = DocumentT1FragmentArgs.fromBundle(requireArguments())
    binding.t1 = arg.t1
  }

  override fun download() {
    val t1 = binding.t1 ?: return
    val url = t1.fileUrl ?: return
    downloadFileByUrl(url, t1.dataCreated, FormType.T1, t1.fullName)
  }
}