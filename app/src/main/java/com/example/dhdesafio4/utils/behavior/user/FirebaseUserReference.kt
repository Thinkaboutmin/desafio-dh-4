package com.example.dhdesafio4.utils.behavior.user

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.storage.StorageReference

class FirebaseUserReference(val fAuth: FirebaseAuth) : FirebaseReferenceMaker {
    override fun storageReference(rootRef: StorageReference): StorageReference =
        rootRef.child(fAuth.currentUser!!.uid).child("games")

    override fun databaseReference(rootRef: DatabaseReference): DatabaseReference =
        rootRef.child(fAuth.currentUser!!.uid).child("games")

}