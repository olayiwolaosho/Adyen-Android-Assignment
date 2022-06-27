package com.adyen.android.assignment.ui.apods

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adyen.android.assignment.api.model.AstronomyPicture
import com.adyen.android.assignment.data.*
import com.adyen.android.assignment.data.db.AstronomyPictureEnt
import com.adyen.android.assignment.data.db.FavouriteAstronomyPictureEnt
import com.adyen.android.assignment.data.extensions.toAstronomyPictureEnt
import com.adyen.android.assignment.data.extensions.toFavouritePictureEnt
import com.adyen.android.assignment.data.repo.PlanetaryRepo
import com.adyen.android.assignment.util.exception.NoConnectivityException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class ApodsViewModel @Inject constructor(
    private val planetaryRepo : PlanetaryRepo,
) : ViewModel() {

    var currentSortTag = NO_SORT_TAG

    private var allApods = SingleLiveEvent<Resource<List<AstronomyPictureEnt>>>()

    private val filteredApods = SingleLiveEvent<Resource<List<AstronomyPictureEnt>>>()

    private val favouriteApods = SingleLiveEvent<Resource<MutableList<FavouriteAstronomyPictureEnt>>>()

    fun allApods() : SingleLiveEvent<Resource<List<AstronomyPictureEnt>>>{

        return allApods

    }

    fun filteredApods() : SingleLiveEvent<Resource<List<AstronomyPictureEnt>>>{

        return filteredApods

    }

    fun favouriteApods() : SingleLiveEvent<Resource<MutableList<FavouriteAstronomyPictureEnt>>>{

        return favouriteApods

    }

    fun isApodsAvailable() : Boolean{

        //get all favourite apods
        getFavouriteApods()

        //check if we have data in our liveEvents so we that instead of calling api or db
        if(filterApods() || getAllApods()){
            return true
        }

        return false

    }

    fun getApods(){

        if(isApodsAvailable()) return

        allApods.value = Resource.loading()

        viewModelScope.launch(Dispatchers.Main) {

            getApodsFromService()

        }
    }

    fun getApodsFromDb(){

        if(isApodsAvailable()) return

        allApods.value = Resource.loading()

        viewModelScope.launch(Dispatchers.Main) {

            val favouriteApodsFromDb : MutableList<FavouriteAstronomyPictureEnt> = withContext(CoroutineScope(Dispatchers.IO).coroutineContext) {

                planetaryRepo.getFavouritePicturesFromDB()

            }

            val apodsFromDb : List<AstronomyPictureEnt> = withContext(CoroutineScope(Dispatchers.IO).coroutineContext) {

                planetaryRepo.getPicturesFromDB()

            }

            favouriteApods.value = Resource.success(favouriteApodsFromDb)

            allApods.value = Resource.success(apodsFromDb)

        }

    }

    fun filterApods() : Boolean{

        if(currentSortTag != NO_SORT_TAG){

            filteredApods.value = Resource.success(filteredApods.value?.data)

            return true

        }

        return false

    }

    fun getFavouriteApods(){

        if(favouriteApods.value?.data != null){

            favouriteApods.value = Resource.success(favouriteApods.value?.data)

        }

    }

    fun getAllApods() : Boolean{

        if(allApods.value?.data != null){

            allApods.value = Resource.success(allApods.value?.data)

            return true

        }

        return false

    }

    suspend fun getApodsFromService(){
        //make api call
        try {

            val response = getApodResponse()

            if(response.isSuccessful){

                val images = getImagesFromResponse(response.body())

                images?.let {

                    withContext(CoroutineScope(Dispatchers.IO).coroutineContext
                    ) {

                        //remove appPictures from db before adding new data
                        planetaryRepo.removeAllPictureFromDB()

                    }

                    val result : MutableList<FavouriteAstronomyPictureEnt> = withContext(
                            CoroutineScope(Dispatchers.IO).coroutineContext
                        ) {

                            //add images to db this represents allApods
                            planetaryRepo.addAllPicturesToDb(images)

                            planetaryRepo.getFavouritePicturesFromDB()

                        }

                    val imagesFromDb : List<AstronomyPictureEnt> = withContext(
                        CoroutineScope(Dispatchers.IO).coroutineContext) {

                            planetaryRepo.getPicturesFromDB()

                        }

                    favouriteApods.value = Resource.success(result)

                    allApods.value = Resource.success(imagesFromDb)

                }

                return

            }

        } catch (e: NoConnectivityException) {

            allApods.value = Resource.noNetwork()

            return
        }
        catch (e: IOException) {

            allApods.value = Resource.error("")

            return
        }

        allApods.value = Resource.error("")

    }

    fun getImagesFromResponse(responseBody : List<AstronomyPicture>?) : List<AstronomyPictureEnt>?{

        return  responseBody?.filter { data ->

            data.mediaType == IMAGE

        }?.map { astronomyPic ->

            astronomyPic.toAstronomyPictureEnt()

        }

    }

    suspend fun getApodResponse(): Response<List<AstronomyPicture>> {

        return CoroutineScope(Dispatchers.IO).async{

            return@async planetaryRepo.getPictures()

        }.await()

    }

    fun addFilter(sortTag : Int) {

        when(sortTag){

            TITLE_SORT_TAG -> {

                val sortByTitle = allApods.value?.data

                filteredApods.value = Resource.success(sortByTitle?.sortedBy { it.title })

                currentSortTag = TITLE_SORT_TAG
            }

            DATE_SORT_TAG -> {

                val sortByDate = allApods.value?.data

                filteredApods.value = Resource.success(sortByDate?.sortedByDescending { it.date })

                currentSortTag = DATE_SORT_TAG
            }

        }

    }

    fun addFavourite(astronomyPictureId : Int) {

        viewModelScope.launch(Dispatchers.Main) {

            val favouriteData = allApods.value?.data?.get(astronomyPictureId)

            favouriteData?.toFavouritePictureEnt()?.let { favourite ->

                if(favourite.favourite){

                    withContext(Dispatchers.IO) {

                        planetaryRepo.addFavouritePictureToDB(favourite)

                    }


                    favouriteApods.value?.data?.add(FIRST_INDEX,favourite)

                }
                else{

                    withContext(Dispatchers.IO) {

                        planetaryRepo.removeFavouritePictureFromDB(favourite)

                    }

                    favouriteApods.value?.data?.remove(favourite)

                }

                favouriteApods.value = Resource.success(favouriteApods.value?.data)

            }

        }

    }

    fun removeFilter() {

        currentSortTag = NO_SORT_TAG

        allApods.value = Resource.success(allApods.value?.data)

    }


    companion object{

        const val FIRST_INDEX = 0

    }

}