package com.example.dhdesafio4.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.dhdesafio4.databinding.FragmentSignInBinding
import com.example.dhdesafio4.utils.behavior.OkAndErrorSnack
import com.example.dhdesafio4.viewmodel.toSignIn.ToSignInViewModel
import com.example.dhdesafio4.viewmodel.toSignIn.ToSignInViewModelFactory
import com.google.firebase.auth.FirebaseAuth

class ToSignInFragment : Fragment() {
    private val snackCreator by lazy {
        OkAndErrorSnack(
            requireView(),
            requireContext()
        )
    }

    private val viewModel by viewModels<ToSignInViewModel> {
        ToSignInViewModelFactory(
            FirebaseAuth.getInstance()
        )
    }

    private var _binding: FragmentSignInBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignInBinding.inflate(inflater)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.run {
            btnLogin.setOnClickListener {

            }


        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}