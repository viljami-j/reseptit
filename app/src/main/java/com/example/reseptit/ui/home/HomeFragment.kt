package com.example.reseptit.ui.home

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.reseptit.Recipe
import com.example.reseptit.databinding.FragmentHomeBinding
import com.example.reseptit.ui.search.HomeViewModel
import java.util.*
import kotlin.math.roundToInt
import kotlin.random.Random

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private var recipes: List<Recipe> = listOf()

    private var timeLeft: Int = 0

    // Different prefixes for recipe suggestions
    private var prefixList = listOf("Ehkä", "Kenties", "Saisiko olla", "Maistuisiko")

    lateinit var homeViewModel: HomeViewModel

    private var suggestionsTimer: Timer = Timer()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        homeViewModel = ViewModelProvider(this)[HomeViewModel::class.java]
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textHome
        homeViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }

        val recipesObserver = Observer<List<Recipe>> { newRecipes ->
            // Update the UI
            recipes = newRecipes
            if (recipes.isNotEmpty()) {
                // Do something with recipe data
            }

            // Show a toast if there is a network error
            if (homeViewModel.eventNetworkError.value == true) homeViewModel.networkErrorToast.show()
        }

        homeViewModel.recipes.observe(viewLifecycleOwner, recipesObserver)

        homeViewModel.recipeName = binding.recipeName
        homeViewModel.recipeImg = binding.recipeImg

        suggestionsTimer.run {  }

        return root
    }


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

    private fun decodeB64ToBitmap(b64: String?): Bitmap? {
        val maxImageSize = 256f * 3
        val decodedString: ByteArray = Base64.decode(b64, Base64.DEFAULT) //imageUri = base64 string in this case, not truly a uri
        return scaleDown(BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size), maxImageSize, true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Timer().scheduleAtFixedRate(object : TimerTask() {
            @SuppressLint("SetTextI18n")
            override fun run() {
                val rnd: Random = Random
                var rndRecipeIndex: Int

                if (isAdded) {
                    requireActivity().runOnUiThread {
                        if (prefixList.isNotEmpty() && recipes.isNotEmpty()&& recipes.size > 1) {
                            rndRecipeIndex = rnd.nextInt(0, recipes.size - 1)
                            val recipe = recipes[rndRecipeIndex]
                            homeViewModel.recipeImg?.setImageBitmap(decodeB64ToBitmap(recipe.imageBase64))
                            homeViewModel.recipeName?.text = prefixList[rnd.nextInt(
                                0,
                                prefixList.size - 1
                            )] + " \"" + recipes[rndRecipeIndex].name + "\"?"
                        }
                    }
                }
            }
        }, 0, 3000)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        suggestionsTimer.cancel()
        _binding = null
    }
}