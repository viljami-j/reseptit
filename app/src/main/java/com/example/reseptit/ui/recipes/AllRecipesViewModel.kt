package com.example.reseptit.ui.recipes

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.RecyclerView
import com.example.reseptit.R
import com.example.reseptit.Recipe
import com.example.reseptit.RecipeDatabase
import com.example.reseptit.RecipesRepository
import kotlinx.coroutines.launch
import java.io.IOException

class AllRecipesViewModel(application: Application) : AndroidViewModel(application) {

    private val recipesRepository = RecipesRepository(RecipeDatabase.getDatabase(application))

    val recipes: LiveData<List<Recipe>> = recipesRepository.recipes

    private fun refreshDataFromRepository() {
        viewModelScope.launch {
            try {
                recipesRepository.refreshRecipes()
                _eventNetworkError.value = false
            } catch (networkError: IOException) {
                if (recipes.value.isNullOrEmpty()) _eventNetworkError.value = true
            }
        }
    }

    init {
        refreshDataFromRepository()
    }

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


    private val _text = MutableLiveData<String>().apply {
        value = "Tämä on Kaikkien reseptien sivu"
    }
    val text: LiveData<String> = _text
}