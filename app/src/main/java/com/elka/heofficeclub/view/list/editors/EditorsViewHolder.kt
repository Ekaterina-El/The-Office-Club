package com.elka.heofficeclub.view.list.editors

import android.content.Context
import android.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.elka.heofficeclub.R
import com.elka.heofficeclub.databinding.DivisionItemBinding
import com.elka.heofficeclub.databinding.EditorItemBinding
import com.elka.heofficeclub.other.UserStatus
import com.elka.heofficeclub.service.model.Division
import com.elka.heofficeclub.service.model.User

class EditorsViewHolder(
  val context: Context,
  val binding: EditorItemBinding,
  val listener: Listener
) : RecyclerView.ViewHolder(binding.root) {
  private var editor: User? = null

  private val menu by lazy {
    val popupMenu = PopupMenu(context, binding.wrapper)

    popupMenu.setOnMenuItemClickListener {
      when (it.itemId) {
        CHANGE_BLOCK_STATUS -> listener.onChangeBlockStatus(editor!!)
        else -> Unit
      }
      return@setOnMenuItemClickListener true
    }

    return@lazy popupMenu
  }

  fun bind(editor: User) {
    binding.editor = editor
    this.editor = editor

    binding.wrapper.setOnLongClickListener {
      menu.menu.clear()
      menu.menu.add(0, CHANGE_BLOCK_STATUS, 0, if (editor!!.status == UserStatus.UNBLOCKED) R.string.block else R.string.unblock)
      menu.show()
      return@setOnLongClickListener true
    }
  }

  companion object {
    const val CHANGE_BLOCK_STATUS = 1

    interface Listener {
      fun onChangeBlockStatus(editor: User)
    }
  }
}