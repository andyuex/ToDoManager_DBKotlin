package es.unex.giiis.asee.todomanager_dbkotlin

import android.content.Intent
import androidx.room.*
import es.unex.giiis.asee.todomanager_dbkotlin.roomdb.DateConverter
import es.unex.giiis.asee.todomanager_dbkotlin.roomdb.PriorityConverter
import es.unex.giiis.asee.todomanager_dbkotlin.roomdb.StatusConverter
import java.text.SimpleDateFormat
import java.util.*

@Entity(tableName = "todo")
class ToDoItem {
    enum class Priority {
        LOW, MED, HIGH
    }

    enum class Status {
        NOTDONE, DONE
    }

    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
    @ColumnInfo(name = "title")
    var title: String? = String()
    @TypeConverters(PriorityConverter::class)
    var priority = Priority.LOW
    @TypeConverters(StatusConverter::class)
    var status = Status.NOTDONE
    @TypeConverters(DateConverter::class)
    var date = Date()

    @Ignore
    internal constructor(title: String?, priority: Priority, status: Status, date: Date) {
        this.title = title
        this.priority = priority
        this.status = status
        this.date = date
    }

    @Ignore
    constructor(id: Long, title: String?, priority: String?, status: String?, date: String?) {
        this.id = id
        this.title = title
        this.priority = Priority.valueOf(priority ?: Priority.MED.toString())
        this.status = Status.valueOf(status ?: Status.NOTDONE.toString())
        this.date = FORMAT.parse(date ?: Date().toString()) as Date
    }

    // Create a new ToDoItem from data packaged in an Intent
    @Ignore
    internal constructor(intent: Intent) {
        id = intent.getLongExtra(ID, 0) //TODO think best default value for ID
        title = intent.getStringExtra(TITLE)
        priority = Priority.valueOf(intent.getStringExtra(PRIORITY) ?: Priority.MED.toString())
        status = Status.valueOf(intent.getStringExtra(STATUS) ?: Status.NOTDONE.toString())
        date = FORMAT.parse(intent.getStringExtra(DATE) ?: Date().toString()) as Date
    }

    constructor(id: Long, title: String?, priority: Priority?, status: Status?, date: Date?) {
        this.id = id
        this.date = date!!
        this.status = status!!
        this.title = title
        this.priority = priority!!
    }

    override fun toString(): String {
        return (id.toString() + ITEM_SEP + title + ITEM_SEP + priority + ITEM_SEP + status + ITEM_SEP
                + FORMAT.format(date))
    }

    fun toLog(): String {
        return ("ID: " + id + ITEM_SEP + "Title:" + title + ITEM_SEP + "Priority:" + priority
                + ITEM_SEP + "Status:" + status + ITEM_SEP + "Date:"
                + FORMAT.format(date))
    }

    companion object {
        @Ignore
        val ITEM_SEP: String = System.getProperty("line.separator") as String
        @Ignore
        const val ID = "ID"
        @Ignore
        const val TITLE = "title"
        @Ignore
        const val PRIORITY = "priority"
        @Ignore
        const val STATUS = "status"
        @Ignore
        const val DATE = "date"
        @Ignore
        val FORMAT = SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss", Locale.US
        )

        // Take a set of String data values and
        // package them for transport in an Intent
        fun packageIntent(
            intent: Intent, title: String?,
            priority: Priority, status: Status, date: String?
        ) {
            intent.putExtra(TITLE, title)
            intent.putExtra(PRIORITY, priority.toString())
            intent.putExtra(STATUS, status.toString())
            intent.putExtra(DATE, date)
        }
    }
}
