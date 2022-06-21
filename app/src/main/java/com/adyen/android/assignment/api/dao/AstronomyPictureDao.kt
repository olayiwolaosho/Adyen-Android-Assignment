package com.adyen.android.assignment.api.dao

import com.squareup.moshi.Json
import java.time.LocalDate

data class AstronomyPictureDao (

    var id : Int,

    var serviceVersion: String,

    var title: String,

    var explanation: String,

    var date: LocalDate,

    var mediaType: String,

    var hdUrl: String?,

    var url: String,

    var favourite: Boolean,

)