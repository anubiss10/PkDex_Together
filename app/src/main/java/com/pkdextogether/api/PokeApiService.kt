package com.pkdextogether.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

data class PokemonResponse(
    val name: String,
    val height: Int,
    val weight: Int,
    val sprites: Sprites,
    val types: List<PokemonType>
)

data class Sprites(val front_default: String)
data class PokemonType(val type: TypeName)
data class TypeName(val name: String)

data class SpeciesResponse(
    val flavor_text_entries: List<FlavorTextEntry>
)

data class FlavorTextEntry(
    val flavor_text: String,
    val language: Language
)

data class Language(val name: String)

interface PokeApiService {
    @GET("pokemon/{id}")
    fun getPokemonDetails(@Path("id") id: Int): Call<PokemonResponse>

    @GET("pokemon-species/{name}")
    fun getPokemonSpecies(@Path("name") name: String): Call<SpeciesResponse>
}