package es.unex.giiis.asee.todomanager_dbkotlin.database

import android.provider.BaseColumns

class DBContract private constructor() {
    object TodoItem : BaseColumns {
        const val _ID = "_id"
        const val TABLE_NAME = "todo"
        const val COLUMN_NAME_TITLE = "title"
        const val COLUMN_NAME_STATUS = "status"
        const val COLUMN_NAME_PRIORITY = "priority"
        const val COLUMN_NAME_DATE = "date"
    }
}
