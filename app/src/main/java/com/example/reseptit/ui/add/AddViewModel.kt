package com.example.reseptit.ui.add

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AddViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "Tämä on Lisää resepti-sivu"
    }
    val text: LiveData<String> = _text
}