package com.adyen.android.assignment.ui.apods

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adyen.android.assignment.data.*
import com.adyen.android.assignment.data.db.AstronomyPictureEnt
import com.adyen.android.assignment.data.db.FavouriteAstronomyPictureEnt
import com.adyen.android.assignment.data.extensions.toFavouritePictureEnt
import com.adyen.android.assignment.data.repo.PlanetaryRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class ApodsViewModel @Inject constructor(
    private val planetaryRepo : PlanetaryRepo,
) : ViewModel() {

    // Backing property to avoid state updates from other classes
    private val _allApodsState = MutableStateFlow<Resource<List<AstronomyPictureEnt>>>(Resource.empty())

    // The UI collects from this StateFlow to get its state updates
    val allApodsState: StateFlow<Resource<List<AstronomyPictureEnt>>> = _allApodsState.asStateFlow()

    private var _allApods : List<AstronomyPictureEnt>? = listOf()

    private val favouriteApods = SingleLiveEvent<Resource<MutableList<FavouriteAstronomyPictureEnt>>>()

    var currentSortTag = NO_SORT_TAG

    /*fun filteredApods() : SingleLiveEvent<Resource<List<AstronomyPictureEnt>>>{

        return filteredApods

    }*/

    fun favouriteApods() : SingleLiveEvent<Resource<MutableList<FavouriteAstronomyPictureEnt>>>{

        return favouriteApods

    }

    fun isApodsAvailable() : Boolean{

        //get all favourite apods
        ///getFavouriteApods()

        //check if we have data in our liveEvents so we that instead of calling api or db
        if(filterApods() || getAllApods()){
            return true
        }

        return false

    }

    fun getApods(){

        if(isApodsAvailable()) return

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

            //_filteredApodsState.value = Resource.success(filteredApods.value?.data)

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

        if(_allApodsState.value.data != null){

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

                    _allApods = state.data

                    _allApodsState.value = Resource.success(_allApods)

                }

            }
        }

    }


    fun addFilter(sortTag : Int) {

        when(sortTag){

            TITLE_SORT_TAG -> {

                val sortByTitle = _allApodsState.value.data

                _allApodsState.value = Resource.success(sortByTitle?.sortedBy { it.title })

                currentSortTag = TITLE_SORT_TAG
            }

            DATE_SORT_TAG -> {

                val sortByDate = _allApodsState.value.data

                _allApodsState.value = Resource.success(sortByDate?.sortedByDescending { it.date })

                currentSortTag = DATE_SORT_TAG
            }

        }

    }

    fun addFavourite(astronomyPictureId : Int) {

        viewModelScope.launch(Dispatchers.Main) {

            val favouriteData = _allApodsState.value.data?.get(astronomyPictureId)

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

        _allApodsState.value = Resource.success(_allApods)

    }


    companion object{

        const val FIRST_INDEX = 0

    }

}