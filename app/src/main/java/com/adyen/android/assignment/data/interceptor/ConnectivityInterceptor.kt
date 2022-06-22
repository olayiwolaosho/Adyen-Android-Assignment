package com.adyen.android.assignment.data.interceptor

import com.adyen.android.assignment.data.NO_NETWORK_MESSAGE
import com.adyen.android.assignment.util.NetworkConnectionUtil
import com.adyen.android.assignment.util.exception.NoConnectivityException
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

            throw NoConnectivityException(NO_NETWORK_MESSAGE)

        }

        val request = chain.request()

        return chain.proceed(request)
    }

}