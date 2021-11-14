package com.abdullah996.g3an.data.database

import androidx.room.TypeConverter
import com.abdullah996.g3an.model.FoodRecipe
import com.abdullah996.g3an.model.Result
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class RecipesTypeConverter {
    val gson= Gson()

    @TypeConverter
    fun foodRecipeToString(foodRecipe: FoodRecipe):String{
        return gson.toJson(foodRecipe)
    }
    @TypeConverter
    fun stringToFoodRecipe(string: String):FoodRecipe{
        val listType=object :TypeToken<FoodRecipe>() {}.type
        return gson.fromJson(string,listType)
    }
    @TypeConverter
    fun resultToString(result: Result):String{
        return gson.toJson(result)
    }
    @TypeConverter
    fun stringTOResult(string: String):Result{
        val listType=object :TypeToken<Result>() {}.type
        return gson.fromJson(string,listType)
    }
}