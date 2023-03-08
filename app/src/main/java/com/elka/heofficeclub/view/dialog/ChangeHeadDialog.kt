package com.elka.heofficeclub.view.dialog

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import com.elka.heofficeclub.R
import com.elka.heofficeclub.databinding.ChangeHeadDialogBinding
import com.elka.heofficeclub.other.Role
import com.elka.heofficeclub.service.model.User
import com.elka.heofficeclub.view.list.users.SpinnerUsersAdapter

class ChangeHeadDialog(context: Context) : Dialog(context) {
  private lateinit var binding: ChangeHeadDialogBinding

  init {
    initDialog()
  }

  private fun initDialog() {
    binding = ChangeHeadDialogBinding.inflate(LayoutInflater.from(context))
    binding.apply {
      master = this@ChangeHeadDialog
    }
    setContentView(binding.root)

    window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    setCancelable(true)

  }

  private var selectedUser: User? = null

  fun open(head: Role, listener: Listener, spinner: SpinnerUsersAdapter) {
    val title = when (head) {
      Role.HUMAN_RESOURCES_DEPARTMENT_HEAD -> context.getString(R.string.change_hrd_head_title)
      Role.ORGANIZATION_HEAD -> context.getString(R.string.change_org_head_title)
      else -> return
    }

    binding.title.text = title
    binding.spinner.adapter = spinner

    binding.btnContinue.setOnClickListener {
      if (selectedUser != null) listener.agree(selectedUser!!)
      disagree()
    }

    binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
      override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        selectedUser = p0?.selectedItem as User
      }

      override fun onNothingSelected(p0: AdapterView<*>?) {}
    }
    show()
  }

  fun disagree() {
    dismiss()
  }

  companion object {
    interface Listener {
      fun agree(user: User)
    }
  }
}