package com.adyen.android.assignment.util

import java.text.SimpleDateFormat
import java.util.*

class Globals {

    companion object{

        fun convertDate(date : String) : String {

            val format1 = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)

            val format2 = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)

            val output = format1.parse(date)

            output?.let { outDate ->

                return format2.format(outDate).toString()

            }

            return ""

        }

    }
}