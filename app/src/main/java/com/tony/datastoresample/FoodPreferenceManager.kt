package com.tony.datastoresample

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.datastore.core.CorruptionException
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.core.Serializer
import androidx.datastore.preferences.SharedPreferencesMigration
import com.google.protobuf.InvalidProtocolBufferException
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream


enum class FoodTaste {
    SWEET, SPICY
}

enum class FoodType {
    VEG, NON_VEG
}

data class Food(val name: String, val type: FoodType, val taste: FoodTaste)

data class UserFoodPreference(val type: FoodType?, val taste: FoodTaste?)

class FoodPreferenceManager(val context: Context) {

    private val dataStore: DataStore<FoodPreferences> = DataStoreFactory.create(FoodPreferenceSerializer(), produceFile = { return@create File("food_prefs") })

    companion object {
        const val TAG = "FoodPreferenceManager"
    }

    val userFoodPreference = dataStore.data.catch {
        listOf(SharedPreferencesMigration(context, "shared_prefs"))
        if (it is IOException) {
            Log.e(TAG, "Error reading sort order preferences.", it)
            emit(FoodPreferences.getDefaultInstance())
        } else {
            throw it
        }
    }.map {
        val type = when (it.type) {
            FoodPreferences.FoodType.TYPE_VEG -> FoodType.VEG
            FoodPreferences.FoodType.TYPE_NON_VEG -> FoodType.NON_VEG
            else -> null
        }
        val taste = when (it.taste) {
            FoodPreferences.FoodTaste.TASTE_SWEET -> FoodTaste.SWEET
            FoodPreferences.FoodTaste.TASTE_SPICY -> FoodTaste.SPICY
            else -> null
        }

        UserFoodPreference(type, taste)
    }

    suspend fun updateUserFoodTypePreference(type: FoodType?) {

        val foodType = when (type) {
            FoodType.VEG -> FoodPreferences.FoodType.TYPE_VEG
            FoodType.NON_VEG -> FoodPreferences.FoodType.TYPE_NON_VEG
            null -> FoodPreferences.FoodType.TYPE_UNSPECIFIED
        }

        dataStore.updateData { preferences ->
            preferences.toBuilder()
                .setType(foodType)
                .build()
        }
    }


    suspend fun updateUserFoodTastePreference(taste: FoodTaste?) {

        val foodTaste = when (taste) {
            FoodTaste.SWEET -> FoodPreferences.FoodTaste.TASTE_SWEET
            FoodTaste.SPICY -> FoodPreferences.FoodTaste.TASTE_SPICY
            null -> FoodPreferences.FoodTaste.TASTE_UNSPECIFIED
        }

        dataStore.updateData { preferences ->
            preferences.toBuilder()
                .setTaste(foodTaste)
                .build()
        }
    }






}


class FoodPreferenceSerializer: Serializer<FoodPreferences> {

    override suspend fun readFrom(input: InputStream): FoodPreferences {

        try {
            return FoodPreferences.parseFrom(input)
        } catch (e: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto", e)
        }
    }

    override suspend fun writeTo(t: FoodPreferences, output: OutputStream) {
        t.writeTo(output)
    }

    override val defaultValue: FoodPreferences
        get() = TODO("Not yet implemented")


}