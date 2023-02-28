package com.elka.heofficeclub.view.ui.authScreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.elka.heofficeclub.databinding.AuthorizationFragmentBinding
import com.elka.heofficeclub.databinding.WelcomeFragmentBinding
import com.elka.heofficeclub.view.ui.BaseFragment

class AuthFragment: BaseFragment() {
    private lateinit var binding: AuthorizationFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = AuthorizationFragmentBinding.inflate(layoutInflater, container, false)
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            master = this@AuthFragment
        }

        return binding.root
    }

    fun goBack() {
        navController.popBackStack()
    }

    fun tryLogin() {
        Toast.makeText(requireContext(), "Try login...", Toast.LENGTH_SHORT).show()
    }
}