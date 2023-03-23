package com.elka.heofficeclub.view.list.employees

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.elka.heofficeclub.databinding.EditorItemBinding
import com.elka.heofficeclub.databinding.EmployeesItemBinding
import com.elka.heofficeclub.service.model.Employer
import com.elka.heofficeclub.service.model.User
import com.elka.heofficeclub.view.list.BaseAdapter

class EmployeesAdapter(val listener: EmployeesViewHolder.Companion.Listener) : BaseAdapter<Employer>() {

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
    val binding = EmployeesItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    return EmployeesViewHolder(parent.context, binding, listener)
  }

  override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
    val item = items[position]
    (holder as EmployeesViewHolder).bind(item)
  }
}