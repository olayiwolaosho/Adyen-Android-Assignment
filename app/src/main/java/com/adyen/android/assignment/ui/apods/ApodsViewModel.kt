package com.adyen.android.assignment.ui.apods

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adyen.android.assignment.api.dao.AstronomyPictureDao
import com.adyen.android.assignment.api.model.AstronomyPicture
import com.adyen.android.assignment.data.Constants
import com.adyen.android.assignment.data.Resource
import com.adyen.android.assignment.data.SingleLiveEvent
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

    var currentSortTag = Constants.NO_SORT_TAG

    private var allApods = SingleLiveEvent<Resource<List<AstronomyPictureDao>>>()

    private val filteredApods = SingleLiveEvent<Resource<List<AstronomyPictureDao>>>()

    private val favouriteApods = SingleLiveEvent<Resource<MutableList<AstronomyPictureDao>>>()

    fun allApods() : SingleLiveEvent<Resource<List<AstronomyPictureDao>>>{

        return allApods

    }

    fun filteredApods() : SingleLiveEvent<Resource<List<AstronomyPictureDao>>>{

        return filteredApods

    }

    fun favouriteApods() : SingleLiveEvent<Resource<MutableList<AstronomyPictureDao>>>{

        return favouriteApods

    }

    fun getApods(){

        //get all favourite apods
        getFavouriteApods()

        //check filter first
        if(filterApods()) return

        //get all apods if no filter
        if(getAllApods()) return

        allApods.value = Resource.loading()

        getApodsFromService()
    }

    fun filterApods() : Boolean{

        if(currentSortTag != Constants.NO_SORT_TAG){

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

    fun getApodsFromService(){

        //make api call
        viewModelScope.launch(Dispatchers.Main) {
            try {
                val response = getApodResponse().await()

                if(response.isSuccessful){

                    val images = response.body()?.filter { data ->

                        data.mediaType == Constants.IMAGE

                    }

                    val imagesDao = mutableListOf<AstronomyPictureDao>()

                    images?.let { allImages ->

                        for (i in allImages.indices) {

                            val astronomyDaoData = AstronomyPictureDao(
                                i + 1,
                                allImages[i].serviceVersion,
                                allImages[i].title,
                                allImages[i].explanation,
                                allImages[i].date,
                                allImages[i].mediaType,
                                allImages[i].hdUrl,
                                allImages[i].url,
                                false
                            )

                            imagesDao.add(astronomyDaoData)
                        }

                    }

                    favouriteApods.value = Resource.success(imagesDao.filter { favourite -> favourite.favourite }.toMutableList())

                    allApods.value = Resource.success(imagesDao)

                    return@launch

                }

            } catch (e: NoConnectivityException) {

                allApods.value = Resource.noNetwork()

                return@launch
            }
            catch (e: IOException) {

                allApods.value = Resource.error("")

                return@launch
            }

            allApods.value = Resource.error("")

        }

    }

    suspend fun getApodResponse(): Deferred<Response<List<AstronomyPicture>>> {

        return CoroutineScope(Dispatchers.IO).async{

            return@async planetaryRepo.getPictures()

        }

    }

    fun addFilter(sortTag : Int) {

        when(sortTag){

            Constants.TITLE_SORT_TAG -> {

                val sortByTitle = allApods.value?.data

                filteredApods.value = Resource.success(sortByTitle?.sortedBy { it.title })

                currentSortTag = Constants.TITLE_SORT_TAG
            }

            Constants.DATE_SORT_TAG -> {

                val sortByDate = allApods.value?.data

                filteredApods.value = Resource.success(sortByDate?.sortedByDescending { it.date })

                currentSortTag = Constants.DATE_SORT_TAG
            }

        }

    }

    fun addFavourite(astronomyPictureId : Int) {

        val favouriteData = allApods.value?.data?.get(astronomyPictureId)

        favouriteData?.let { favourite ->

            if(favourite.favourite){

                favouriteApods.value?.data?.add(FIRST_INDEX,favourite)

            }
            else{

                favouriteApods.value?.data?.remove(favourite)

            }

            favouriteApods.value = Resource.success(favouriteApods.value?.data)

        }

    }

    fun removeFilter() {

        currentSortTag = Constants.NO_SORT_TAG

        allApods.value = Resource.success(allApods.value?.data)

    }


    companion object{

        const val FIRST_INDEX = 0

    }

}