package es.unex.giiis.asee.todomanager_dbkotlin.roomdb

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import es.unex.giiis.asee.todomanager_dbkotlin.ToDoItem

@Dao
interface ToDoItemDao {
    @Query("SELECT * FROM todo")
    fun getAll(): List<ToDoItem?>?

    @Insert
    fun insert(item: ToDoItem?): Long

    @Query("DELETE FROM todo")
    fun deleteAll()

    @Update
    fun update(item: ToDoItem?): Int
}
