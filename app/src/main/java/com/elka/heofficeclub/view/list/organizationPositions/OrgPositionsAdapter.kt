package com.elka.heofficeclub.view.list.organizationPositions

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.elka.heofficeclub.databinding.PositionItemBinding
import com.elka.heofficeclub.other.Role
import com.elka.heofficeclub.service.model.OrganizationPosition
import com.elka.heofficeclub.service.model.User
import com.elka.heofficeclub.view.list.BaseAdapter
import com.elka.heofficeclub.view.list.editors.EditorsViewHolder

class OrgPositionsAdapter(val listener: OrgPositionsViewHolder.Companion.Listener) : BaseAdapter<OrganizationPosition>() {

  private val holders = arrayListOf<OrgPositionsViewHolder>()
  private var viewerRole = Role.EDITOR

  fun updateViewerRole(role: Role) {
    viewerRole = role
    holders.forEach { it.setViewerRole(role) }
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
    val binding = PositionItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    val holder = OrgPositionsViewHolder(parent.context, binding, listener)
    holders.add(holder)
    return holder
  }

  override fun setItems(newItems: List<OrganizationPosition>) {
    holders.clear()
    super.setItems(newItems)
  }

  override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
    val item = items[position]
    (holder as OrgPositionsViewHolder).bind(item, viewerRole)
  }
}