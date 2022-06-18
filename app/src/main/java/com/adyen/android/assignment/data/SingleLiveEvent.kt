package com.adyen.android.assignment.data

import androidx.lifecycle.MutableLiveData
import java.util.concurrent.atomic.AtomicBoolean

class SingleLiveEvent<T> : MutableLiveData<T>() {

    private val pending = AtomicBoolean(false)

}