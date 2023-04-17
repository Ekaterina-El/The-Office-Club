package com.elka.heofficeclub.view.ui.documentScreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.elka.heofficeclub.databinding.DocumentT11FragmentBinding
import com.elka.heofficeclub.other.documents.FormType
import com.elka.heofficeclub.view.ui.BaseDocumentScreen

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
      master = this@DocumentT11Fragment
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
    downloadFileByUrl(url, t11.dataCreated, FormType.T11, fullName)
  }
}