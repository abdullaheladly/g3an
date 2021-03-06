package com.abdullah996.g3an.adapters

import android.view.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.abdullah996.g3an.R
import com.abdullah996.g3an.data.database.entities.FavoritesEntity
import com.abdullah996.g3an.databinding.FavoriteRecipesRowLayoutBinding
import com.abdullah996.g3an.ui.fragments.favourite.FavoriteRecipesFragmentDirections
import com.abdullah996.g3an.util.RecipesDiffUtil
import com.abdullah996.g3an.viewmodels.MainViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.favorite_recipes_row_layout.view.*

class FavoriteRecipesAdapter(
    private val requiredActivity:FragmentActivity,
    private val mainViewModel: MainViewModel
):RecyclerView.Adapter<FavoriteRecipesAdapter.MyViewHolder>(),ActionMode.Callback {
    private var favoriterecipes= emptyList<FavoritesEntity>()
    private var multiSelection=false
    private lateinit var mActionMode: ActionMode
    private lateinit var rootView:View
    private var myVieHolders= arrayListOf<MyViewHolder>()
    private var selectedRecipes= arrayListOf<FavoritesEntity>()
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
        myVieHolders.add(holder)
        rootView=holder.itemView.rootView
        val currentRecipe=favoriterecipes[position]
        holder.bind(currentRecipe)

        //single Click listeners

        holder.itemView.favoriteRecipesRowLayout.setOnClickListener {
            if (multiSelection){
                applySelection(holder,currentRecipe)
            }
            else{
                val action=
                    FavoriteRecipesFragmentDirections.actionFavoriteRecipesFragmentToDetailsActivity(currentRecipe.result)
                holder.itemView.findNavController().navigate(action)

            }

        }


        //long click Listeners
        holder.itemView.favoriteRecipesRowLayout.setOnLongClickListener {
            if (!multiSelection){
                multiSelection=true
                requiredActivity.startActionMode(this)
                applySelection(holder,currentRecipe)
                true
            }else{
                 multiSelection=false
                false
            }

        }
    }

    private fun applySelection(holder:MyViewHolder,currentRecipe:FavoritesEntity){
        if (selectedRecipes.contains(currentRecipe)){
            selectedRecipes.remove(currentRecipe)
            changeRecipeStyle(holder,R.color.cardBackgroundColor,R.color.strokeColor)
            applyActionModeTitle()
        }else{
            selectedRecipes.add(currentRecipe)
            changeRecipeStyle(holder,R.color.cardBackgroundLightColor,R.color.colorPrimary)
            applyActionModeTitle()
        }
    }
    private fun changeRecipeStyle(holder:MyViewHolder,backgroundColor:Int,strokeColor:Int){
        holder.itemView.favoriteRecipesRowLayout.setBackgroundColor(
            ContextCompat.getColor(requiredActivity,backgroundColor)
        )
        holder.itemView.favorite_row_cardView.strokeColor=
            ContextCompat.getColor(requiredActivity,strokeColor)
    }

    private fun applyActionModeTitle(){
        when(selectedRecipes.size){
            0->{
                mActionMode.finish()
            }
            1->{
                mActionMode.title="${selectedRecipes.size} item Selected"
            }
            else->{
                mActionMode.title="${selectedRecipes.size} items Selected"
            }
        }
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

    override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
       mode?.menuInflater?.inflate(R.menu.favorite_contextual_menu,menu)
        mActionMode=mode!!
        applyStatusBarColor(R.color.contextualStatusBarColor)
        return true
    }

    override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean {
        return true
    }

    override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean {
        if (item?.itemId==R.id.delete_favorite_recipe_menu){
            selectedRecipes.forEach {
                 mainViewModel.deleteFavoriteRecipe(it)

            }
            showSnackBar("${selectedRecipes.size} Recipe/s Deleted")

            multiSelection=false
            selectedRecipes.clear()
            mActionMode?.finish()
        }
        return true
    }

    override fun onDestroyActionMode(mode: ActionMode?) {
        myVieHolders.forEach{holder->
        changeRecipeStyle(holder,R.color.cardBackgroundColor,R.color.strokeColor)}
        multiSelection=false
        selectedRecipes.clear()
        applyStatusBarColor(R.color.statusBarColor)
    }
    private fun applyStatusBarColor(color:Int){
        requiredActivity.window.statusBarColor=
            ContextCompat.getColor(requiredActivity,color)

    }
    private fun showSnackBar(string: String){
        Snackbar.make(
            rootView,
            string,
            Snackbar.LENGTH_SHORT
        ).setAction("okay"){}
            .show()
    }
    fun clearContextualActionMode(){
        if(this::mActionMode.isInitialized){
            mActionMode.finish()
        }
    }
}

