package com.adyen.android.assignment.data.repo

import android.provider.SyncStateContract
import androidx.lifecycle.viewModelScope
import com.adyen.android.assignment.SharedPreferenceManager
import com.adyen.android.assignment.api.PlanetaryService
import com.adyen.android.assignment.api.model.AstronomyPicture
import com.adyen.android.assignment.data.*
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
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.Dispatcher
import retrofit2.Response
import java.io.IOException
import java.time.LocalDate
import javax.inject.Inject

class PlanetaryRepoImpl @Inject constructor(
    private val planetaryService : PlanetaryService,
    private val planetaryDbRepo: PlanetaryDbRepo,
    var prefManager : SharedPreferenceManager
) : PlanetaryRepo {

    override suspend fun getAllPictures(): Flow<Resource<List<AstronomyPictureEnt>>> {

        return flow<Resource<List<AstronomyPictureEnt>>> {

            if(prefManager.getStringItem(DATE_TODAY) == LocalDate.now().toString()){

                getApodsFromDB().collect(){ resource ->

                    emit(resource)

                }

            }
            else{

                getPictures().collect(){ resource ->

                    emit(resource)

                }

            }

        }.flowOn(Dispatchers.IO)

    }

    private suspend fun getPictures(): Flow<Resource<List<AstronomyPictureEnt>>> {

        return channelFlow<Resource<List<AstronomyPictureEnt>>> {

            send(Resource.loading())

            try {

                val response = planetaryService.getPictures()

                if(response.isSuccessful){

                    val responseEnt = response.body()?.filter { data ->

                        data.mediaType == IMAGE

                    }?.map { astronomyPic ->

                        astronomyPic.toAstronomyPictureEnt()

                    }

                    responseEnt?.let { allImages ->

                        send(Resource.success(allImages))

                        //remove appPictures from db before adding new data
                        planetaryDbRepo.removeAllPictureFromDB()


                        //add images to db this represents allApods
                        planetaryDbRepo.addAllPicturesToDb(allImages)

                        //todo : favourites should have its own function and observer
                        //planetaryDbRepo.getFavouritePicturesFromDB()

                        if(prefManager.getStringItem(DATE_TODAY) != LocalDate.now().toString()){

                            prefManager.saveItem(DATE_TODAY,LocalDate.now().toString())

                        }

                    }

                }

            } catch (e: NoConnectivityException) {

                send(Resource.noNetwork())

            }
            catch (e: IOException) {

                send(Resource.error(""))

            }

        }

    }

    private suspend fun getApodsFromDB(): Flow<Resource<List<AstronomyPictureEnt>>> {

        return channelFlow {

            send(Resource.loading())

            planetaryDbRepo.getPicturesFromDB().collectLatest { latest ->

                send(Resource.success(latest))

            }

        }

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