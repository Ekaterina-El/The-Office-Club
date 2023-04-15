package com.elka.heofficeclub.view.list.docs

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import com.elka.heofficeclub.databinding.DocsItemBinding
import com.elka.heofficeclub.service.model.documents.forms.DocForm

class DocumentViewHolder(
  val context: Context,
  val binding: DocsItemBinding,
  val listener: Listener
) : RecyclerView.ViewHolder(binding.root) {
  private var docForm: DocForm? = null

  fun bind(docForm: DocForm) {
    binding.docForm = docForm
    this.docForm = docForm

    binding.wrapper.setOnClickListener {
      listener.onSelect(docForm)
    }
  }


  companion object {

    interface Listener {
      fun onSelect(docForm: DocForm)
    }
  }
}