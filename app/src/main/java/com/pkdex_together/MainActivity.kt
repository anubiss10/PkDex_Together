package com.pkdex_together

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val startButton: Button = findViewById(R.id.startButton)
        startButton.setOnClickListener {
            val intent = Intent(this, PokeDetailsActivity::class.java)
            intent.putExtra("POKEMON_ID", 1) // ID inicial del Pokémon
            startActivity(intent)
        }
    }

}