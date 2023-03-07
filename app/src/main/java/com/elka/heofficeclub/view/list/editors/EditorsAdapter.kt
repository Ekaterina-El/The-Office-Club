package com.elka.heofficeclub.view.list.editors

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.elka.heofficeclub.databinding.EditorItemBinding
import com.elka.heofficeclub.other.Role
import com.elka.heofficeclub.service.model.User
import com.elka.heofficeclub.view.list.BaseAdapter

class EditorsAdapter(val listener: EditorsViewHolder.Companion.Listener) : BaseAdapter<User>() {
  private val holders = arrayListOf<EditorsViewHolder>()
  private var viewerRole = Role.EDITOR

  fun updateViewerRole(role: Role) {
    viewerRole = role
    holders.forEach { it.setViewerRole(role) }
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
    val binding = EditorItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    val holder = EditorsViewHolder(parent.context, binding, listener)
    holders.add(holder)
    return holder
  }

  override fun setItems(newItems: List<User>) {
    holders.clear()
    super.setItems(newItems)
  }

  override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
    val item = items[position]
    (holder as EditorsViewHolder).bind(item, viewerRole)
  }
}