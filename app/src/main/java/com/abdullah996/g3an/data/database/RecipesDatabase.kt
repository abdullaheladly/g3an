package com.abdullah996.g3an.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.abdullah996.g3an.data.database.entities.FavoritesEntity
import com.abdullah996.g3an.data.database.entities.RecipesEntity

@Database(
        entities = [RecipesEntity::class,FavoritesEntity::class],
        version = 1,
        exportSchema = false
)
@TypeConverters(RecipesTypeConverter::class)
abstract class RecipesDatabase:RoomDatabase() {

    abstract fun recipesDau(): RecipesDao
}