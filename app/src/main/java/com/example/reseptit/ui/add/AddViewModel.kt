package com.example.reseptit.ui.add

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.*
import com.example.reseptit.R
import com.example.reseptit.Recipe
import com.example.reseptit.RecipeDatabase
import com.example.reseptit.RecipeRepository
import kotlinx.coroutines.launch
import java.io.IOException

class AddViewModel(application: Application) : AndroidViewModel(application) {

    private val recipeRepository = RecipeRepository(RecipeDatabase.getDatabase(application))

    val recipes: LiveData<List<Recipe>> = recipeRepository.recipes

    private fun refreshDataFromRepository() {
        viewModelScope.launch {
            try {
                recipeRepository.refreshRecipes()
                _eventNetworkError.value = false
            } catch (networkError: IOException) {
                if (recipes.value.isNullOrEmpty()) _eventNetworkError.value = true
            }
        }
    }

    init {
        refreshDataFromRepository()
    }
    fun addRecipe(recipe: Recipe){
        recipeRepository.addRecipe(recipe)
    }

    val recipeAddedToast: Toast = Toast.makeText(application, "Resepti luotu onnistuneesti", Toast.LENGTH_LONG)


    /**
     * Event triggered for network error. This is private to avoid exposing a
     * way to set this value to observers.
     */
    private var _eventNetworkError = MutableLiveData(false)

    /**
     * Event triggered for network error. Views should use this to get access
     * to the data.
     */
    val eventNetworkError: LiveData<Boolean>
        get() = _eventNetworkError

    val networkErrorToast: Toast = Toast.makeText(application, R.string.network_error_toast, Toast.LENGTH_LONG)
}