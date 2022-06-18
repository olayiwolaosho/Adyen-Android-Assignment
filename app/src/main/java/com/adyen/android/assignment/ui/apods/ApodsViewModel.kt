package com.adyen.android.assignment.ui.apods

import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.adyen.android.assignment.api.model.AstronomyPicture
import com.adyen.android.assignment.data.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ApodsViewModel @Inject constructor() : ViewModel() {

    private var allApods = MutableLiveData<Resource<List<AstronomyPicture>>>()

    fun allApods() : MutableLiveData<Resource<List<AstronomyPicture>>>{

        return allApods

    }

    fun getApods(){



    }

}