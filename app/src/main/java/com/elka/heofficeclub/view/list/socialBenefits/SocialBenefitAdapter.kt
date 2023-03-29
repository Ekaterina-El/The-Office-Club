package com.elka.heofficeclub.view.list.socialBenefits

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.elka.heofficeclub.databinding.SocialBenefitItemBinding
import com.elka.heofficeclub.other.documents.SocialBenefit
import com.elka.heofficeclub.view.list.BaseAdapter

class SocialBenefitAdapter : BaseAdapter<SocialBenefit>() {
  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
    val binding = SocialBenefitItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    return SocialBenefitsViewHolder(parent.context, binding)
  }

  override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
    val item = items[position]
    (holder as SocialBenefitsViewHolder).bind(item)
  }
}