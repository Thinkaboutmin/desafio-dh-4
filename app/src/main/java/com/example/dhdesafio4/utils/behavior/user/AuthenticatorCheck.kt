package com.example.dhdesafio4.utils.behavior.user

import com.example.dhdesafio4.ui.LoginActivity

interface AuthenticatorCheck {
    fun verifyUserLogged(): Boolean

    fun goTo(): Class<*>
}