package es.unex.giiis.asee.todomanager_dbkotlin.roomdb

import androidx.room.TypeConverter
import es.unex.giiis.asee.todomanager_dbkotlin.ToDoItem

class PriorityConverter {
    @TypeConverter
    fun toString(priority: ToDoItem.Priority?): String? {
        return priority?.name
    }

    @TypeConverter
    fun toPriority(priority: String?): ToDoItem.Priority? {
        return priority?.let { ToDoItem.Priority.valueOf(priority) }
    }
}
