package com.example.dhdesafio4.viewmodel.game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage

class GameViewModelFactory(val storageRef: FirebaseStorage? = null, val fAuth: FirebaseAuth,
                              val fDatabase: FirebaseDatabase) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        val cont = modelClass.getConstructor(
            FirebaseStorage::class.java,
            FirebaseAuth::class.java,
            FirebaseDatabase::class.java
        )

        return cont.newInstance(storageRef, fAuth, fDatabase)
    }
}