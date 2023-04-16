package com.elka.heofficeclub.view.ui.documentScreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DividerItemDecoration
import com.elka.heofficeclub.databinding.DocumentT1FragmentBinding
import com.elka.heofficeclub.databinding.DocumentT3FragmentBinding
import com.elka.heofficeclub.databinding.DocumentT5FragmentBinding
import com.elka.heofficeclub.view.list.t3.AdapterT3
import com.elka.heofficeclub.view.ui.BaseDocumentScreen
import com.elka.heofficeclub.view.ui.BaseFragmentWithOrganization
import com.elka.heofficeclub.viewModel.OrganizationDocsViewModel

class DocumentT3Fragment : BaseDocumentScreen() {
  private lateinit var binding: DocumentT3FragmentBinding
  private val t3Adapter by lazy { AdapterT3() }

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    binding = DocumentT3FragmentBinding.inflate(layoutInflater, container, false)
    binding.apply {
      lifecycleOwner = viewLifecycleOwner
      adapterT3 = this@DocumentT3Fragment.t3Adapter
    }

    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    val arg = DocumentT3FragmentArgs.fromBundle(requireArguments())
    binding.t3 = arg.t3

    t3Adapter.setItems(arg.t3.rows)


    val decorator = DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
    binding.list.addItemDecoration(decorator)
  }
}