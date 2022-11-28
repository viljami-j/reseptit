package com.example.reseptit.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.reseptit.R
import com.example.reseptit.Recipe
import com.example.reseptit.adapter.RecipeListAdapter
import com.example.reseptit.databinding.FragmentSearchBinding

class SearchFragment : Fragment() {
    private var _binding: FragmentSearchBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    // Holds the observed recipes
    private var recipes: List<Recipe> = listOf()

    lateinit var root: View
    lateinit var recyclerView: RecyclerView
    lateinit var searchView: SearchView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val searchViewmodel = ViewModelProvider(this)[SearchViewmodel::class.java]

        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        root = binding.root
        recyclerView = root.findViewById(R.id.searchRecyclerView) as RecyclerView
        searchView = root.findViewById(R.id.searchView)

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                val filteredRecipes = recipes.filter { it.name?.contains(searchView.query, true) ?: false }
                if (filteredRecipes.isNotEmpty()) {
                    recyclerView.adapter = RecipeListAdapter(filteredRecipes)
                    recyclerView.adapter?.notifyDataSetChanged() //TODO: If required, provide more efficient solution (notifydatasetchanged is meant to be a last resort)
                }
                else {
                    recyclerView.adapter = RecipeListAdapter(filteredRecipes)
                    Toast.makeText(root.context, R.string.no_search_results_toast, Toast.LENGTH_LONG).show()
                }
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return true
            }
        })

        searchViewmodel.recipes.observe(viewLifecycleOwner) { newRecipes ->
            // Update data property
            recipes = newRecipes

            // Show a toast if there is a network error
            if (searchViewmodel.eventNetworkError.value == true) searchViewmodel.networkErrorToast.show()
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