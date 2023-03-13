package com.elka.heofficeclub.view.list.organizationPositions

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.elka.heofficeclub.databinding.PositionItemBinding
import com.elka.heofficeclub.service.model.OrganizationPosition
import com.elka.heofficeclub.view.list.BaseAdapter

class OrgPositionsAdapter() : BaseAdapter<OrganizationPosition>() {
  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
    val binding = PositionItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    return OrgPositionsViewHolder(parent.context, binding)
  }

  override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
    val item = items[position]
    (holder as OrgPositionsViewHolder).bind(item)
  }
}