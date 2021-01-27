package com.example.dhdesafio4.viewmodel.toSignIn

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth

class ToSignInViewModelFactory(val auth: FirebaseAuth) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(FirebaseAuth::class.java).newInstance(auth)
    }
}