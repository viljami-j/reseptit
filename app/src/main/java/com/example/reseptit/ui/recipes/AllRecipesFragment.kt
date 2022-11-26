package com.example.reseptit.ui.recipes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.reseptit.R
import com.example.reseptit.Recipe
import com.example.reseptit.databinding.FragmentAllRecipesBinding
import com.example.reseptit.adapter.RecipeListAdapter

class AllRecipesFragment : Fragment() {
    private var _binding: FragmentAllRecipesBinding? = null


    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    // Holds the observed recipes
    private var recipes: List<Recipe> = listOf()

    lateinit var root: View
    lateinit var recyclerView: RecyclerView;


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val allRecipesViewModel = ViewModelProvider(this)[AllRecipesViewModel::class.java]

        _binding = FragmentAllRecipesBinding.inflate(inflater, container, false)
        root = binding.root
        recyclerView = root.findViewById(R.id.recipesRecyclerView) as RecyclerView

        allRecipesViewModel.recipes.observe(viewLifecycleOwner) { newRecipes ->
            // Update the UI
            recipes = newRecipes
            if (recipes.isNotEmpty()) {
                recyclerView.adapter = RecipeListAdapter(recipes)
                recyclerView.adapter?.notifyDataSetChanged() //TODO: If required, provide more efficient solution (notifydatasetchanged is meant to be a last resort)
            }

            // Show a toast if there is a network error
            if (allRecipesViewModel.eventNetworkError.value == true) allRecipesViewModel.networkErrorToast.show()
        }

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView.layoutManager = LinearLayoutManager(this.context)
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}