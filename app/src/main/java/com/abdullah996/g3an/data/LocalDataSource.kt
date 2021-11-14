package com.abdullah996.g3an.data

import com.abdullah996.g3an.data.database.RecipesDao
import com.abdullah996.g3an.data.database.entities.FavoritesEntity
import com.abdullah996.g3an.data.database.entities.RecipesEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalDataSource @Inject constructor(
        private val recipesDao: RecipesDao
) {

    fun readRecipes():Flow<List<RecipesEntity>>{
        return recipesDao.readRecipes()
    }

    fun readFavoriteRecipes():Flow<List<FavoritesEntity>>{
        return recipesDao.readFavoriteRecipes()
    }



    suspend fun insertRecipes(recipesEntity: RecipesEntity) {
        recipesDao.insertRecipes(recipesEntity)
    }
    suspend fun insertFavoriteRecipes(favoritesEntity: FavoritesEntity) {
        recipesDao.insertFavoriteRecipes(favoritesEntity)
    }
    suspend fun deleteFavoriteRecipe(favoritesEntity: FavoritesEntity){
        recipesDao.deleteFavoriteRecipes(favoritesEntity)
    }
    suspend fun deleteAllFavoriteRecipes(){
        recipesDao.deleteAllFavoriteRecipes()
    }
}