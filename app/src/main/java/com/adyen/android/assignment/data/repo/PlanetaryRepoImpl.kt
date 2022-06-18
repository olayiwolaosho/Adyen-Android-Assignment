package com.adyen.android.assignment.data.repo

import com.adyen.android.assignment.api.PlanetaryService
import com.adyen.android.assignment.api.model.AstronomyPicture
import com.adyen.android.assignment.data.Constants
import retrofit2.Response
import javax.inject.Inject

class PlanetaryRepoImpl @Inject constructor(
    private val planetaryService : PlanetaryService
) : PlanetaryRepo {

    override suspend fun getPictures(): Response<List<AstronomyPicture>> {

        return planetaryService.getPictures()

    }

}