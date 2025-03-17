package com.yehorsk.platea.core.data.remote.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.yehorsk.platea.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import com.yehorsk.platea.R
import com.yehorsk.platea.cart.data.remote.CartRepositoryImpl
import com.yehorsk.platea.menu.data.dao.MenuDao
import com.yehorsk.platea.menu.data.remote.dto.MenuItemDto
import com.yehorsk.platea.menu.data.remote.dto.toMenuItemEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import org.json.JSONObject
import javax.inject.Inject

@AndroidEntryPoint
class PushNotificationService: FirebaseMessagingService() {

    @Inject
    lateinit var menuDao: MenuDao

    @Inject
    lateinit var cartRepositoryImpl: CartRepositoryImpl

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Timber.d("FCM Token $token")
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        Timber.d("Message received: ${message.data}")

        val title = message.data["title"] ?: ""
        val body = message.data["body"] ?: ""

        val dataJson = message.data["data"]
        val action = message.data["action"]

        if (dataJson != null) {
            val jsonObject = JSONObject(dataJson)
            val itemJson = jsonObject.getJSONObject("item").toString()
            val item = Json.decodeFromString<MenuItemDto>(itemJson)
            CoroutineScope(Dispatchers.IO).launch{
                Timber.d("Action $action")
                if(action.equals("update")){
                    menuDao.upsertMenuItem(item.toMenuItemEntity())
                }else if(action.equals("delete")){
                    menuDao.deleteMenuItem(item.toMenuItemEntity())
                }else if(action.equals("store")){
                    menuDao.insertMenuItem(item.toMenuItemEntity())
                }else if(action.equals("cart")){
                    menuDao.upsertMenuItem(item.toMenuItemEntity())
                    cartRepositoryImpl.getAllItems()
                }else{

                }
            }
            Timber.d("Message received: $item")
        } else {
            Timber.d("No item data found")
        }

        if(title.isNotEmpty() && body.isNotEmpty()){
            showNotification(title, body)
        }
    }

    private fun showNotification(title: String, message: String) {
        val channelId = getString(R.string.default_channel)
        val notificationId = System.currentTimeMillis().toInt()

        val intent = Intent(this, MainActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        }

        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.logo)
            .setContentTitle(title)
            .setContentText(message)
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_HIGH)

        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        val channel = NotificationChannel(
            channelId,
            getString(R.string.app_name),
            NotificationManager.IMPORTANCE_HIGH
        )
        notificationManager.createNotificationChannel(channel)

        notificationManager.notify(notificationId, notificationBuilder.build())
    }

}