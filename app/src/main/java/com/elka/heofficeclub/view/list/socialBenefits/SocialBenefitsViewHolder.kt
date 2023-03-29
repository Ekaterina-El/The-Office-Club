package com.elka.heofficeclub.view.list.socialBenefits

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import com.elka.heofficeclub.databinding.SocialBenefitItemBinding
import com.elka.heofficeclub.other.documents.SocialBenefit

class SocialBenefitsViewHolder(
  val context: Context,
  val binding: SocialBenefitItemBinding
) : RecyclerView.ViewHolder(binding.root) {
  fun bind(socialBenefit: SocialBenefit) {
    binding.socialBenefit = socialBenefit
  }
}