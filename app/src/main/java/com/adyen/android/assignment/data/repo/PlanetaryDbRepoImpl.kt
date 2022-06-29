package com.adyen.android.assignment.data.repo

import com.adyen.android.assignment.data.db.AstronomyPictureDao
import com.adyen.android.assignment.data.db.AstronomyPictureEnt
import com.adyen.android.assignment.data.db.FavouriteAstronomyPictureEnt
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PlanetaryDbRepoImpl @Inject constructor(
    private val astronomyPictureDao: AstronomyPictureDao
) : PlanetaryDbRepo {

    override suspend fun addAllPicturesToDb(allPictures: List<AstronomyPictureEnt>) {

        astronomyPictureDao.insertAll(*allPictures.toTypedArray())

    }

    override fun getPicturesFromDB() = astronomyPictureDao.getAll()


    override fun getFavouritePicturesFromDB() = astronomyPictureDao.getAllFavourites()


    override suspend fun addFavouritePictureToDB(favouriteAstronomyPicture: FavouriteAstronomyPictureEnt) {

        astronomyPictureDao.insert(favouriteAstronomyPicture)

    }

    override suspend fun removeFavouritePictureFromDB(favouriteAstronomyPicture: FavouriteAstronomyPictureEnt) {

        astronomyPictureDao.deleteFavourite(favouriteAstronomyPicture)

    }

    override suspend fun removeAllPictureFromDB() {

        astronomyPictureDao.deleteAll()

    }

}