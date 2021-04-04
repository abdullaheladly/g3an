package com.abdullah996.g3an.data

import android.content.Context
import androidx.datastore.DataStore
import androidx.datastore.preferences.*
import com.abdullah996.g3an.util.Constants.Companion.DEFAULT_DIET_TYPE
import com.abdullah996.g3an.util.Constants.Companion.DEFAULT_MEAL_TYPE
import com.abdullah996.g3an.util.Constants.Companion.PREFERENCES_DIET_TYPE
import com.abdullah996.g3an.util.Constants.Companion.PREFERENCES_DIET_TYPE_ID
import com.abdullah996.g3an.util.Constants.Companion.PREFERENCES_MEAL_TYPE
import com.abdullah996.g3an.util.Constants.Companion.PREFERENCES_MEAL_TYPE_ID
import com.abdullah996.g3an.util.Constants.Companion.PREFERENCES_NAME
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import okio.IOException
import javax.inject.Inject

@ActivityRetainedScoped
class DataStoreRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private object PreferenceKeys{
        val selectedMealType= preferencesKey<String>(PREFERENCES_MEAL_TYPE)
        val selectedMealTypeId= preferencesKey<Int>(PREFERENCES_MEAL_TYPE_ID)
        val selectedDietType= preferencesKey<String>(PREFERENCES_DIET_TYPE)
        val selectedDietTypeId= preferencesKey<Int>(PREFERENCES_DIET_TYPE_ID)
    }
    private val dataStore:DataStore<Preferences> = context.createDataStore(
        name = PREFERENCES_NAME
    )

    suspend fun saveMealAndDietType(mealType:String,mealTypeId:Int,dietType:String,dietTypeId:Int){
        dataStore.edit { preferences->
            preferences[PreferenceKeys.selectedMealType]=mealType
            preferences[PreferenceKeys.selectedMealTypeId]=mealTypeId
            preferences[PreferenceKeys.selectedDietType]=dietType
            preferences[PreferenceKeys.selectedDietTypeId]=dietTypeId
        }
    }

    val readMealAndDietType:Flow<MealAndDietType> = dataStore.data
        .catch {ex->
            if (ex is IOException){
                emit(emptyPreferences())
            }else{
                throw  ex
            }
        }.map { preferences->
            val selectedMealType=preferences[PreferenceKeys.selectedMealType]?: DEFAULT_MEAL_TYPE
            val selectedMealTypeId=preferences[PreferenceKeys.selectedMealTypeId]?: 0
            val selectedDietType=preferences[PreferenceKeys.selectedDietType]?: DEFAULT_DIET_TYPE
            val selectedDietTypeId=preferences[PreferenceKeys.selectedDietTypeId]?: 0
            MealAndDietType(
                selectedMealType,
                selectedMealTypeId,
                selectedDietType,
                selectedDietTypeId
            )
        }

}

data class MealAndDietType(
    val selectedMealType:String,
    val selectedMealTypeID:Int,
    val selectedDietType:String,
    val selectedDietTypeId:Int

)