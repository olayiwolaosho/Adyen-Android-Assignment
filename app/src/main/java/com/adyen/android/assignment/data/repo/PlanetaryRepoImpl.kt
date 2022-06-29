package com.adyen.android.assignment.data.repo

import com.adyen.android.assignment.api.PlanetaryService
import com.adyen.android.assignment.api.model.AstronomyPicture
import com.adyen.android.assignment.data.Resource
import com.adyen.android.assignment.data.db.AstronomyPictureDao
import com.adyen.android.assignment.data.db.AstronomyPictureEnt
import com.adyen.android.assignment.data.db.FavouriteAstronomyPictureEnt
import com.adyen.android.assignment.ui.UiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.Dispatcher
import retrofit2.Response
import javax.inject.Inject

class PlanetaryRepoImpl @Inject constructor(
    private val planetaryService : PlanetaryService,
    private val planetaryDbRepo: PlanetaryDbRepo
) : PlanetaryRepo {

    override suspend fun getPictures(): Response<List<AstronomyPicture>> {

        return planetaryService.getPictures()

    }

    override suspend fun getApodsUiState(): Flow<Resource<List<AstronomyPictureEnt>>> {

        return flow {

            emit(Resource.loading())

            planetaryDbRepo.getPicturesFromDB().collectLatest { latest ->

                emit(Resource.success(latest))

            }

        }.flowOn(Dispatchers.IO)

    }

}