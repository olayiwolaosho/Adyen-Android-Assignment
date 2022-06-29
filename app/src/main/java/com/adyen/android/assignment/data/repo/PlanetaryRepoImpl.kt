package com.adyen.android.assignment.data.repo

import android.provider.SyncStateContract
import androidx.lifecycle.viewModelScope
import com.adyen.android.assignment.api.PlanetaryService
import com.adyen.android.assignment.api.model.AstronomyPicture
import com.adyen.android.assignment.data.ADD_FAVOURITE
import com.adyen.android.assignment.data.IMAGE
import com.adyen.android.assignment.data.REMOVE_FAVOURITE
import com.adyen.android.assignment.data.Resource
import com.adyen.android.assignment.data.db.AstronomyPictureDao
import com.adyen.android.assignment.data.db.AstronomyPictureEnt
import com.adyen.android.assignment.data.db.FavouriteAstronomyPictureEnt
import com.adyen.android.assignment.data.extensions.toAstronomyPictureEnt
import com.adyen.android.assignment.data.extensions.toFavouritePictureEnt
import com.adyen.android.assignment.ui.UiState
import com.adyen.android.assignment.ui.apods.ApodsViewModel
import com.adyen.android.assignment.util.exception.NoConnectivityException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.Dispatcher
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject

class PlanetaryRepoImpl @Inject constructor(
    private val planetaryService : PlanetaryService,
    private val planetaryDbRepo: PlanetaryDbRepo
) : PlanetaryRepo {

    override suspend fun getPictures(): Flow<Resource<List<AstronomyPictureEnt>>> {

        return flow<Resource<List<AstronomyPictureEnt>>> {

            emit(Resource.loading())

            try {

                val response = planetaryService.getPictures()

                if(response.isSuccessful){

                    val responseEnt = response.body()?.filter { data ->

                        data.mediaType == IMAGE

                    }?.map { astronomyPic ->

                        astronomyPic.toAstronomyPictureEnt()

                    }

                    responseEnt?.let { allImages ->

                        emit(Resource.success(allImages))

                        //remove appPictures from db before adding new data
                        planetaryDbRepo.removeAllPictureFromDB()


                        //add images to db this represents allApods
                        planetaryDbRepo.addAllPicturesToDb(allImages)

                        //todo : favourites should have its own function and observer
                        //planetaryDbRepo.getFavouritePicturesFromDB()

                    }

                }

            } catch (e: NoConnectivityException) {

                emit(Resource.noNetwork())

            }
            catch (e: IOException) {

                emit(Resource.error(""))

            }

        }.flowOn(Dispatchers.IO)

    }

    override suspend fun getApodsUiState(): Flow<Resource<List<AstronomyPictureEnt>>> {

        return flow {

            emit(Resource.loading())

            planetaryDbRepo.getPicturesFromDB().collectLatest { latest ->

                emit(Resource.success(latest))

            }

        }.flowOn(Dispatchers.IO)

    }


    override suspend fun editFavourite(favourite : FavouriteAstronomyPictureEnt) : Flow<Resource<String>> {

        return flow {

            if(favourite.favourite){

                planetaryDbRepo.addFavouritePictureToDB(favourite)

                emit(Resource.success(ADD_FAVOURITE))

            }
            else{

                planetaryDbRepo.removeFavouritePictureFromDB(favourite)

                emit(Resource.success(REMOVE_FAVOURITE))

            }

        }

    }

}