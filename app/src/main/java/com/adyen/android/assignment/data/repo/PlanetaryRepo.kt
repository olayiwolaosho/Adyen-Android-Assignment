package com.adyen.android.assignment.data.repo

import com.adyen.android.assignment.api.model.AstronomyPicture
import retrofit2.Response

interface PlanetaryRepo {

    suspend fun getPictures() : Response<List<AstronomyPicture>>

}