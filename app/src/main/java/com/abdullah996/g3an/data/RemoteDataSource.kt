package com.abdullah996.g3an.data

import com.abdullah996.g3an.data.network.FoodRecipesApi
import com.abdullah996.g3an.model.FoodRecipe
import retrofit2.Response
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
        private val foodRecipesApi: FoodRecipesApi
) {
    suspend fun getRecipes(queries:Map<String,String>) : Response<FoodRecipe>{
        return foodRecipesApi.getRecipes(queries)
    }
}