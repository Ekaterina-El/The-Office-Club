package com.elka.heofficeclub.view.ui.documentScreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.elka.heofficeclub.databinding.DocumentT8FragmentBinding
import com.elka.heofficeclub.other.documents.FormType
import com.elka.heofficeclub.view.ui.BaseDocumentScreen

class DocumentT8Fragment : BaseDocumentScreen() {
  private lateinit var binding: DocumentT8FragmentBinding

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    binding = DocumentT8FragmentBinding.inflate(layoutInflater, container, false)
    binding.apply {
      lifecycleOwner = viewLifecycleOwner
      master = this@DocumentT8Fragment
    }

    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    val arg = DocumentT8FragmentArgs.fromBundle(requireArguments())
    binding.t8 = arg.t8
  }

  override fun download() {
    val t8 = binding.t8 ?: return
    val url = t8.fileUrl ?: return
    val fullName = t8.employer.T2Local?.fullName ?: return
    downloadFileByUrl(url, t8.dataCreated, FormType.T8, fullName)
  }
}