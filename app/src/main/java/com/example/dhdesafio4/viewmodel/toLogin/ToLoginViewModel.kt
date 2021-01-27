package com.example.dhdesafio4.viewmodel.toLogin

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.dhdesafio4.data.User
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth

class ToLoginViewModel(private val auth: FirebaseAuth) : ViewModel() {
    /**
     * <p>
     * PT-BR
     * Transita o estado do login. O primeiro valor do par é para demonstrar se o login foi feito
     * com sucesso e o segundo valor é a mensagem contendo alguma informação do sucesso ou não.
     * <p>
     *
     * <p>
     * EN-US
     * Sends the login state. The first value of the pair is to verify if the login was done
     * successfully and the second one is the message with any information regarding the status.
     * <p>
     */
    val loginStatus = MutableLiveData<Pair<Boolean, String>>()
    private var task: Task<AuthResult>? = null

    /**
     *
     * <p>PT-BR</p>
     *
     * <p>
     * Loga o usuário ao sistema do Firebase. Retorno de sucesso ou falho é pego através do
     * @see #.loginStatus
     * </p>
     *
     *
     * <p>EN-US</p>
     * <p>
     * Login an user to the Firebase system. Returns the success or failure in the
     * @see #.loginStatus
     * </p>
     *
     * @param user O usuário a ser logado
     *             The user to login
     *
     */
    fun login(user: User) {
        if (user.email.isBlank() || user.email.isEmpty() || user.password.isBlank() ||
                user.password.isEmpty()) {
            loginStatus.value = Pair(false, "Algum dos campos está vázio")
            return
        }

        task = auth.signInWithEmailAndPassword(user.email, user.password).addOnSuccessListener {
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