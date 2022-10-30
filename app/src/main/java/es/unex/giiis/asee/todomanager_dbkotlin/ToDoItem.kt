package es.unex.giiis.asee.todomanager_dbkotlin

import android.content.Intent
import java.text.SimpleDateFormat
import java.util.*

class ToDoItem {
    enum class Priority {
        LOW, MED, HIGH
    }

    enum class Status {
        NOTDONE, DONE
    }

    var mID: Long = 0
    var mTitle: String? = String()
    var mPriority = Priority.LOW
    var mStatus = Status.NOTDONE
    var mDate = Date()

    internal constructor(title: String?, priority: Priority, status: Status, date: Date) {
        mTitle = title
        mPriority = priority
        mStatus = status
        mDate = date
    }

    constructor(ID: Long, title: String?, priority: String?, status: String?, date: String?) {
        mID = ID
        mTitle = title
        mPriority = Priority.valueOf(priority ?: Priority.MED.toString())
        mStatus = Status.valueOf(status ?: Status.NOTDONE.toString())
        mDate = FORMAT.parse(date ?: Date().toString()) as Date
    }

    // Create a new ToDoItem from data packaged in an Intent
    internal constructor(intent: Intent) {
        mID = intent.getLongExtra(ID, 0) //TODO think best default value for ID
        mTitle = intent.getStringExtra(TITLE)
        mPriority = Priority.valueOf(intent.getStringExtra(PRIORITY) ?: Priority.MED.toString())
        mStatus = Status.valueOf(intent.getStringExtra(STATUS) ?: Status.NOTDONE.toString())
        mDate = FORMAT.parse(intent.getStringExtra(DATE) ?: Date().toString()) as Date
    }

    fun getID(): Long {
        return mID
    }

    fun setID(ID: Long) {
        mID = ID
    }

    fun getTitle(): String? {
        return mTitle
    }

    fun setTitle(title: String) {
        mTitle = title
    }

    fun getPriority(): Priority {
        return mPriority
    }

    fun setPriority(priority: Priority) {
        mPriority = priority
    }

    fun getStatus(): Status {
        return mStatus
    }

    fun setStatus(status: Status) {
        mStatus = status
    }

    fun getDate(): Date {
        return mDate
    }

    fun setDate(date: Date) {
        mDate = date
    }

    override fun toString(): String {
        return (mID.toString() + ITEM_SEP + mTitle + ITEM_SEP + mPriority + ITEM_SEP + mStatus + ITEM_SEP
                + FORMAT.format(mDate))
    }

    fun toLog(): String {
        return ("ID: " + mID + ITEM_SEP + "Title:" + mTitle + ITEM_SEP + "Priority:" + mPriority
                + ITEM_SEP + "Status:" + mStatus + ITEM_SEP + "Date:"
                + FORMAT.format(mDate))
    }

    companion object {
        val ITEM_SEP = System.getProperty("line.separator")
        const val ID = "ID"
        const val TITLE = "title"
        const val PRIORITY = "priority"
        const val STATUS = "status"
        const val DATE = "date"
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
