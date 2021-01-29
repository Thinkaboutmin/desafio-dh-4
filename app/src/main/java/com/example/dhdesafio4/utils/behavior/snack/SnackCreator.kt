package com.example.dhdesafio4.utils.behavior.snack

import com.google.android.material.snackbar.Snackbar

/**
 * PT-BR
 * Cria snacks com cores e mensagens diferentes com o paramêtro passado.
 *
 * EN-US
 * Create snacks with colors and messages differents from the parameter given
 *
 * @author Jomar Júnior
 */
interface SnackCreator {
    fun showSnack(value: Pair<Boolean, Int>): Snackbar

    fun showSnackS(value: Pair<Boolean, String>): Snackbar
}
