package es.unex.giiis.asee.todomanager_dbkotlin.roomdb

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import es.unex.giiis.asee.todomanager_dbkotlin.ToDoItem


@Database(entities = [ToDoItem::class], version = 1)
abstract class ToDoDatabase : RoomDatabase() {
    abstract val dao: ToDoItemDao?

    companion object {
        private var instance: ToDoDatabase? = null
        fun getInstance(context: Context?): ToDoDatabase? {
            if (instance == null) {
                if (context != null) {
                    instance = Room.databaseBuilder(
                        context,
                        ToDoDatabase::class.java, "todo.db"
                    ).build()
                }
            }
            return instance
        }
    }
}
