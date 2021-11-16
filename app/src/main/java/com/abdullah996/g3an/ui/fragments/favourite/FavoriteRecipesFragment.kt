package com.abdullah996.g3an.ui.fragments.favourite

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.abdullah996.g3an.R
import com.abdullah996.g3an.adapters.FavoriteRecipesAdapter
import com.abdullah996.g3an.databinding.FragmentFavoriteRecipesBinding
import com.abdullah996.g3an.viewmodels.MainViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.EntryPoint
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_favorite_recipes.view.*

@AndroidEntryPoint
class FavoriteRecipesFragment : Fragment() {
    private val mainViewModel:MainViewModel by viewModels()
    private val mAdapter:FavoriteRecipesAdapter by lazy { FavoriteRecipesAdapter(requireActivity(),mainViewModel) }


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
        setHasOptionsMenu(true)
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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.favorite_recipes_menu,menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId==R.id.delete_all_favorite_Recipes_menu){
            mainViewModel.deleteAllFavoriteRecipes()
            showSnackBar("All Recipes Removed")
        }
        return super.onOptionsItemSelected(item)

    }

    private fun showSnackBar(string: String){
        Snackbar.make(
            binding.root,
            string,
            Snackbar.LENGTH_SHORT
        ).setAction("okay"){}.show()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding=null
        mAdapter.clearContextualActionMode()
    }


}