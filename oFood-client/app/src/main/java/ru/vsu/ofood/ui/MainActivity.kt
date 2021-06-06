package ru.vsu.ofood.ui

import android.content.Intent
import android.content.Intent.ACTION_DIAL
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.collect

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {

    private val vm: MainViewModel by lazy {
        ViewModelProvider(this).get(MainViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launchWhenStarted {
            vm.events.collect { event ->
                processEvent(event)
            }
        }
        setContent {
            AppContent {
                MainScreen(vm)
            }
        }
    }

    private fun processEvent(event: Event) {
        Log.d(TAG, "process Event")
        when (event) {
            is Event.NavigateToActivityCallNumber -> {
                startActivity(Intent().apply {
                    data = Uri.parse("tel:${event.phoneNumber}")
                    action = ACTION_DIAL
                })
            }
        }
    }

    override fun onBackPressed() {
        if (vm.back())
            super.onBackPressed()
    }
}