package es.unex.giiis.asee.todomanager_dbkotlin

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import es.unex.giiis.asee.todomanager_dbkotlin.database.ToDoItemCRUD

class ToDoManagerActivity : AppCompatActivity() {
    private var mRecyclerView: RecyclerView? = null
    private var mLayoutManager: RecyclerView.LayoutManager? = null
    private var mAdapter: ToDoAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_to_do_manager)
        val fab = findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener { // - Attach Listener to FloatingActionButton. Implement onClick()
            val intent = Intent(this@ToDoManagerActivity, AddToDoActivity::class.java)
            startActivityForResult(intent, ADD_TODO_ITEM_REQUEST)
        }

        // - Get a reference to the RecyclerView
        mRecyclerView = findViewById(R.id.my_recycler_view)

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView?.setHasFixedSize(true)

        // use a linear layout manager
        // - Set a Linear Layout Manager to the RecyclerView
        mLayoutManager = LinearLayoutManager(this)
        mRecyclerView?.layoutManager = mLayoutManager

        // - Create a new Adapter for the RecyclerView
        // specify an adapter (see also next example)
        val view = findViewById<View>(android.R.id.content)
        mAdapter = ToDoAdapter(this, object : ToDoAdapter.OnItemClickListener {
            override fun onItemClick(item: ToDoItem?) {
                Log.i("onItemClick", "Item " + item?.mTitle + " clicked")
                Snackbar.make(
                    mRecyclerView!!,
                    "Item " + item?.mTitle + " clicked",
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        })

        // - Attach the adapter to the RecyclerView
        mRecyclerView?.adapter = mAdapter
        val crud = ToDoItemCRUD.getInstance(this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        log("Entered onActivityResult()")

        //  - Check result code and request code.
        // If user submitted a new ToDoItem
        // Create a new ToDoItem from the data Intent
        // and then add it to the adapter
        if (requestCode == ADD_TODO_ITEM_REQUEST) {
            if (resultCode == RESULT_OK) {
                val item = ToDoItem(data!!)

                //insert into DB
                val crud = ToDoItemCRUD.getInstance(this)
                val id = crud?.insert(item)

                if (id != null) {
                    //update item ID
                    item.setID(id)

                    //insert into adapter list
                    mAdapter?.add(item)
                }
            }
        }
    }

    public override fun onResume() {
        super.onResume()

        // Load saved ToDoItems, if necessary
        if (mAdapter?.itemCount == 0) loadItems()
    }

    override fun onPause() {
        super.onPause()

        // ALTERNATIVE: Save all ToDoItems
    }

    override fun onDestroy() {
        val crud = ToDoItemCRUD.getInstance(this)
        crud?.close()
        super.onDestroy()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        super.onCreateOptionsMenu(menu)
        menu.add(Menu.NONE, MENU_DELETE, Menu.NONE, "Delete all")
        menu.add(Menu.NONE, MENU_DUMP, Menu.NONE, "Dump to log")
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            MENU_DELETE -> {
                val crud = ToDoItemCRUD.getInstance(this)
                crud?.deleteAll()
                mAdapter?.clear()
                true
            }
            MENU_DUMP -> {
                dump()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun dump() {
        if (mAdapter != null) {
            for (i in 0 until mAdapter!!.itemCount) {
                val data = (mAdapter!!.getItem(i) as ToDoItem).toLog()
                log("Item " + i + ": " + data.replace(ToDoItem.ITEM_SEP, ","))
            }
        }
    }

    // Load stored ToDoItems
    private fun loadItems() {
        val crud = ToDoItemCRUD.getInstance(this)
        val items = crud?.getAll()
        if (items != null) {
            mAdapter?.load(items as MutableList<ToDoItem>)
        }
    }

    private fun log(msg: String) {
        try {
            Thread.sleep(500)
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
        Log.i(TAG, msg)
    }

    companion object {
        // Add a ToDoItem Request Code
        private const val ADD_TODO_ITEM_REQUEST = 0
        private const val TAG = "Lab-UserInterface"

        // IDs for menu items
        private const val MENU_DELETE = Menu.FIRST
        private const val MENU_DUMP = Menu.FIRST + 1
    }
}
