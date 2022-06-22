package com.adyen.android.assignment.util.converters

import java.time.LocalDate


/**
 * Converts [LocalDate]s to Strings and vice-versa for Room
 */
class LocalDateConverter: BaseConverter<LocalDate>() {

    override fun objectFromString(value: String): LocalDate? = try {
        LocalDate.parse(value)
    } catch (e: Exception) {
        null
    }
}