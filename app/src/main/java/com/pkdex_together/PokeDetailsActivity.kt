package com.pkdex_together

import android.os.Bundle
import android.view.GestureDetector
import android.view.MotionEvent
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.pkdex_together.api.PokeApiClient
import com.pkdex_together.api.PokeApiService
import com.pkdex_together.api.PokemonResponse
import com.pkdex_together.api.SpeciesResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PokeDetailsActivity : AppCompatActivity(), GestureDetector.OnGestureListener {

    private lateinit var gestureDetector: GestureDetector
    private var pokemonId = 1
    private val apiService = PokeApiClient.retrofit.create(PokeApiService::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.pkdex_layout)

        gestureDetector = GestureDetector(this, this)
        pokemonId = intent.getIntExtra("POKEMON_ID", 1)

        fetchPokemonDetails(pokemonId)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        gestureDetector.onTouchEvent(event)
        return super.onTouchEvent(event)
    }

    private fun fetchPokemonDetails(id: Int) {
        val imageView: ImageView = findViewById(R.id.pokemonImage)
        val nameView: TextView = findViewById(R.id.pokemonName)
        val typeView: TextView = findViewById(R.id.pokemonType)
        val heightView: TextView = findViewById(R.id.pokemonHeight)
        val weightView: TextView = findViewById(R.id.pokemonWeight)
        val descriptionView: TextView = findViewById(R.id.pokemonDescription)

        apiService.getPokemonDetails(id).enqueue(object : Callback<PokemonResponse> {
            override fun onResponse(call: Call<PokemonResponse>, response: Response<PokemonResponse>) {
                if (response.isSuccessful) {
                    val pokemon = response.body()
                    pokemon?.let {
                        nameView.text = it.name.replaceFirstChar { char -> char.uppercase() }
                        typeView.text = "Tipo: ${it.types[0].type.name}"
                        heightView.text = "Altura: ${it.height}"
                        weightView.text = "Peso: ${it.weight}"
                        Glide.with(this@PokeDetailsActivity).load(it.sprites.front_default).into(imageView)

                        fetchPokemonDescription(it.name, descriptionView)
                    }
                }
            }

            override fun onFailure(call: Call<PokemonResponse>, t: Throwable) {
                descriptionView.text = "Error al cargar datos del Pokémon."
            }
        })
    }

    private fun fetchPokemonDescription(name: String, descriptionView: TextView) {
        apiService.getPokemonSpecies(name).enqueue(object : Callback<SpeciesResponse> {
            override fun onResponse(call: Call<SpeciesResponse>, response: Response<SpeciesResponse>) {
                if (response.isSuccessful) {
                    val species = response.body()
                    species?.let {
                        val description = it.flavor_text_entries.find { entry ->
                            entry.language.name == "es"
                        }?.flavor_text?.replace("\n", " ")?.replace("\u000c", " ") ?: "No disponible"

                        descriptionView.text = description
                    }
                }
            }

            override fun onFailure(call: Call<SpeciesResponse>, t: Throwable) {
                descriptionView.text = "Error al cargar la descripción."
            }
        })
    }

    override fun onFling(
        e1: MotionEvent?,
        e2: MotionEvent,
        velocityX: Float,
        velocityY: Float
    ): Boolean {
        // Verifica que los eventos no sean nulos
        if (e1 != null && e2 != null) {
            if (e1.y - e2.y > 50) { // Deslizar hacia abajo
                pokemonId++
                fetchPokemonDetails(pokemonId)
            } else if (e2.y - e1.y > 50 && pokemonId > 1) { // Deslizar hacia arriba
                pokemonId--
                fetchPokemonDetails(pokemonId)
            }
        }
        return true
    }

    override fun onDown(e: MotionEvent): Boolean = true
    override fun onShowPress(e: MotionEvent) {}
    override fun onSingleTapUp(e: MotionEvent): Boolean = false
    override fun onScroll(
        e1: MotionEvent?,
        e2: MotionEvent,
        distanceX: Float,
        distanceY: Float
    ): Boolean {
        TODO("Not yet implemented")
    }

    override fun onLongPress(e: MotionEvent) {}

}