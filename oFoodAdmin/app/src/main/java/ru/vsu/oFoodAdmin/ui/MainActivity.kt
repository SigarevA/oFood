package ru.vsu.oFoodAdmin.ui

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts

import kotlinx.coroutines.channels.Channel
import ru.vsu.oFoodAdmin.ui.theme.OFoodAdminTheme

class MainActivity : ComponentActivity() {

    val uriChannel = Channel<Uri>(Channel.CONFLATED)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            OFoodAdminTheme {
                OFoodAdminApp()
            }
        }

    }

    val selectedImageLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uriChannel.offer(uri)
        }
}