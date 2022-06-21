package com.adyen.android.assignment.data.interceptor

import android.content.Context
import com.adyen.android.assignment.data.Constants
import com.adyen.android.assignment.util.NetworkConnectionUtil
import com.adyen.android.assignment.util.exception.NoConnectivityException
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class ConnectivityInterceptor @Inject constructor(
    private val network : NetworkConnectionUtil
) :
    Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {

        if (!network.isConnectedToInternet()) {

            throw NoConnectivityException(Constants.NO_NETWORK_MESSAGE)

        }

        val request = chain.request()

        return chain.proceed(request)
    }

}