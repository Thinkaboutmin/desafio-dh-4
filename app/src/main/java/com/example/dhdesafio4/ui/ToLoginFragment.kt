package com.example.dhdesafio4.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.dhdesafio4.R
import com.example.dhdesafio4.data.User
import com.example.dhdesafio4.databinding.FragmentLogInBinding
import com.example.dhdesafio4.utils.behavior.OkAndErrorSnack
import com.example.dhdesafio4.utils.behavior.SnackCreator
import com.example.dhdesafio4.viewmodel.toLogin.ToLoginViewModel
import com.example.dhdesafio4.viewmodel.toLogin.ToLoginViewModelFactory
import com.google.firebase.auth.FirebaseAuth

class ToLoginFragment : Fragment() {
    private val snackCreator: SnackCreator by lazy {
        OkAndErrorSnack(
            requireView(),
            requireContext()
        )
    }

    private val viewModel by viewModels<ToLoginViewModel> {
        ToLoginViewModelFactory(
            FirebaseAuth.getInstance()
        )
    }

    private var _binding: FragmentLogInBinding? = null

    private val binding get() =  _binding!!

    private val navController by lazy {
        findNavController()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLogInBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.run {
            btnSignIn.setOnClickListener {
                navController.navigate(R.id.toSignInFragment)
            }


            viewModel.loginStatus.observe(viewLifecycleOwner) {
                loading.llLoading.visibility = View.GONE
                snackCreator.showSnackS(it)
            }

            btnLogin.setOnClickListener {
                loading.llLoading.visibility = View.VISIBLE
                viewModel.login(User(
                        tvEmail.editText!!.text.toString(),
                        "",
                        tvPsw.editText!!.text.toString()
                ))
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}