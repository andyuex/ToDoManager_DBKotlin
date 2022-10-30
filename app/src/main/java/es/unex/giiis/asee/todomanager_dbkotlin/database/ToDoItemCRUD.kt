package es.unex.giiis.asee.todomanager_dbkotlin.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.util.Log
import es.unex.giiis.asee.todomanager_dbkotlin.ToDoItem

class ToDoItemCRUD private constructor(context: Context) {
    private val mDbHelper: ToDoManagerDbHelper?

    init {
        mDbHelper = ToDoManagerDbHelper(context)
    }

    fun getAll(): List<ToDoItem> {
        val db = mDbHelper?.readableDatabase
        val projection = arrayOf(
            DBContract.TodoItem._ID,
            DBContract.TodoItem.COLUMN_NAME_TITLE,
            DBContract.TodoItem.COLUMN_NAME_STATUS,
            DBContract.TodoItem.COLUMN_NAME_PRIORITY,
            DBContract.TodoItem.COLUMN_NAME_DATE
        )
        val selection: String? = null
        val selectionArgs: Array<String>? = null
        val sortOrder: String? = null
        val cursor = db?.query(
            DBContract.TodoItem.TABLE_NAME, // The table to query
            projection,                     // The columns to return
            selection,                      // The columns for the WHERE clause
            selectionArgs,                  // The values for the WHERE clause
            null,                   // don't group the rows
            null,                    // don't filter by row groups
            sortOrder                       // The sort order
        )
        val items: ArrayList<ToDoItem> = ArrayList()
        if (cursor != null) {
            if (cursor.count > 0) {
                cursor.moveToFirst()
                do {
                    items.add(getToDoItemFromCursor(cursor))
                } while (cursor.moveToNext())
            }
            cursor.close()
        }
        return items
    }

    fun insert(item: ToDoItem): Long {
        // Gets the data repository in write mode
        val db = mDbHelper?.writableDatabase

        // Create a new map of values, where column names are the keys
        val values = ContentValues()
        values.put(DBContract.TodoItem.COLUMN_NAME_TITLE, item.getTitle())
        values.put(DBContract.TodoItem.COLUMN_NAME_PRIORITY, item.getPriority().name)
        values.put(DBContract.TodoItem.COLUMN_NAME_STATUS, item.getStatus().name)
        values.put(DBContract.TodoItem.COLUMN_NAME_DATE, ToDoItem.FORMAT.format(item.getDate()))


        // Insert the new row, returning the primary key value of the new row
        return db?.insert(DBContract.TodoItem.TABLE_NAME, null, values) ?: -1
    }

    fun deleteAll() {
        // Gets the data repository in write mode
        val db = mDbHelper?.writableDatabase

        // Define 'where' part of query.
        val selection: String? = null
        // Specify arguments in placeholder order.
        val selectionArgs: Array<String>? = null

        // Issue SQL statement.
        db?.delete(DBContract.TodoItem.TABLE_NAME, selection, selectionArgs)
    }

    fun updateStatus(ID: Long, status: ToDoItem.Status): Int {
        val db = mDbHelper?.readableDatabase
        Log.d("ToDoItemCRUD", "Item ID: $ID")

        // New value for one column
        val values = ContentValues()
        values.put(DBContract.TodoItem.COLUMN_NAME_STATUS, status.name)

        // Which row to update, based on the ID
        val selection: String = DBContract.TodoItem._ID + " = ?"
        val selectionArgs =
            arrayOf(ID.toString())
        return db?.update(
            DBContract.TodoItem.TABLE_NAME,
            values,
            selection,
            selectionArgs
        )
            ?: -1
    }

    fun close() {
        mDbHelper?.close()
    }

    companion object {
        private var mInstance: ToDoItemCRUD? = null
        fun getInstance(context: Context): ToDoItemCRUD? {
            if (mInstance == null) mInstance = ToDoItemCRUD(context)
            return mInstance
        }

        fun getToDoItemFromCursor(cursor: Cursor): ToDoItem {
            val id = cursor.getInt(cursor.getColumnIndex(DBContract.TodoItem._ID)).toLong()
            val title =
                cursor.getString(cursor.getColumnIndex(DBContract.TodoItem.COLUMN_NAME_TITLE))
            val priority =
                cursor.getString(cursor.getColumnIndex(DBContract.TodoItem.COLUMN_NAME_PRIORITY))
            val status =
                cursor.getString(cursor.getColumnIndex(DBContract.TodoItem.COLUMN_NAME_STATUS))
            val date = cursor.getString(cursor.getColumnIndex(DBContract.TodoItem.COLUMN_NAME_DATE))
            val item = ToDoItem(id, title, priority, status, date)
            Log.d("ToDoItemCRUD", item.toLog())
            return item
        }
    }
}
