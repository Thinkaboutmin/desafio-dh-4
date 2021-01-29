package com.example.dhdesafio4.utils.behavior.user

import com.google.firebase.database.DatabaseReference
import com.google.firebase.storage.StorageReference

interface FirebaseReferenceMaker {
    fun storageReference(rootRef: StorageReference): StorageReference

    fun databaseReference(rootRef: DatabaseReference): DatabaseReference
}