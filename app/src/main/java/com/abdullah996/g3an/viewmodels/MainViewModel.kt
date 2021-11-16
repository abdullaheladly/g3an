package com.abdullah996.g3an.viewmodels

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.abdullah996.g3an.data.Repository
import com.abdullah996.g3an.data.database.entities.FavoritesEntity
import com.abdullah996.g3an.data.database.entities.RecipesEntity
import com.abdullah996.g3an.model.FoodRecipe
import com.abdullah996.g3an.util.NetworkResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import java.lang.Exception

class  MainViewModel @ViewModelInject constructor (
        private val repository: Repository,
        application: Application
):AndroidViewModel(application) {


    /** Room */

    val readRecipes:LiveData<List<RecipesEntity>> = repository.local.readRecipes().asLiveData()
    val readFavoriteRecipes:LiveData<List<FavoritesEntity>> = repository.local.readFavoriteRecipes().asLiveData()


    private fun insertRecipes(recipesEntity: RecipesEntity)=viewModelScope.launch(Dispatchers.IO) {
        repository.local.insertRecipes(recipesEntity)

    }
     fun insertFavoriteRecipe(favoritesEntity: FavoritesEntity)=viewModelScope.launch(Dispatchers.IO) {
        repository.local.insertFavoriteRecipes(favoritesEntity)

    }
     fun deleteFavoriteRecipe(favoritesEntity: FavoritesEntity)=viewModelScope.launch(Dispatchers.IO) {
        repository.local.deleteFavoriteRecipe(favoritesEntity)
    }
     fun deleteAllFavoriteRecipes()=viewModelScope.launch(Dispatchers.IO) {
        repository.local.deleteAllFavoriteRecipes()
    }





    /** Retrofit */
    var recipeResponse:MutableLiveData<NetworkResult<FoodRecipe>> = MutableLiveData()

    fun getRecipes(queries: Map<String,String>)=viewModelScope.launch {
        getRecipesSafeCall(queries)
    }

    private suspend fun getRecipesSafeCall(queries: Map<String, String>) {
        recipeResponse.value=NetworkResult.Loading()
        if (hasInternetConnection()){
            try {
                val response= repository.remote.getRecipes(queries)
                recipeResponse.value=handleFoodRecipesResponse(response)

                val foodRecipe=recipeResponse.value!!.data
                if (foodRecipe!=null){
                    offlineCacheRecipes(foodRecipe)
                }
            }catch (e:Exception){
                recipeResponse.value=NetworkResult.Error("recipes not found")

            }
        }
        else{
            recipeResponse.value=NetworkResult.Error("no internet connection. ")
        }
    }

    private fun offlineCacheRecipes(foodRecipe: FoodRecipe) {
            val recipesEntity= RecipesEntity(foodRecipe)
            insertRecipes(recipesEntity)
    }

    private fun handleFoodRecipesResponse(response: Response<FoodRecipe>): NetworkResult<FoodRecipe>? {
        when{
            response.message().toString().contains("timeout")->{
                return NetworkResult.Error("Timeout")
            }
            response.code()==402 ->{
                return NetworkResult.Error("Api key Limited.")
            }
            response.body()!!.results.isNullOrEmpty()->{
                return NetworkResult.Error("recipes not found.")
            }
            response.isSuccessful->{
                val foodRecipes=response.body()
                return NetworkResult.Success(foodRecipes!!)
            }
           else -> {
               return NetworkResult.Error(response.message())
           }
        }
    }

    private fun hasInternetConnection():Boolean{
        val connectivityManager=getApplication<Application>().getSystemService(
                Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        val activeNetwork=connectivityManager.activeNetwork?:return false
        val capabilities=connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
        return  when{
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ->true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    }
}