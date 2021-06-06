package ru.vsu.ofood.notification

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import ru.vsu.ofood.di.ComponentHolder
import ru.vsu.ofood.domain.ClientRepo
import javax.inject.Inject

private const val TAG = "MyFMService"

class MyFirebaseMessagingService : FirebaseMessagingService() {

    @Inject lateinit var clientRepo: ClientRepo

    override fun onNewToken(p0: String) {
        Log.d(TAG, "token : ${p0}")
        super.onNewToken(p0)
        Log.d(TAG, "token : ${p0}")
        ComponentHolder.appComponent.inject(this)
        GlobalScope.launch {
            try {
                val status = clientRepo.register(p0)
                Log.d(TAG, "status : ${status.status}")
            }
            catch (ex : Exception) {
                Log.e("onNewToken", "err", ex)
            }
        }
    }
}