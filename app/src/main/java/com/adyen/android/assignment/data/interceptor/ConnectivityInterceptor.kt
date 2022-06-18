package com.adyen.android.assignment.data.interceptor

import android.content.Context
import com.adyen.android.assignment.data.Constants
import com.adyen.android.assignment.util.NetworkConnectionUtil
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
        //val intent = Intent(Constants.ACTION_TOKEN_EXPIRED)
        //context.sendBroadcast(intent)

        if (!network.isConnectedToInternet()) {

            return Response.Builder()
                .body(null)
                .code(Constants.NO_NETWORK_CODE)
                .message(Constants.NO_NETWORK_MESSAGE)
                .build()

            //throw NoConnectivityException()

        }

        val request = chain.request()

        return chain.proceed(request)
    }

}