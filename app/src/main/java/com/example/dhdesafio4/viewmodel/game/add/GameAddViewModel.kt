package com.example.dhdesafio4.viewmodel.game.add

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.dhdesafio4.data.GameItem
import com.example.dhdesafio4.utils.behavior.user.AuthToLoginIfNot
import com.example.dhdesafio4.utils.behavior.user.AuthenticatorCheck
import com.example.dhdesafio4.utils.behavior.user.FirebaseReferenceMaker
import com.example.dhdesafio4.utils.behavior.user.FirebaseUserReference
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class GameAddViewModel(private val fStorage: FirebaseStorage, private val fAuth: FirebaseAuth,
                       private val fDatabase: FirebaseDatabase) : ViewModel() {
    private var storageRef: StorageReference? = null

    private var databaseRef: DatabaseReference? = null

    private val fReferenceMaker: FirebaseReferenceMaker by lazy {
        FirebaseUserReference(
            fAuth
        )
    }

    private val authenticatorCheck: AuthenticatorCheck by lazy {
        // Define um comportamento caso o login não tenha sido realizado
        AuthToLoginIfNot(
            fAuth
        )
    }

    var imageUri: String = ""

    val goToAndFinish = MutableLiveData<Class<*>>()

    val gameCreateStatus = MutableLiveData<Pair<Boolean, String>>()

    private fun validateGameItem(gameItem: GameItem): Pair<Boolean, String> {
        if (gameItem.createdAt.isEmpty() || gameItem.createdAt.isBlank()) {
            return Pair(false, "Não foi definida a data")
        } else if (gameItem.name.isEmpty() || gameItem.name.isBlank()) {
            return Pair(false, "Não foi definido o nome do jogo")
        } else if (gameItem.description.isEmpty() || gameItem.description.isBlank()) {
            return Pair(false, "Não foi definido a descrição do jogo")
        } else if (gameItem.imageURI.isBlank() || gameItem.imageURI.isEmpty()) {
            return Pair(false, "Não foi selecionado a imagem do jogo")
        }

        return Pair(true, "tudo ok")
    }

    fun saveGameItem(gameItem: GameItem) {
        validations(gameItem)
        generateRefs(gameItem)

        databaseRef = databaseRef!!.push()
        storageRef = storageRef!!.child(databaseRef!!.key!!)

        storageRef!!.putFile(Uri.parse(gameItem.imageURI)).addOnSuccessListener {
            val newGameItem = GameItem(
                gameItem.name,
                gameItem.createdAt,
                gameItem.description,
                storageRef!!.path
            )

            databaseRef!!.setValue(newGameItem).addOnSuccessListener {
                gameCreateStatus.value = Pair(true, "Jogo foi criado com sucesso")
            }.addOnCanceledListener {
                gameCreateStatus.value = Pair(false, "Criação do jogo foi cancelada")
            }.addOnFailureListener {
                if (it.message == null) {
                    gameCreateStatus.value = Pair(false, "Ocorreu algum erro")
                    return@addOnFailureListener
                }

                gameCreateStatus.value = Pair(false, it.message!!)
            }
        }.addOnCanceledListener {
            gameCreateStatus.value = Pair(false, "Criação do jogo foi cancelada")
        }.addOnFailureListener {
            if (it.message == null) {
                gameCreateStatus.value = Pair(false, "Ocorreu algum erro")
                return@addOnFailureListener
            }

            gameCreateStatus.value = Pair(false, it.message!!)
        }
    }

    fun updateGameItem(gameItem: GameItem) {
        validations(gameItem)
        generateRefs(gameItem)

        if (gameItem.imageURI.matches(Regex("content://.*"))) {
            storageRef!!.putFile(Uri.parse(gameItem.imageURI)).addOnSuccessListener {
                val newGameItem = GameItem(
                    gameItem.name,
                    gameItem.createdAt,
                    gameItem.description,
                    storageRef!!.path,
                )

                databaseRef!!.child(gameItem.uid).setValue(newGameItem).addOnSuccessListener {
                    gameCreateStatus.value = Pair(true, "Jogo foi atualizado com sucesso")
                }.addOnCanceledListener {
                    gameCreateStatus.value = Pair(false, "Atualização do jogo foi cancelada")
                }.addOnFailureListener {
                    if (it.message == null) {
                        gameCreateStatus.value = Pair(false, "Ocorreu algum erro")
                        return@addOnFailureListener
                    }

                    gameCreateStatus.value = Pair(false, it.message!!)
                }
            }.addOnCanceledListener {
                gameCreateStatus.value = Pair(false, "Atualização do jogo foi cancelada")
            }.addOnFailureListener {
                if (it.message == null) {
                    gameCreateStatus.value = Pair(false, "Ocorreu algum erro")
                    return@addOnFailureListener
                }

                gameCreateStatus.value = Pair(false, it.message!!)
            }

            return
        }

        // Coloca o caminho de forma manual do firebase
        gameItem.imageURI = "${fAuth.uid}/games/${gameItem.uid}"
        databaseRef!!.child(gameItem.uid).setValue(gameItem).addOnSuccessListener {
            gameCreateStatus.value = Pair(true, "Jogo foi atualizado com sucesso")
        }.addOnCanceledListener {
            gameCreateStatus.value = Pair(false, "Atualização do jogo foi cancelada")
        }.addOnFailureListener {
            if (it.message == null) {
                gameCreateStatus.value = Pair(false, "Ocorreu algum erro")
                return@addOnFailureListener
            }

            gameCreateStatus.value = Pair(false, it.message!!)
        }
    }

    private fun validations(gameItem: GameItem) {
        // Envia para o cliente que não tem como proceder pois não tem um usuário logado
        if (!authenticatorCheck.verifyUserLogged()) {
            goToAndFinish.value = authenticatorCheck.goTo()
            return
        }

        val resp = validateGameItem(gameItem)
        if (!resp.first) {
            gameCreateStatus.value = resp
            return
        }
    }

    private fun generateRefs(gameItem: GameItem) {
        if (storageRef == null) {
            if (gameItem.uid.isEmpty() || gameItem.uid.isBlank()) {
                storageRef = fReferenceMaker.storageReference(fStorage.reference)
            } else {
                storageRef = fReferenceMaker.storageReference(fStorage.reference).child(gameItem.uid)
            }

        }

        if (databaseRef == null) {
            databaseRef = fReferenceMaker.databaseReference(fDatabase.reference)
        }
    }

}