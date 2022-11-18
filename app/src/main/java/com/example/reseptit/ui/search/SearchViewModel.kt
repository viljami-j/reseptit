package com.example.menudrawerexample.ui.gallery

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.reseptit.RecipeDatabase

class SearchViewModel(application: Application) : AndroidViewModel(application) {

    private val recipeDatabase by lazy { RecipeDatabase.getDatabase(application).recipeDao() }

    private val _text = MutableLiveData<String>().apply {
        value = "Tämä on Reseptien haku sivu"
    }
    val text: LiveData<String> = _text
}