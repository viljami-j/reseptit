package com.example.reseptit.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.reseptit.Recipe
import com.example.reseptit.RecipeDatabase
import com.example.reseptit.RecipeRepository
import kotlinx.coroutines.launch
import java.io.IOException

class OpenedRecipeViewmodel(application: Application) : AndroidViewModel(application) {
    private val recipeRepository = RecipeRepository(RecipeDatabase.getDatabase(application))

    var _application = application
    val recipes: LiveData<List<Recipe>> = recipeRepository.recipes
    private fun refreshDataFromRepository(application: Application) {
        viewModelScope.launch {
            try {
                recipeRepository.refreshRecipes()
                _application = application
            } catch (networkError: IOException) {
            }
        }
    }
    init {
        refreshDataFromRepository(application)
    }
}