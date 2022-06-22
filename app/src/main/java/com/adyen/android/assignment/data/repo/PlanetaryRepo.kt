package com.adyen.android.assignment.data.repo

import com.adyen.android.assignment.api.model.AstronomyPicture
import com.adyen.android.assignment.data.db.AstronomyPictureEnt
import com.adyen.android.assignment.data.db.FavouriteAstronomyPictureEnt
import retrofit2.Response

interface PlanetaryRepo {

    suspend fun getPictures() : Response<List<AstronomyPicture>>

    fun addAllPicturesToDb(allPictures : List<AstronomyPictureEnt>)

    fun getPicturesFromDB() : List<AstronomyPictureEnt>

    fun getFavouritePicturesFromDB() : MutableList<FavouriteAstronomyPictureEnt>

    fun addFavouritePictureToDB(favouriteAstronomyPicture : FavouriteAstronomyPictureEnt)

    fun removeFavouritePictureFromDB(favouriteAstronomyPicture : FavouriteAstronomyPictureEnt)

    suspend fun removeAllPictureFromDB()

}