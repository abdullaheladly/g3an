package com.abdullah996.g3an.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.navArgs
import com.abdullah996.g3an.R
import com.abdullah996.g3an.adapters.PagerAdapter
import com.abdullah996.g3an.data.database.entities.FavoritesEntity
import com.abdullah996.g3an.ui.fragments.ingredients.IngredientsFragment
import com.abdullah996.g3an.ui.fragments.instructions.InstructionsFragment
import com.abdullah996.g3an.ui.fragments.overview.OverViewFragment
import com.abdullah996.g3an.util.Constants.Companion.RECIPE_RESULT_KEY
import com.abdullah996.g3an.viewmodels.MainViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_details.*


@AndroidEntryPoint
class DetailsActivity : AppCompatActivity() {
    private val args by navArgs<DetailsActivityArgs>()
    private val mainViewModel:MainViewModel by viewModels()
    private var recipeSaved= false
    private var savedRecipeId=0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)
        setSupportActionBar(toolbar)
        toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.white))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val fragments = ArrayList<Fragment>()
        fragments.add(OverViewFragment())
        fragments.add(IngredientsFragment())
        fragments.add(InstructionsFragment())

        val titles = ArrayList<String>()
        titles.add("Overview")
        titles.add("Ingredients")
        titles.add("Instructions")

        val resultBundle = Bundle()
        resultBundle.putParcelable(RECIPE_RESULT_KEY, args.result)

        val adapter = PagerAdapter(
            resultBundle,
            fragments,
            titles,
            supportFragmentManager
        )
        viewPager.adapter = adapter
        tabLayout.setupWithViewPager(viewPager)

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.details_menu,menu)
        val menuItem=menu?.findItem(R.id.save_to_favorites)
        checkSavedRecipes(menuItem!!)
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }else if( item.itemId==R.id.save_to_favorites){
            if (recipeSaved){
                removeFromFavorite(item)
            }else{
            saveToFavorites(item)}
        }
        return super.onOptionsItemSelected(item)
    }

    private fun checkSavedRecipes(menuItem: MenuItem) {
        mainViewModel.readFavoriteRecipes.observe(this,{favoritesEntity->
            try {
                for (savedRecipe in favoritesEntity){
                    if (savedRecipe.result.id==args.result.id){
                        changeMenuItemColor(menuItem,R.color.yellow)
                        savedRecipeId=savedRecipe.id
                        recipeSaved=true
                    }else{
                        changeMenuItemColor(menuItem,R.color.white)
                    }
                }
            }catch (e:Exception){
                Log.e("DetailsActivity",e.message.toString())
            }

        })

    }

    private fun saveToFavorites(item: MenuItem) {
        val favoritesEntity=
            FavoritesEntity(
                0,
                args.result
            )
        mainViewModel.insertFavoriteRecipe(favoritesEntity)
        changeMenuItemColor(item,R.color.yellow)
        showSnackBar("Recipe Saved")
        recipeSaved=true
    }

    private fun removeFromFavorite(menuItem: MenuItem){
        val favoritesEntity=
            FavoritesEntity(
                savedRecipeId,
                args.result
            )
        mainViewModel.deleteFavoriteRecipe(favoritesEntity)
        changeMenuItemColor(menuItem,R.color.white)
        showSnackBar("Recipe Removed  From Favourite")
        recipeSaved=false
    }

    private fun showSnackBar(s: String) {
        Snackbar.make(
            detailsLayout,
            s,
            Snackbar.LENGTH_SHORT
        ).setAction("Okay"){}.show()
    }

    private fun changeMenuItemColor(item: MenuItem, color: Int) {
        item.icon.setTint(ContextCompat.getColor(this,color))
    }
}