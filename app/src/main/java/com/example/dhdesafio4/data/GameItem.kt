package com.example.dhdesafio4.data

import android.net.Uri
import java.time.LocalDateTime

data class GameItem(
        val name: String,
        val createdAt: LocalDateTime,
        val description: String,
        val imageURI: Uri
)
