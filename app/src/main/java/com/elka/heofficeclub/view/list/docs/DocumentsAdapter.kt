package com.elka.heofficeclub.view.list.docs

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.elka.heofficeclub.databinding.DocsItemBinding
import com.elka.heofficeclub.service.model.Employer
import com.elka.heofficeclub.service.model.documents.forms.DocForm
import com.elka.heofficeclub.view.list.BaseAdapter
import com.elka.heofficeclub.view.list.employees.EmployeesViewHolder

class DocumentsAdapter(val listener: DocumentViewHolder.Companion.Listener) : BaseAdapter<DocForm>() {

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
    val binding = DocsItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    return DocumentViewHolder(parent.context, binding, listener)
  }

  override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
    val item = items[position]
    (holder as DocumentViewHolder).bind(item)
  }
}