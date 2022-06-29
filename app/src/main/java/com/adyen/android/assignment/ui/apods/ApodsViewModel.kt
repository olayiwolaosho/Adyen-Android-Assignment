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
import com.adyen.android.assignment.ui.UiState
import com.adyen.android.assignment.util.exception.NoConnectivityException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class ApodsViewModel @Inject constructor(
    private val planetaryRepo : PlanetaryRepo,
) : ViewModel() {

    // Backing property to avoid state updates from other classes
    private val _allApodsState = MutableStateFlow<Resource<List<AstronomyPictureEnt>>>(Resource.empty())

    // The UI collects from this StateFlow to get its state updates
    val allApodsState: StateFlow<Resource<List<AstronomyPictureEnt>>> = _allApodsState.asStateFlow()

    var currentSortTag = NO_SORT_TAG

    private var allApods = SingleLiveEvent<Resource<List<AstronomyPictureEnt>>>()

    private val filteredApods = SingleLiveEvent<Resource<List<AstronomyPictureEnt>>>()

    private val favouriteApods = SingleLiveEvent<Resource<MutableList<FavouriteAstronomyPictureEnt>>>()


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

        viewModelScope.launch {

            planetaryRepo.getApodsUiState().collect { state ->

                when (state.status) {

                    Resource.Status.LOADING -> {

                        _allApodsState.value = Resource.loading()

                    }

                    Resource.Status.SUCCESS -> {

                        _allApodsState.value = Resource.success(state.data)

                    }

                }
            }
        }

        //favouriteApods.value = Resource.success(favouriteApodsFromDb)

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
        planetaryRepo.getPictures().collect { state ->

            when (state.status) {

                Resource.Status.LOADING -> {

                    _allApodsState.value = Resource.loading()

                }

                Resource.Status.NO_NETWORK -> {

                    _allApodsState.value = Resource.noNetwork()

                }

                Resource.Status.ERROR -> {

                    _allApodsState.value = Resource.error("")

                }

                Resource.Status.SUCCESS -> {

                    //favouriteApods.value = Resource.success(result)

                    //allApods.value = Resource.success(imagesFromDb)

                    _allApodsState.value = Resource.success(state.data)

                }

            }
        }

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


                planetaryRepo.editFavourite(favourite).collect{ response ->

                    when(response.status){

                        Resource.Status.SUCCESS -> {

                            if(response.data == ADD_FAVOURITE){

                                favouriteApods.value?.data?.add(FIRST_INDEX,favourite)

                            }
                            else{

                                favouriteApods.value?.data?.remove(favourite)

                            }

                            favouriteApods.value = Resource.success(favouriteApods.value?.data)

                        }
                        else -> {

                            favouriteApods.value = Resource.success(favouriteApods.value?.data)

                        }

                    }

                }

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