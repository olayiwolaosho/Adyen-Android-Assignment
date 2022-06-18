package com.adyen.android.assignment.ui.apods

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adyen.android.assignment.api.model.AstronomyPicture
import com.adyen.android.assignment.data.Constants
import com.adyen.android.assignment.data.Resource
import com.adyen.android.assignment.data.repo.PlanetaryRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import okhttp3.Response
import javax.inject.Inject

@HiltViewModel
class ApodsViewModel @Inject constructor(
    private val planetaryRepo : PlanetaryRepo,
) : ViewModel() {

    private var allApods = MutableLiveData<Resource<List<AstronomyPicture>>>()

    fun allApods() : MutableLiveData<Resource<List<AstronomyPicture>>>{

        return allApods

    }

    fun getApods(){

        allApods.value = Resource.loading()

        allApods.value?.data?.let { data ->

            allApods.value = Resource.success(data)

            return@let

        }

        /*if(allApods.value?.data != null){

            allApods.value = Resource.success(allApods.value?.data)

            return

        }*/

        //make api call
        viewModelScope.launch(Dispatchers.Main) {

            val planets = CoroutineScope(Dispatchers.IO).async{

                return@async planetaryRepo.getPictures()

            }

            val response = planets.await()

            if(response.code() == Constants.NO_NETWORK_CODE){

                allApods.value = Resource.noNetwork()

                return@launch
            }

            if(response.isSuccessful){

                val images = response.body()?.filter { data ->

                    data.mediaType == Constants.IMAGE

                }

                allApods.value = Resource.success(images)

                return@launch

            }

            allApods.value = Resource.error("")

        }
    }

}