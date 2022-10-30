package es.unex.giiis.asee.todomanager_dbkotlin

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import es.unex.giiis.asee.todomanager_dbkotlin.database.ToDoItemCRUD
import es.unex.giiis.asee.todomanager_dbkotlin.roomdb.ToDoDatabase

class ToDoAdapter     // Provide a suitable constructor (depends on the kind of dataset)
    (private var mContext: Context, private val listener: OnItemClickListener) :
    RecyclerView.Adapter<ToDoAdapter.ViewHolder>() {
    private var mItems: MutableList<ToDoItem> = ArrayList()

    interface OnItemClickListener {
        fun onItemClick(item: ToDoItem?) //Type of the element to be returned
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        // - Inflate the View for every element
        val v: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.todo_item, parent, false)
        return ViewHolder(mContext, v)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(mItems[position], listener)
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount(): Int {
        return mItems.size
    }

    fun add(item: ToDoItem) {
        mItems.add(item)
        notifyDataSetChanged()
    }

    fun clear() {
        mItems.clear()
        notifyDataSetChanged()
    }

    fun load(items: MutableList<ToDoItem>) {
        mItems.clear()
        mItems = items
        notifyDataSetChanged()
    }

    fun getItem(pos: Int): Any {
        return mItems[pos]
    }

    class ViewHolder(private val mContext: Context, itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        private val title: TextView
        private val statusView: CheckBox
        private val priorityView: TextView
        private val dateView: TextView

        init {

            // - Get the references to every widget of the Item View
            title = itemView.findViewById(R.id.titleView)
            statusView = itemView.findViewById(R.id.statusCheckBox)
            priorityView = itemView.findViewById(R.id.priorityView)
            dateView = itemView.findViewById(R.id.dateView)
        }

        fun bind(toDoItem: ToDoItem, listener: OnItemClickListener) {

            // - Display Title in TextView
            title.text = toDoItem.title

            // - Display Priority in a TextView
            priorityView.text = toDoItem.priority.toString()

            //  - Display Time and Date.
            // Hint - use ToDoItem.FORMAT.format(toDoItem.getDate()) to get date and time String
            dateView.text = ToDoItem.FORMAT.format(toDoItem.date)

            //  - Set up Status CheckBox
            statusView.isChecked = toDoItem.status === ToDoItem.Status.DONE
            if (toDoItem.status === ToDoItem.Status.DONE) title.setBackgroundColor(Color.GREEN)
            statusView.setOnCheckedChangeListener { _, isChecked -> //  Set up and implement an OnCheckedChangeListener
                // is called when the user toggles the status checkbox
                if (isChecked) {
                    toDoItem.status = ToDoItem.Status.DONE
                    title.setBackgroundColor(Color.GREEN)
                } else {
                    toDoItem.status = ToDoItem.Status.NOTDONE
                    title.setBackgroundColor(Color.WHITE)
                }
//                val crud = ToDoItemCRUD.getInstance(mContext)
//                crud?.updateStatus(toDoItem.getID(), toDoItem.getStatus())
                AppExecutors.instance?.diskIO()?.execute {
                    ToDoDatabase.getInstance(mContext)?.dao?.update(toDoItem)
                }
            }
            itemView.setOnClickListener { listener.onItemClick(toDoItem) }
        }
    }
}
