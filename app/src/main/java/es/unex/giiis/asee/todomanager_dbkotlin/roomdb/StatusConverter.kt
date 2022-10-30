package es.unex.giiis.asee.todomanager_dbkotlin.roomdb

import androidx.room.TypeConverter
import es.unex.giiis.asee.todomanager_dbkotlin.ToDoItem

class StatusConverter {
    @TypeConverter
    fun toString(status: ToDoItem.Status?): String? {
        return status?.name
    }

    @TypeConverter
    fun toStatus(status: String?): ToDoItem.Status? {
        return status?.let { ToDoItem.Status.valueOf(status) }
    }
}
