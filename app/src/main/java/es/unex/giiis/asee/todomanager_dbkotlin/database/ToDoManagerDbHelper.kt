package es.unex.giiis.asee.todomanager_dbkotlin.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class ToDoManagerDbHelper(context: Context?) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_TODOS)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL(SQL_DELETE_TODOS)
        onCreate(db)
    }

    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "todos.db"
        private const val TEXT_TYPE = " TEXT"
        private const val COMMA_SEP = ","
        private const val SQL_CREATE_TODOS = "CREATE TABLE " + DBContract.TodoItem.TABLE_NAME + " (" +
                DBContract.TodoItem._ID + " INTEGER PRIMARY KEY," +
                DBContract.TodoItem.COLUMN_NAME_TITLE + TEXT_TYPE + COMMA_SEP +
                DBContract.TodoItem.COLUMN_NAME_STATUS + TEXT_TYPE + COMMA_SEP +
                DBContract.TodoItem.COLUMN_NAME_PRIORITY + TEXT_TYPE + COMMA_SEP +
                DBContract.TodoItem.COLUMN_NAME_DATE + TEXT_TYPE +
                " )"
        private const val SQL_DELETE_TODOS =
            "DROP TABLE IF EXISTS " + DBContract.TodoItem.TABLE_NAME
    }
}
