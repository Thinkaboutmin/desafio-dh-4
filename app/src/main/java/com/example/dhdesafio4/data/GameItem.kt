package com.example.dhdesafio4.data

import com.google.firebase.database.Exclude
import java.io.Serializable

data class GameItem(
        var name: String = "",
        var createdAt: String = "",
        var description: String = "",
        var imageURI: String = "",
        @get:Exclude val uid: String = ""
) : Serializable
