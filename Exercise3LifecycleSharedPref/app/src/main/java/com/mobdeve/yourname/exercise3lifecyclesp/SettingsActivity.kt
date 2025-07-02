package com.mobdeve.yourname.exercise3lifecyclesp

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Switch
import androidx.appcompat.app.AppCompatActivity

import android.content.SharedPreferences

class SettingsActivity : AppCompatActivity() {
    // Views for the switches
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private lateinit var linearViewSwitch: Switch
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private lateinit var hideLikeSwitch: Switch
    private lateinit var sharedPrefs: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        // Instantiation of the Switch views
        this.linearViewSwitch = findViewById(R.id.viewSwitch)
        this.hideLikeSwitch = findViewById(R.id.hideLikeSwitch)

        sharedPrefs = getSharedPreferences("AppPrefs", MODE_PRIVATE)
        editor = sharedPrefs.edit()

        // Load and apply current preferences
        val layoutType = sharedPrefs.getInt("layoutType", LayoutType.LINEAR_VIEW_TYPE.ordinal)
        val hideLike = sharedPrefs.getBoolean("hideLikeButtons", false)

        linearViewSwitch.isChecked = layoutType == LayoutType.LINEAR_VIEW_TYPE.ordinal
        hideLikeSwitch.isChecked = hideLike

        // Save preferences when switches are toggled
        linearViewSwitch.setOnCheckedChangeListener { _, isChecked ->
            val newLayoutType = if (isChecked) LayoutType.LINEAR_VIEW_TYPE.ordinal else LayoutType.GRID_VIEW_TYPE.ordinal
            editor.putInt("layoutType", newLayoutType).apply()
        }

        hideLikeSwitch.setOnCheckedChangeListener { _, isChecked ->
            editor.putBoolean("hideLikeButtons", isChecked).apply()
        }
    }
}