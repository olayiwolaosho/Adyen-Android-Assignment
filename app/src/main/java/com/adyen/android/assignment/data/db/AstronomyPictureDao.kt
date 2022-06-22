package com.adyen.android.assignment.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface AstronomyPictureDao {

    @Query("SELECT * FROM AstronomyPictureEnt")
    fun getAll(): List<AstronomyPictureEnt>

    @Query("SELECT * FROM FavouriteAstronomyPictureEnt")
    fun getAllFavourites(): MutableList<FavouriteAstronomyPictureEnt>

    @Insert
    fun insert(picture: FavouriteAstronomyPictureEnt)

    @Insert
    fun insertAll(vararg pictures: AstronomyPictureEnt)

    @Query("DELETE FROM AstronomyPictureEnt")
    fun deleteAll()

    @Delete
    fun deleteFavourite(picture: FavouriteAstronomyPictureEnt)
}