package com.example.menudrawerexample.ui.slideshow

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AllRecipesViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "Tämä on Kaikkien reseptien sivu"
    }
    val text: LiveData<String> = _text
}