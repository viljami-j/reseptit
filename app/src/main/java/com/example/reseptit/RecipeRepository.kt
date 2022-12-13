package com.example.reseptit

import androidx.lifecycle.LiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RecipeRepository(private val database: RecipeDatabase) {
    val recipes: LiveData<List<Recipe>> = database.recipeDao().getRecipes() // Gets local recipes

    // Refresh offline recipe cache from network
    suspend fun refreshRecipes() {
        withContext(Dispatchers.IO) {
            val recipes = RecipeNetwork.recipes.getRecipes()
            database.recipeDao().insertAll(*recipes.toTypedArray())
        }
    }
    fun addRecipe(recipe: Recipe){
        database.recipeDao().insertOne(recipe)
    }
}