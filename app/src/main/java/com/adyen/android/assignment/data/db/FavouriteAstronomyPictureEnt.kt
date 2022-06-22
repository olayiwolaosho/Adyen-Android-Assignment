package com.adyen.android.assignment.data.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity
data class FavouriteAstronomyPictureEnt(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,

    @ColumnInfo(name = "serviceVersion") val serviceVersion: String,

    @ColumnInfo(name = "title") val title: String,

    @ColumnInfo(name = "explanation") val explanation: String,

    @ColumnInfo(name = "date") val date: LocalDate,

    @ColumnInfo(name = "mediaType") val mediaType: String?,

    @ColumnInfo(name = "hdUrl") val hdUrl: String?,

    @ColumnInfo(name = "url") val url: String,

    @ColumnInfo(name = "favourite") var favourite: Boolean
)
