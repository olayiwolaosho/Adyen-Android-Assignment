package com.adyen.android.assignment

import com.adyen.android.assignment.api.model.DayAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

class TestServiceGenerator {

    companion object{

        fun <T> getService(serviceClass: Class<T>): T {

            return Retrofit.Builder()
                .baseUrl(BuildConfig.NASA_BASE_URL)
                .addConverterFactory(moshiConverterFactory())
                .client(getClient())
                .build()
                .create(serviceClass)
        }

        private fun moshiConverterFactory(): MoshiConverterFactory {
            val moshi = Moshi
                .Builder()
                .add(DayAdapter())
                .add(KotlinJsonAdapterFactory())
                .build()

            return MoshiConverterFactory.create(moshi)
        }

        private fun getClient(): OkHttpClient {
            val client = OkHttpClient
                .Builder()
                .connectTimeout(30, TimeUnit.SECONDS) // connect timeout
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)

            return client.build()
        }

    }

}