package com.example.reseptit

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

// https://developer.android.com/static/codelabs/basic-android-kotlin-training-repository-pattern/img/69021c8142d29198_960.png
// https://prnt.sc/Xi7H1ykuQ_ML
class RecipesRepository(private val database: RecipeDatabase) {
    // Should be decided in this class whether we fetch data locally or from remote

    // Refresh offline cache from network
    suspend fun refreshRecipes() {
        withContext(Dispatchers.IO) {
            val recipes = RecipeNetwork.recipes.getRecipes()
            println(recipes)
            //database.recipeDao().insertAll(recipes)
        }
    }
}