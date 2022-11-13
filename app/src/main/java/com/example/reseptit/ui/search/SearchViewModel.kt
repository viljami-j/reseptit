package com.example.menudrawerexample.ui.gallery

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SearchViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "Tämä on Reseptien haku sivu"
    }
    val text: LiveData<String> = _text
}