package com.example.dhdesafio4.viewmodel.game.details

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.dhdesafio4.data.GameItem
import com.example.dhdesafio4.utils.behavior.user.AuthToLoginIfNot
import com.example.dhdesafio4.utils.behavior.user.AuthenticatorCheck
import com.example.dhdesafio4.utils.behavior.user.FirebaseReferenceMaker
import com.example.dhdesafio4.utils.behavior.user.FirebaseUserReference
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class GameDetailsViewModel(private val fStorage: FirebaseStorage, private val fAuth: FirebaseAuth,
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

    private var eventListener: ValueEventListener? = null

    val goToAndFinish = MutableLiveData<Class<*>>()

    val gameItemChanges = MutableLiveData<GameItem>()

    fun followChanges(gameItem: GameItem) {
        validations()
        generateRefs(gameItem)

        if (eventListener == null) {
            eventListener = object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    var game = snapshot.getValue(GameItem::class.java)

                    if (game != null) {
                        fStorage.getReference(game.imageURI).downloadUrl.addOnSuccessListener {
                            game = GameItem(
                                game!!.name,
                                game!!.createdAt,
                                game!!.description,
                                it.toString(),
                                game!!.uid
                            )
                            gameItemChanges.value = game
                        }.addOnCanceledListener {

                        }.addOnFailureListener {

                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                }
            }

            databaseRef!!.addValueEventListener(
                eventListener!!
            )
        }
    }

    private fun validations() {
        // Envia para o cliente que não tem como proceder pois não tem um usuário logado
        if (!authenticatorCheck.verifyUserLogged()) {
            goToAndFinish.value = authenticatorCheck.goTo()
            return
        }
    }

    private fun generateRefs(gameItem: GameItem) {
        if (storageRef == null) {
            storageRef = fReferenceMaker.storageReference(fStorage.reference).child(gameItem.uid)
        }

        if (databaseRef == null) {
            databaseRef = fReferenceMaker.databaseReference(fDatabase.reference).child(gameItem.uid)
        }
    }
}