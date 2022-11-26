package com.example.reseptit.adapter

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.reseptit.R
import com.example.reseptit.Recipe
import com.example.reseptit.viewholder.RecyclerViewHolder
import kotlin.math.roundToInt


class RecipeListAdapter(private val recipes: List<Recipe>) : RecyclerView.Adapter<RecyclerViewHolder>() {
    // A Viewholder represents an item.
    // Here in the adapter class we bind data into items we create.

    // Function for scaling reducing image size
    private fun scaleDown(
        realImage: Bitmap, maxImageSize: Float,
        filter: Boolean
    ): Bitmap? {
        val ratio = Math.min(
            maxImageSize / realImage.width,
            maxImageSize / realImage.height
        )
        val width = (ratio * realImage.width).roundToInt()
        val height = (ratio * realImage.height).roundToInt()
        return Bitmap.createScaledBitmap(
            realImage, width,
            height, filter
        )
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.recipe_item, parent, false)
        return RecyclerViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        val maxImageSize = 256f;

        val recipe = recipes[position]

        val decodedString: ByteArray = Base64.decode(recipe.imageBase64, Base64.DEFAULT) //imageUri = base64 string in this case, not truly a uri
        val decodedByte: Bitmap? = scaleDown(BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size), maxImageSize, true)

        holder.viewImg.setImageBitmap(decodedByte)
        holder.viewTitle.text = recipe.name
        holder.viewIngredients.text = recipe.ingredients
        holder.viewCookingInstructions.text = recipe.cookingInstructions
        holder.viewCookingTimeInMinutes.text = recipe.cookingTimeInMinutes.toString()
        holder.viewDesc.text = recipe.description
    }

    override fun getItemCount(): Int {
        return recipes.count()
    }
}