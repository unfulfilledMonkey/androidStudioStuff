package com.mobdeve.yourname.exercise3lifecyclesp

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import android.content.SharedPreferences

class MainActivity : AppCompatActivity() {
    // Data for the application
    private lateinit var data: ArrayList<PostModel>

    // RecyclerView components
    private lateinit var recyclerView: RecyclerView
    private lateinit var myAdapter: MyAdapter

    private lateinit var sharedPrefs: SharedPreferences

    /*
    // Indicators for what Layout should be used or if the like buttons should be hidden
    private val recyclerViewDefaultView = LayoutType.LINEAR_VIEW_TYPE.ordinal // int of LayoutType.LINEAR_VIEW_TYPE (default) or LayoutType.GRID_VIEW_TYPE
    private val hideLikeButtons = false // true = hide buttons; false = shown buttons (default)
    */

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sharedPrefs = getSharedPreferences("AppPrefs", MODE_PRIVATE)
        val savedLayoutType = sharedPrefs.getInt("layoutType", LayoutType.LINEAR_VIEW_TYPE.ordinal)
        val hideLikeButtons = sharedPrefs.getBoolean("hideLikeButtons", false)

        // Initialize the data, recyclerView and adapter
        this.data = DataHelper.initializeData()
        this.recyclerView = findViewById(R.id.recyclerView)
        this.myAdapter = MyAdapter(this.data)

        // Set the layout manager according to the default view
        this.recyclerView.layoutManager = getLayoutManager(savedLayoutType)

        // Initialize the view type and hide like button settings
        this.myAdapter.setViewType(savedLayoutType)
        this.myAdapter.setHideLikeBtn(hideLikeButtons)

        // Sets the adapter of the recycler view
        this.recyclerView.adapter = this.myAdapter
    }

    /*
     * Just a method to return a specific LayoutManager based on the ViewType provided.
     * */
    private fun getLayoutManager(value: Int): RecyclerView.LayoutManager {
        return if (value == LayoutType.LINEAR_VIEW_TYPE.ordinal)
             LinearLayoutManager(this)
        else
             GridLayoutManager(this, 2)
    }

    /*
    * Responsible for inflating the options menu on the upper right corner of the screen.
    * */
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    /*
     * A little overkill tbh, but this method is responsible for handling the selection of items
     * in the options menu. There's only one item anyway -- Settings, which leads the user to the
     * Settings activity.
     * */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> {
                val i = Intent(this@MainActivity, SettingsActivity::class.java)
                startActivity(i)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onResume() {
        super.onResume()
        // Re-apply preferences in case they changed
        val layoutType = sharedPrefs.getInt("layoutType", LayoutType.LINEAR_VIEW_TYPE.ordinal)
        val hideLikeButtons = sharedPrefs.getBoolean("hideLikeButtons", false)
        this.recyclerView.layoutManager = getLayoutManager(layoutType)
        this.myAdapter.setViewType(layoutType)
        this.myAdapter.setHideLikeBtn(hideLikeButtons)
        this.myAdapter.notifyDataSetChanged()
    }
}