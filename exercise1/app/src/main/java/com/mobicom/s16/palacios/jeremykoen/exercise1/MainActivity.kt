package com.mobicom.s16.palacios.jeremykoen.exercise1

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

import android.view.View;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

class MainActivity : AppCompatActivity() {
    private var isLiked = false;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val likeButton = findViewById<FloatingActionButton>(R.id.likeButton)
        likeButton.setOnClickListener{
            isLiked = !isLiked;
            val iconRes = if(isLiked) R.drawable.heartcolored else R.drawable.heart
            likeButton.setImageResource(iconRes)
        }
    }
}