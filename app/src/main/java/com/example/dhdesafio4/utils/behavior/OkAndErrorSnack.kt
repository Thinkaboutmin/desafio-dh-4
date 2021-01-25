package com.example.dhdesafio4.utils.behavior

import android.content.Context
import android.graphics.Color
import android.view.View
import com.google.android.material.snackbar.Snackbar

/**
 * PT-BR
 * Cria uma snack com a colaração verde caso o primeiro item do par seja verdadeira senão
 * uma snack vermelha é criada.
 *
 * EN-US
 * Creates a snack with the green color if the first item of the pair is true otherwise a red one
 * will be created
 *
 * @param view View na qual a snack vai aparecer acima
 *             View which the snack will show on top
 * @param context Contexto do app
 *                App context
 */
class OkAndErrorSnack(
    private val view: View,
    private val context: Context
) : SnackCreator {
    override fun showSnack(value: Pair<Boolean, Int>): Snackbar {
        return showSnackS(
            Pair(
                value.first,
                context.getString(value.second)
            )
        )
    }

    override fun showSnackS(value: Pair<Boolean, String>): Snackbar {
        val snack = Snackbar.make(
            view,
            value.second,
            Snackbar.LENGTH_SHORT
        )

        snack.show()

        if (value.first) {
            snack.setBackgroundTint(Color.GREEN)
        } else {
            snack.setBackgroundTint(Color.RED)
        }

        return snack
    }
}