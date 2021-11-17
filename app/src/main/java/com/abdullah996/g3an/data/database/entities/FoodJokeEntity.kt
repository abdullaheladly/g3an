package com.abdullah996.g3an.data.database.entities

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.abdullah996.g3an.model.FoodJoke
import com.abdullah996.g3an.util.Constants.Companion.FOOD_JOKE_TABLE


@Entity(tableName = FOOD_JOKE_TABLE)
class FoodJokeEntity (
    @Embedded
    var foodJoke: FoodJoke
    ) {
        @PrimaryKey(autoGenerate = false)
        var id: Int = 0
    }
