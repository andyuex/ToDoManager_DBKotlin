package es.unex.giiis.asee.todomanager_dbkotlin.roomdb

import androidx.room.TypeConverter
import java.util.*

class DateConverter {
    @TypeConverter
    fun toTimestamp(date: Date?): Long? {
        return date?.time
    }

    @TypeConverter
    fun toDate(timestamp: Long?): Date? {
        return timestamp?.let { Date(it) }
    }
}
