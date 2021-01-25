package com.example.dhdesafio4.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.dhdesafio4.R
import com.example.dhdesafio4.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }

}