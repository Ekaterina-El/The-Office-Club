package com.elka.heofficeclub.view.list.users

import android.content.Context
import android.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.elka.heofficeclub.R
import com.elka.heofficeclub.databinding.MemberItemBinding
import com.elka.heofficeclub.other.documents.Member

class MemberViewHolder(
  val context: Context,
  val binding: MemberItemBinding,
  val listener: Listener
) : RecyclerView.ViewHolder(binding.root) {
  private var member: Member? = null
  private var pos: Int = 0

  private val menu by lazy {
    val popupMenu = PopupMenu(context, binding.wrapper)
    popupMenu.menu.add(0, DELETE, 0, R.string.delete)
    popupMenu.show()

    popupMenu.setOnMenuItemClickListener {
      when (it.itemId) {
        DELETE -> listener.onDelete(pos)
        else -> Unit
      }
      return@setOnMenuItemClickListener true
    }

    return@lazy popupMenu
  }

  fun bind(member: Member, isDismissedEmployer: Boolean, adapterPosition: Int) {
    binding.member = member
    binding.isDismissedEmployer = isDismissedEmployer
    this.member = member
    this.pos = adapterPosition

    binding.wrapper.setOnLongClickListener {
      menu.show()
      return@setOnLongClickListener true
    }
  }

  fun getMemberDate() = Member(
    fullName = binding.teFullName.text.toString(),
    degreeOfKinship = binding.teRole.text.toString(),
    yearOfBirthday = binding.teYear.text.toString(),
  )

  companion object {
    const val DELETE = 1

    interface Listener {
      fun onDelete(pos: Int)
    }
  }
}