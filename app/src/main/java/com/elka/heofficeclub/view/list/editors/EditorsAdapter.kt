package com.elka.heofficeclub.view.list.editors

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.elka.heofficeclub.databinding.EditorItemBinding
import com.elka.heofficeclub.service.model.User
import com.elka.heofficeclub.view.list.BaseAdapter

class EditorsAdapter(val listener: EditorsViewHolder.Companion.Listener) : BaseAdapter<User>() {
  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
    val binding = EditorItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    return EditorsViewHolder(parent.context, binding, listener)
  }

  override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
    val item = items[position]
    (holder as EditorsViewHolder).bind(item)
  }
}