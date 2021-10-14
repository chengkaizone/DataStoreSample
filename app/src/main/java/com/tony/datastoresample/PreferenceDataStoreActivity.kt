package com.tony.datastoresample

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@Suppress("WHEN_ENUM_CAN_BE_NULL_IN_JAVA")
class PreferenceDataStoreActivity: AppCompatActivity() {

    lateinit var prefsManager: PrefsManager
    private var isDarkMode = true

    lateinit var roowView: View
    lateinit var imageButton: ImageButton

    companion object {
        const val TAG = "PrefeDataStoreActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preference_datastore)

        prefsManager = PrefsManager(applicationContext)

        roowView = findViewById(R.id.rootView)
        imageButton = findViewById(R.id.imageButton)

        imageButton.setOnClickListener {

            /*GlobalScope.launch(Dispatchers.Main) {
                when (isDarkMode) {
                    true -> prefsManager.setUiMode(UiMode.LIGHT)
                    false -> prefsManager.setUiMode(UiMode.DARK)
                }
            }*/
            lifecycleScope.launch {
                Log.e(TAG, "onclick:: $isDarkMode")
                when (isDarkMode) {
                    true -> prefsManager.setUiMode(UiMode.LIGHT)
                    false -> prefsManager.setUiMode(UiMode.DARK)
                }
            }
        }

        observeUiPreferences()
    }

    private fun observeUiPreferences() {
        prefsManager.uiModeFlow.asLiveData().observe(this) { uiMode ->

            Log.e(TAG, "observeData:: ${uiMode}")
            when (uiMode) {
                UiMode.LIGHT -> onLightMode()
                UiMode.DARK -> onDarkMode()
            }
        }

    }

    private fun onLightMode() {
        Log.e(TAG, "onLightMode")
        isDarkMode = false

        roowView.setBackgroundColor(ContextCompat.getColor(this, android.R.color.white))
        imageButton.setImageResource(R.drawable.ic_moon)
    }

    private fun onDarkMode() {
        Log.e(TAG, "onLightMode")
        isDarkMode = true

        roowView.setBackgroundColor(ContextCompat.getColor(this, android.R.color.black))
        imageButton.setImageResource(R.drawable.ic_sun)
    }

}