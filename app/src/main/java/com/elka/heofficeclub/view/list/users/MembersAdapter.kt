package com.elka.heofficeclub.view.list.users

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.elka.heofficeclub.databinding.MemberItemBinding
import com.elka.heofficeclub.other.documents.Member
import com.elka.heofficeclub.view.list.BaseAdapter

class MembersAdapter(private val listener: MemberViewHolder.Companion.Listener): BaseAdapter<Member>() {

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
    val binding = MemberItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    return MemberViewHolder(parent.context, binding, listener)
  }

  override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
    val item = items[position]
    (holder as MemberViewHolder).bind(item, position)
  }
}