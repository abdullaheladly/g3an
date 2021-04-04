package com.abdullah996.g3an.ui.fragments.recipes

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.abdullah996.g3an.viewmodels.MainViewModel
import com.abdullah996.g3an.R
import com.abdullah996.g3an.adapters.RecipesAdapter
import com.abdullah996.g3an.databinding.FragmentRecipesBinding
import com.abdullah996.g3an.util.Constants.Companion.API_KEY
import com.abdullah996.g3an.util.NetworkResult
import com.abdullah996.g3an.util.observeOnce
import com.abdullah996.g3an.viewmodels.RecipesViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_recipes.view.*
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RecipesFragment : Fragment() {
    private val args by navArgs<RecipesFragmentArgs>()
    private var _binding:FragmentRecipesBinding?=null
    private val binding get()=_binding!!
    private lateinit var recipesViewModel: RecipesViewModel
    private lateinit var mainViewModel: MainViewModel
    private val mAdapter by lazy { RecipesAdapter() }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainViewModel=ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
        recipesViewModel=ViewModelProvider(requireActivity()).get(RecipesViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding= FragmentRecipesBinding.inflate(inflater, container, false)
        binding.lifecycleOwner=this
        binding.mainViewModel=mainViewModel
        setupRecycleView()
        readDatabase()
        binding.recipeFab.setOnClickListener {
            findNavController().navigate(R.id.action_recipesFragment_to_recipesBottomSheet)
        }
        return binding.root
    }

    private fun readDatabase() {
       lifecycleScope.launch {
           mainViewModel.readRecipes.observeOnce(viewLifecycleOwner,{database->
               if (database.isNotEmpty()&& !args.backFromBottomSheet ){
                   Log.d("Recipes Fragment","read  data called")

                   mAdapter.setData(database[0].foodRecipe)
                   hideShimmerEffect()
               }else{
                   requestApiData()
               }

           })
       }
    }

    private fun requestApiData(){
        Log.d("Recipes Fragment","request api data called")
        mainViewModel.getRecipes(recipesViewModel.applyQueries())
        mainViewModel.recipeResponse.observe(viewLifecycleOwner, Observer{ respose->
            when(respose){
                is NetworkResult.Success->{
                    hideShimmerEffect()
                    respose.data?.let {
                        mAdapter.setData(it)

                    }
                }
                is NetworkResult.Error ->{
                    hideShimmerEffect()
                    loadDataFromCache()
                    Toast.makeText(
                            requireContext(),
                            respose.message.toString(),
                            Toast.LENGTH_SHORT
                    ).show()
                }
                is NetworkResult.Loading ->{
                    showShimmerEffect()
                }
            }

        })

    }

    private fun loadDataFromCache(){
        lifecycleScope.launch {
            mainViewModel.readRecipes.observe(viewLifecycleOwner,{database->
                if (database.isNotEmpty()){
                    mAdapter.setData(database[0].foodRecipe)
                }

            })
        }
    }



    private fun showShimmerEffect(){
        binding.recyclerView.showShimmer()
    }
    private fun hideShimmerEffect(){
        binding.recyclerView.hideShimmer()
    }
    private fun setupRecycleView(){
        binding.recyclerView.adapter=mAdapter
        binding.recyclerView.layoutManager=LinearLayoutManager(requireContext())
        showShimmerEffect()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding=null
    }


}