package com.example.dhdesafio4.viewmodel.toSignIn

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.dhdesafio4.data.User
import com.google.firebase.auth.FirebaseAuth

class ToSignInViewModel(val auth: FirebaseAuth) : ViewModel() {
    val registerStatus = MutableLiveData<Pair<Boolean, String>>()

    fun register(user: User, pswRepeat: String) {
        if (user.email.isEmpty() || user.email.isBlank() || user.password.isBlank() ||
                user.password.isEmpty() || pswRepeat.isEmpty() || pswRepeat.isBlank()) {
            registerStatus.value = Pair(false, "Alguns campos estão em branco")
            return
        }

        if (pswRepeat != user.password) {
            registerStatus.value = Pair(false, "As senhas não são as mesmas")
            return
        }

        auth.createUserWithEmailAndPassword(user.email, user.password).addOnCanceledListener {
            registerStatus.value = Pair(false, "Regsitro cancelado")
        }.addOnSuccessListener {
            registerStatus.value = Pair(true, "")
        }.addOnFailureListener {
            if (it.message == null) {
                registerStatus.value = Pair(false, "Erro inesperado")

                return@addOnFailureListener
            }

            registerStatus.value = Pair(false, it.message!!)
        }
    }

}