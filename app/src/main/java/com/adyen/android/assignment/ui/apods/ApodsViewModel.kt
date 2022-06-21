package com.adyen.android.assignment.ui.apods

import androidx.lifecycle.MutableLiveData
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class ApodsViewModel @Inject constructor(
    private val planetaryRepo : PlanetaryRepo,
) : ViewModel() {

    var currentSortTag = Constants.NO_SORT_TAG

    private var allApods = SingleLiveEvent<Resource<List<AstronomyPictureDao>>>()

    private val filteredApods = SingleLiveEvent<Resource<List<AstronomyPictureDao>>>()

    fun allApods() : SingleLiveEvent<Resource<List<AstronomyPictureDao>>>{

        return allApods

    }

    fun filteredApods() : SingleLiveEvent<Resource<List<AstronomyPictureDao>>>{

        return filteredApods

    }

    fun getApods(){

        //check filter first
        if(currentSortTag != Constants.NO_SORT_TAG){

            filteredApods.value = Resource.success(filteredApods.value?.data)

            return

        }

        if(allApods.value?.data != null){

            allApods.value = Resource.success(allApods.value?.data)

            return

        }

        allApods.value = Resource.loading()

        //make api call
        viewModelScope.launch(Dispatchers.Main) {

            val planets = CoroutineScope(Dispatchers.IO).async{

                return@async planetaryRepo.getPictures()

            }

            try {

                val response = planets.await()

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

    fun removeFilter() {

        currentSortTag = Constants.NO_SORT_TAG

        allApods.value = Resource.success(allApods.value?.data)

    }

}