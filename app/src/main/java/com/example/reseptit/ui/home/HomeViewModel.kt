package com.example.reseptit.ui.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.reseptit.Recipe
import com.example.reseptit.RecipeDatabase

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val recipeDatabase by lazy { RecipeDatabase.getDatabase(application).recipeDao() }

    private val _text = MutableLiveData<String>().apply {
        val testRecipe = Recipe(
            "Koodarin rohto",
            "Elvyttää nääntyneen ja unettoman koodarin",
            "- 10g suolaa\n- 2.5dl kyyneliä\n- 200mg raakaa kofeiinijauhetta",
            "- Lämmitä uuni 300 C\n- Aseta ainekset lasipulloon ja kääri se folioon\n- Paista uunissa 15min\n- Jäähdytä 15min kylmässä vesihauteessa\n- Juo ja toivu.",
            5.5f,
            null
        )
        recipeDatabase.insertAll(testRecipe)
        val recipeName = recipeDatabase.findRecipeByRid(3)?.name

        value = "Tämä on Kotisivu.\n" +
                "PS: Pyhän reseptin nimi on " + recipeName
    }
    val text: LiveData<String> = _text
}