package com.adyen.android.assignment.data.repo

import com.adyen.android.assignment.api.model.AstronomyPicture
import com.adyen.android.assignment.data.Resource
import com.adyen.android.assignment.data.db.AstronomyPictureEnt
import com.adyen.android.assignment.data.db.FavouriteAstronomyPictureEnt
import com.adyen.android.assignment.ui.UiState
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface PlanetaryRepo {

    suspend fun getAllPictures() : Flow<Resource<List<AstronomyPictureEnt>>>

    suspend fun editFavourite(favourite : FavouriteAstronomyPictureEnt) : Flow<Resource<String>>

}