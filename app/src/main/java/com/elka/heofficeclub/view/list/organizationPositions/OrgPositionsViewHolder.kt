package com.elka.heofficeclub.view.list.organizationPositions

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import com.elka.heofficeclub.databinding.PositionItemBinding
import com.elka.heofficeclub.service.model.OrganizationPosition

class OrgPositionsViewHolder(val context: Context, val binding: PositionItemBinding) :
  RecyclerView.ViewHolder(binding.root) {
  private var position: OrganizationPosition? = null

  fun bind(position: OrganizationPosition) {
    binding.position = position
    this.position = position
  }
}