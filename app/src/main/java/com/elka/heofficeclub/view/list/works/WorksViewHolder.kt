package com.elka.heofficeclub.view.list.works

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import com.elka.heofficeclub.databinding.WorkItemBinding
import com.elka.heofficeclub.other.documents.WorkExperience

class WorksViewHolder(
  val context: Context,
  val binding: WorkItemBinding
) : RecyclerView.ViewHolder(binding.root) {
  fun bind(workExperience: WorkExperience) {
    binding.work = workExperience
  }
}