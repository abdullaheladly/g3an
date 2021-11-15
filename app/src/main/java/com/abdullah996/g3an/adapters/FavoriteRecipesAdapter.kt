package com.abdullah996.g3an.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.abdullah996.g3an.data.database.entities.FavoritesEntity
import com.abdullah996.g3an.databinding.FavoriteRecipesRowLayoutBinding
import com.abdullah996.g3an.util.RecipesDiffUtil

class FavoriteRecipesAdapter:RecyclerView.Adapter<FavoriteRecipesAdapter.MyViewHolder>() {
    private var favoriterecipes= emptyList<FavoritesEntity>()
    class MyViewHolder(private val binding: FavoriteRecipesRowLayoutBinding)
        :RecyclerView.ViewHolder(binding.root) {
            fun bind(favoritesEntity: FavoritesEntity){
                binding.favoritesEntity=favoritesEntity
                binding.executePendingBindings()
            }
        companion object{
            fun from(parent: ViewGroup):MyViewHolder{
                val layoutInflater=LayoutInflater.from(parent.context)
                val binding=FavoriteRecipesRowLayoutBinding.inflate(layoutInflater,parent,false)
                return MyViewHolder(binding)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val selectedRecipe=favoriterecipes[position]
        holder.bind(selectedRecipe)
    }

    override fun getItemCount(): Int {
        return favoriterecipes.size
    }
    fun setData(newFavoriteRecipe:List<FavoritesEntity>){
        val favoriteRecipesDiffUtil=
            RecipesDiffUtil(favoriterecipes,newFavoriteRecipe)
        val diffUtilResult=DiffUtil.calculateDiff(favoriteRecipesDiffUtil)
        favoriterecipes=newFavoriteRecipe
        diffUtilResult.dispatchUpdatesTo(this)
    }
}

