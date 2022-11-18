package com.example.menudrawerexample.ui.slideshow

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.reseptit.Recipe
import com.example.reseptit.RecipeDatabase

class AllRecipesViewModel(application: Application) : AndroidViewModel(application) {

    private val recipeDatabase by lazy { RecipeDatabase.getDatabase(application).recipeDao() }



    private val _text = MutableLiveData<String>().apply {
        value = "Tämä on Kaikkien reseptien sivu"
    }
    val text: LiveData<String> = _text
}