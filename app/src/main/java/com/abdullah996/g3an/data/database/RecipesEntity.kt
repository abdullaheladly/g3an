package com.abdullah996.g3an.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.abdullah996.g3an.model.FoodRecipe
import com.abdullah996.g3an.util.Constants.Companion.RECIPES_TABLE


@Entity(tableName = RECIPES_TABLE)
class RecipesEntity(
        var foodRecipe: FoodRecipe
) {
    @PrimaryKey(autoGenerate = false)
    var id:Int=0
}