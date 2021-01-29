package com.example.dhdesafio4.viewmodel.game.list

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
import java.util.*

class GameViewModel(private val fStorage: FirebaseStorage, private val fAuth: FirebaseAuth,
                    private val fDatabase: FirebaseDatabase)
    : ViewModel() {
    private val authenticatorCheck: AuthenticatorCheck by lazy {
        // Define um comportamento caso o login não tenha sido realizado
        AuthToLoginIfNot(
            fAuth
        )
    }

    private val fReferenceMaker: FirebaseReferenceMaker by lazy {
        FirebaseUserReference(
            fAuth
        )
    }

    private var databaseRef: DatabaseReference? = null

    private var childrenListener: ChildEventListener? = null

    val goToAndFinish = MutableLiveData<Class<*>>()

    val gameList = MutableLiveData<MutableList<GameItem>>()

    val gameListStatus = MutableLiveData<Pair<Boolean, String>>()

    fun getGameList() {
        if (!authenticatorCheck.verifyUserLogged()) {
            goToAndFinish.value = authenticatorCheck.goTo()
            return
        }

        if (databaseRef == null) {
            databaseRef = fReferenceMaker.databaseReference(fDatabase.reference)
        }

        if (childrenListener == null) {
            childrenListener = object : ChildEventListener {
                override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                    newData(snapshot)
                }

                override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                    dataUpdated(snapshot, false)
                }

                override fun onChildRemoved(snapshot: DataSnapshot) {
                    dataUpdated(snapshot, true)
                }

                override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                    TODO("Not yet implemented")
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

                private fun newData(snapshot: DataSnapshot) {
                    val data =
                        snapshot.getValue(
                            GameItem::class.java
                        )
                    if (data != null && gameList.value != null) {
                        fStorage.getReference(data.imageURI).downloadUrl.addOnSuccessListener {
                                uri ->
                            gameList.value!!.add(GameItem(
                                data.name,
                                data.createdAt,
                                data.description,
                                uri.toString(),
                                snapshot.key!!
                            ))
                            gameList.postValue(gameList.value)
                        }.addOnCanceledListener {
                            // TODO
                        }.addOnFailureListener {
                            // TODO
                        }

                        gameList.postValue(gameList.value)
                    }

                    if (gameList.value == null) {
                        gameList.value = Collections.synchronizedList(mutableListOf())
                        newData(snapshot)
                    }
                }

                private fun dataUpdated(snapshot: DataSnapshot, removed: Boolean) {
                    val data =
                        snapshot.getValue(
                            GameItem::class.java
                        )
                    if (data != null && gameList.value != null) {
                        val gameItemList = gameList.value!!.find {
                            it.uid == snapshot.key
                        }
                        gameList.value!!.remove(gameItemList)

                        if (!removed) {
                            fStorage.getReference(data.imageURI).downloadUrl
                                .addOnSuccessListener { uri ->
                                gameList.value!!.add(
                                    GameItem(
                                        data.name,
                                        data.createdAt,
                                        data.description,
                                        uri.toString(),
                                        snapshot.key!!
                                    )
                                )
                                gameList.postValue(gameList.value)
                            }.addOnCanceledListener {
                                // TODO
                            }.addOnFailureListener {
                                // TODO
                            }
                        }

                        gameList.postValue(gameList.value)
                    }

                    if (gameList.value == null) {
                        gameList.value = Collections.synchronizedList(mutableListOf())
                        newData(snapshot)
                    }
                }
            }

            databaseRef!!.addChildEventListener(childrenListener!!)
        }

    }
}