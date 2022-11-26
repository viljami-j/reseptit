package com.example.reseptit.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.reseptit.Recipe
import com.example.reseptit.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private var recipes: List<Recipe> = listOf()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this)[HomeViewModel::class.java]

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

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}