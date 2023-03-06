package com.elka.heofficeclub.view.list.divisions

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.elka.heofficeclub.databinding.DivisionItemBinding
import com.elka.heofficeclub.service.model.Division
import com.elka.heofficeclub.view.list.BaseAdapter

class DivisionsAdapter(val listener: Listener): BaseAdapter<Division>() {
  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
    val binding = DivisionItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    return DivisionsViewHolder(parent.context, binding, listener)
  }

  override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
    val division = items[position]
    (holder as DivisionsViewHolder).bind(division)
  }

  companion object {
    interface Listener {
      fun onDelete(id: String)
    }
  }
}