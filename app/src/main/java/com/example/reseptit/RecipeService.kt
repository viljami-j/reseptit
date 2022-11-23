package com.example.reseptit

import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

/**
 * A retrofit service to fetch a list of recipes.
 * */
interface RecipeService {
    @GET("recipes")
    suspend fun getRecipes(): List<Recipe>
}

/**
 * Main entry point for network access. Call like `TODO: Example`
 */
object RecipeNetwork {

    // Configure retrofit to parse JSON and use coroutines
    private val retrofit = Retrofit.Builder()
        .baseUrl("http://10.0.2.2:3000/") // API base url
        .addConverterFactory(MoshiConverterFactory.create())
        .build()

    val recipes: RecipeService = retrofit.create(RecipeService::class.java)

}