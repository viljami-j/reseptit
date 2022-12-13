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
import kotlin.math.roundToInt
import android.widget.ImageView
import android.widget.TextView
import androidx.core.graphics.drawable.toBitmap
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import java.io.ByteArrayOutputStream

class RecipeListAdapter(private val recipes: List<Recipe>) : RecyclerView.Adapter<RecipeListAdapter.RecyclerViewHolder>() {
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

        val recipe = recipes[position]

        val maxImageSize = 256f;
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

    inner class RecyclerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var viewImg: ImageView
        var viewTitle: TextView
        var viewDesc: TextView
        var viewCookingTimeInMinutes: TextView

        // Data-only views
        var viewIngredients: TextView
        var viewCookingInstructions: TextView


        init {
            viewImg = itemView.findViewById(R.id.recipe_image) // TODO: Add necessary conversions depending on picture approach
            viewTitle = itemView.findViewById(R.id.recipe_title)
            viewDesc = itemView.findViewById(R.id.recipe_desc)

            viewIngredients = itemView.findViewById(R.id.recipe_ingredients)
            viewCookingInstructions = itemView.findViewById(R.id.recipe_cooking_instructions)
            viewCookingTimeInMinutes = itemView.findViewById(R.id.recipe_cooking_time_in_minutes)


            itemView.setOnClickListener {
                //Toast.makeText(itemView.context, viewTitle.text, Toast.LENGTH_LONG).show()

                val bitmap = viewImg.drawable.toBitmap()
                val stream = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.PNG, 90, stream)
                val img = stream.toByteArray()

                val bundle = bundleOf(
                    "recipeImage" to img,
                    "recipeTitle" to viewTitle.text,
                    "recipeIngredients" to viewIngredients.text,
                    "recipeDesc" to viewDesc.text,
                    "openedRecipeTitle" to viewTitle.text,
                    "recipeCookingInstructions" to viewCookingInstructions.text,
                    "recipeCookingTimeInMinutes" to viewCookingTimeInMinutes.text
                )
                itemView.findNavController().navigate(R.id.nav_opened_recipe, bundle)
                // bundle contents are accessed like so (using arguments?.):
                //tv.text = arguments?.getString("amount")
            }
        }
    }
}