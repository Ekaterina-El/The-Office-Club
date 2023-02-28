package com.elka.heofficeclub.view.ui.splashScreen

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.elka.heofficeclub.R
import com.elka.heofficeclub.databinding.SplashFragmentBinding
import com.elka.heofficeclub.other.Constants
import com.elka.heofficeclub.view.ui.BaseFragment

class SplashFragment: BaseFragment() {
    private lateinit var binding: SplashFragmentBinding
    private val handler by lazy { Handler(Looper.getMainLooper()) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        requireActivity().window.statusBarColor = requireContext().getColor(R.color.white)
        binding = SplashFragmentBinding.inflate(layoutInflater, container, false)
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onStart() {
        super.onStart()
        handler.postDelayed({
//            requireActivity().window.statusBarColor = requireContext().getColor(R.color.primary)
            findNavController().navigate(R.id.action_splashFragment_to_welcomeFragment)
        }, Constants.LOAD_DELAY)
    }
}