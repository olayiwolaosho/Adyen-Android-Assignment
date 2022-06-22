package com.adyen.android.assignment.data.repo

import com.adyen.android.assignment.api.PlanetaryService
import com.adyen.android.assignment.api.model.AstronomyPicture
import com.adyen.android.assignment.data.db.AstronomyPictureDao
import com.adyen.android.assignment.data.db.AstronomyPictureEnt
import com.adyen.android.assignment.data.db.FavouriteAstronomyPictureEnt
import retrofit2.Response
import javax.inject.Inject

class PlanetaryRepoImpl @Inject constructor(
    private val planetaryService : PlanetaryService,
    private val astronomyPictureDao: AstronomyPictureDao
) : PlanetaryRepo {

    override suspend fun getPictures(): Response<List<AstronomyPicture>> {

        return planetaryService.getPictures()

    }

    override fun addAllPicturesToDb(allPictures : List<AstronomyPictureEnt>) {

        astronomyPictureDao.insertAll(*allPictures.toTypedArray())

    }

    override fun getPicturesFromDB(): List<AstronomyPictureEnt> {

        return astronomyPictureDao.getAll()

    }

    override fun getFavouritePicturesFromDB(): MutableList<FavouriteAstronomyPictureEnt> {

        return astronomyPictureDao.getAllFavourites()

    }

    override fun addFavouritePictureToDB(favouriteAstronomyPicture : FavouriteAstronomyPictureEnt) {

        astronomyPictureDao.insert(favouriteAstronomyPicture)

    }

    override fun removeFavouritePictureFromDB(favouriteAstronomyPicture : FavouriteAstronomyPictureEnt) {

        astronomyPictureDao.deleteFavourite(favouriteAstronomyPicture)

    }

    override suspend fun removeAllPictureFromDB() {

        astronomyPictureDao.deleteAll()

    }

}