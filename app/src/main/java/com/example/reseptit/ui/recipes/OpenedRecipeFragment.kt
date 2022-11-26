package com.example.reseptit.ui.recipes
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.reseptit.R
import com.example.reseptit.databinding.FragmentOpenedRecipeBinding

class OpenedRecipeFragment : Fragment() {
    private var _binding: FragmentOpenedRecipeBinding? = null

    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    lateinit var root: View

    // returns an array containing: suffix, valueInDeducedTimeUnit
    private fun deduceAppropriateTimeUnit(minutes: Int?): Array<String> {
        //TODO: change to func to return an object with key'd values rather than an array for clarity
        if (minutes != null) {
        if (minutes >= 1440) {
           return arrayOf(root.context.getString(R.string.recipe_cooking_time_days), (minutes/60/24).toString()) // returns: suffix, valueInDays
        }
        if (minutes > 60) {
            return arrayOf(root.context.getString(R.string.recipe_cooking_time_hours), (minutes/60).toString()) // returns: suffix, valueInHours
        }
        return arrayOf(root.context.getString(R.string.recipe_cooking_time_minutes), minutes.toString()) // returns: suffix, valueInMinutes
        }
        else {
            println("OpenedRecipeFragment.kt: minutes was null")
            return arrayOf("NOTIMEUNIT.", "0")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOpenedRecipeBinding.inflate(inflater, container, false)
        root = binding.root

        val ivImage = root.findViewById<ImageView>(R.id.recipe_opened_image)
        val tvIngredients = root.findViewById<TextView>(R.id.recipe_opened_ingredients)
        val tvCookingInstructions = root.findViewById<TextView>(R.id.recipe_opened_cooking_instructions)
        val tvCookingTimeInUnits = root.findViewById<TextView>(R.id.recipe_opened_cooking_time_in_minutes)
        val tvCookingTimeSuffix = root.findViewById<TextView>(R.id.recipe_suffix_cooking_time_in_minutes)

        // Relevant classes:
        // RecipeListAdapter.kt
        // RecyclerViewHolder.kt

        var imgBA = arguments?.getByteArray("recipeImage")
        if (imgBA != null) {
            ivImage.setImageBitmap(BitmapFactory.decodeByteArray(imgBA, 0, imgBA.size))
        }

        tvIngredients.text = arguments?.getString("recipeIngredients")
        tvCookingInstructions.text = arguments?.getString("recipeCookingInstructions")

        // Determine best type of time unit to display
        val cookingTimeInMinutes = arguments?.getString("recipeCookingTimeInMinutes")?.toInt()
        val timeUnit: Array<String> = deduceAppropriateTimeUnit(cookingTimeInMinutes)
        tvCookingTimeInUnits.text = timeUnit[1]
        tvCookingTimeSuffix.text = timeUnit[0]

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}