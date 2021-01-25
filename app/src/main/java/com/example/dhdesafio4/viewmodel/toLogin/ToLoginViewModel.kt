package com.example.dhdesafio4.viewmodel.toLogin

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.dhdesafio4.data.User
import com.google.firebase.auth.FirebaseAuth

class ToLoginViewModel(private val auth: FirebaseAuth) : ViewModel() {
    val loginStatus = MutableLiveData<Pair<Boolean, String>>()

    fun login(user: User) {
        auth.signInWithEmailAndPassword(user.email, user.password).addOnSuccessListener {
            loginStatus.value = Pair(true, "")
        }.addOnFailureListener {
            if(it.message == null) {
                // TODO: Use res
                loginStatus.value = Pair(false, "Erro inesperado")
                return@addOnFailureListener
            }

            loginStatus.value = Pair(false, it.message!!)
        }.addOnCanceledListener {
            loginStatus.value = Pair(false, "Login cancelado")
        }
    }
}