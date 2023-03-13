package com.elka.heofficeclub.view.list.organizationPositions

import android.content.Context
import android.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.elka.heofficeclub.R
import com.elka.heofficeclub.databinding.PositionItemBinding
import com.elka.heofficeclub.other.Constants
import com.elka.heofficeclub.other.Role
import com.elka.heofficeclub.other.UserStatus
import com.elka.heofficeclub.service.model.OrganizationPosition
import com.elka.heofficeclub.service.model.User
import com.elka.heofficeclub.view.list.editors.EditorsViewHolder

class OrgPositionsViewHolder(
  val context: Context,
  val binding: PositionItemBinding,
  private val listener: Listener
) :
  RecyclerView.ViewHolder(binding.root) {
  private var position: OrganizationPosition? = null

  fun setViewerRole(role: Role) {
    binding.role = role
  }

  private val menu by lazy {
    val popupMenu = PopupMenu(context, binding.wrapper)

    popupMenu.setOnMenuItemClickListener {
      when (it.itemId) {
        DELETE_POSITION -> listener.onDeletePosition(position!!)
        else -> Unit
      }
      return@setOnMenuItemClickListener true
    }

    return@lazy popupMenu
  }

  fun bind(position: OrganizationPosition, viewerRole: Role) {
    binding.position = position
    this.position = position
    setViewerRole(viewerRole)

    binding.wrapper.setOnLongClickListener {
      val viewerRole = binding.role
      val canChange = Constants.rolesChangePositions.contains(viewerRole)

      if (canChange) {
        menu.menu.clear()
        menu.menu.add(0, DELETE_POSITION, 0, R.string.delete)
        menu.show()
      }

      return@setOnLongClickListener true
    }
  }

  companion object {
    const val DELETE_POSITION = 1

    interface Listener {
      fun onDeletePosition(position: OrganizationPosition)
    }
  }
}