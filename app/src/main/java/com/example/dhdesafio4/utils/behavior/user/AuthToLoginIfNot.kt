package com.example.dhdesafio4.utils.behavior.user

import com.example.dhdesafio4.ui.LoginActivity
import com.google.firebase.auth.FirebaseAuth

class AuthToLoginIfNot(val fAuth: FirebaseAuth) : AuthenticatorCheck {
    override fun verifyUserLogged(): Boolean {
        if (fAuth.currentUser == null) {
            return false
        }

        return true
    }

    override fun goTo(): Class<*> = LoginActivity::class.java
}