package com.example.reseptit.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.reseptit.Recipe

//Tämä liittyy reseptien hakuun!!
class RecipeAdapter(private val context: Context) :
    RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder>() {

    private val RecipeList : ArrayList<Recipe>
        get() {
            TODO()
        }
    private val FullList : ArrayList<Recipe>
        get() {
            TODO()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }
    inner class RecipeViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){

    }
}