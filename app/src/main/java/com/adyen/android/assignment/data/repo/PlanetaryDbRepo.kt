package com.adyen.android.assignment.data.repo

import com.adyen.android.assignment.data.db.AstronomyPictureEnt
import com.adyen.android.assignment.data.db.FavouriteAstronomyPictureEnt
import kotlinx.coroutines.flow.Flow

interface PlanetaryDbRepo {

    suspend fun addAllPicturesToDb(allPictures : List<AstronomyPictureEnt>)

    fun getPicturesFromDB() : Flow<List<AstronomyPictureEnt>>

    fun getFavouritePicturesFromDB() : Flow<MutableList<FavouriteAstronomyPictureEnt>>

    suspend fun addFavouritePictureToDB(favouriteAstronomyPicture : FavouriteAstronomyPictureEnt)

    suspend fun removeFavouritePictureFromDB(favouriteAstronomyPicture : FavouriteAstronomyPictureEnt)

    suspend fun removeAllPictureFromDB()

}