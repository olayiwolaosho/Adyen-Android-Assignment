package com.adyen.android.assignment.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface AstronomyPictureDao {

    @Query("SELECT * FROM AstronomyPictureEnt")
    fun getAll(): Flow<List<AstronomyPictureEnt>>

    @Query("SELECT * FROM FavouriteAstronomyPictureEnt")
    fun getAllFavourites(): Flow<MutableList<FavouriteAstronomyPictureEnt>>

    @Insert
    suspend fun insert(picture: FavouriteAstronomyPictureEnt)

    @Insert
    suspend fun insertAll(vararg pictures: AstronomyPictureEnt)

    @Query("DELETE FROM AstronomyPictureEnt")
    suspend fun deleteAll()

    @Delete
    suspend fun deleteFavourite(picture: FavouriteAstronomyPictureEnt)
}