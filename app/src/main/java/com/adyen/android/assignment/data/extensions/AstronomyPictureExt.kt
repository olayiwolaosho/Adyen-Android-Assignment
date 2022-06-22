package com.adyen.android.assignment.data.extensions

import com.adyen.android.assignment.api.model.AstronomyPicture
import com.adyen.android.assignment.data.db.AstronomyPictureEnt
import com.adyen.android.assignment.data.db.FavouriteAstronomyPictureEnt

fun AstronomyPicture.toAstronomyPictureEnt() = AstronomyPictureEnt(

    serviceVersion = this.serviceVersion,

    title = this.title,

    explanation = this.explanation,

    date = this.date,

    mediaType = this.mediaType,

    hdUrl = this.hdUrl,

    url = this.url,

    favourite = false,

)

fun AstronomyPictureEnt.toFavouritePictureEnt() = FavouriteAstronomyPictureEnt(

    id = this.id,

    serviceVersion = this.serviceVersion,

    title = this.title,

    explanation = this.explanation,

    date = this.date,

    mediaType = this.mediaType,

    hdUrl = this.hdUrl,

    url = this.url,

    favourite = this.favourite,

)