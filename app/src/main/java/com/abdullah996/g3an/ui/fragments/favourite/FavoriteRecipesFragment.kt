package com.abdullah996.g3an.ui.fragments.favourite

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.abdullah996.g3an.R
import com.abdullah996.g3an.adapters.FavoriteRecipesAdapter
import com.abdullah996.g3an.databinding.FragmentFavoriteRecipesBinding
import com.abdullah996.g3an.viewmodels.MainViewModel
import dagger.hilt.EntryPoint
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_favorite_recipes.view.*

@AndroidEntryPoint
class FavoriteRecipesFragment : Fragment() {
    private val mAdapter:FavoriteRecipesAdapter by lazy { FavoriteRecipesAdapter() }
    private val mainViewModel:MainViewModel by viewModels()

    private var _binding:FragmentFavoriteRecipesBinding?=null
    val  binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding= FragmentFavoriteRecipesBinding.inflate(inflater,container,false)
        binding.lifecycleOwner=this
        binding.mainViewModel=mainViewModel
        binding.mAdapter=mAdapter

        setupRecycleView(binding.favoriteRecipesRecycleView)

        mainViewModel.readFavoriteRecipes.observe(viewLifecycleOwner,{favouritesEntity->
            mAdapter.setData(favouritesEntity)
        })

        return binding.root
    }

    fun setupRecycleView(recyclerView: RecyclerView){
        recyclerView.adapter=mAdapter
        recyclerView.layoutManager=LinearLayoutManager(requireContext())
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding=null
    }


}