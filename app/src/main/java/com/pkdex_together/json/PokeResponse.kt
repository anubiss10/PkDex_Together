package com.pkdex_together.json

data class PokeResponse(
    val results: List<Pokemon>
)
data class Pokemon(
    val name: String,
    val url: String
)

data class PokeDetails(
    val id: Int,
    val name: String,
    val height: Int,
    val weight: Int,
    val sprites: Sprites
)

data class Sprites(
    val front_default: String //URL a imagen
)
