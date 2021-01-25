package com.example.dhdesafio4.viewmodel.toSignIn

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.dhdesafio4.data.User
import com.google.firebase.auth.FirebaseAuth

class ToSignInViewModel(val auth: FirebaseAuth) : ViewModel() {
    val registerStatus = MutableLiveData<String>()

    fun register(user: User) {
        auth.createUserWithEmailAndPassword(user.email, user.password).addOnCanceledListener {
            registerStatus.value = "Regsitro cancelado"
        }.addOnSuccessListener {
            registerStatus.value = ""
        }.addOnFailureListener {
            if (it.message == null) {
                // TODO
                registerStatus.value = "Erro inesperado"

                return@addOnFailureListener
            }

            registerStatus.value = it.message!!
        }
    }

}