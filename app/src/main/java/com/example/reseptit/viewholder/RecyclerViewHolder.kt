package com.example.reseptit.viewholder

import android.graphics.Bitmap
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.graphics.drawable.toBitmap
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.reseptit.R
import java.io.ByteArrayOutputStream


class RecyclerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
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