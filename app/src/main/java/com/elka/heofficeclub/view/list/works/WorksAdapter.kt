package com.elka.heofficeclub.view.list.works

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.elka.heofficeclub.databinding.WorkItemBinding
import com.elka.heofficeclub.other.documents.WorkExperience
import com.elka.heofficeclub.view.list.BaseAdapter

class WorksAdapter : BaseAdapter<WorkExperience>() {
  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
    val binding = WorkItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    return WorksViewHolder(parent.context, binding)
  }

  override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
    val item = items[position]
    (holder as WorksViewHolder).bind(item)
  }
}