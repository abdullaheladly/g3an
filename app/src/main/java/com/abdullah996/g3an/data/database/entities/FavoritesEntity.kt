package com.abdullah996.g3an.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.abdullah996.g3an.model.Result
import com.abdullah996.g3an.util.Constants.Companion.FAVORITE_RECIPES_TABLE


@Entity(tableName = FAVORITE_RECIPES_TABLE)
class FavoritesEntity(
    @PrimaryKey(autoGenerate = true)
    var id:Int,
    var result: Result
) {
}