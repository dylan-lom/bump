package com.dylanlom.bump

import android.app.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.SystemClock
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

private const val TAG = "Notifier"
const val NOTIFICATION_KEY = "com.dylanlom.bump.NOTIFICATION"

class Notifier : BroadcastReceiver() {
    companion object _notifier {
        fun createNotificationChannel(context: Context) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val name = context.getString(R.string.notification_channel_name)
                val descriptionText = context.getString(R.string.notification_channel_name)
                val importance = NotificationManager.IMPORTANCE_DEFAULT
                val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                    description = descriptionText
                }
                // Register the channel with the system
                val notificationManager: NotificationManager =
                        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                notificationManager.createNotificationChannel(channel)
            }
        }

        fun createNotification(context: Context, title: String, delay: Long) {
            var builder = NotificationCompat.Builder(context, CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_launcher_background)
                    .setContentTitle(title)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setCategory(NotificationCompat.CATEGORY_ALARM)

            val intent = Intent(context, Notifier::class.java)
            intent.putExtra(NOTIFICATION_KEY, builder.build())
            // TODO: I feel like CANCEL_CURRENT isn't what we should be doing...
            // TODO: Review how broadcasts work... will our callback only get triggered by this...
            // specific thing? I'm confused...
            val pendingIntent = PendingIntent.getBroadcast(context, 1, intent, PendingIntent.FLAG_CANCEL_CURRENT)

            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            // TODO: I'm not sure if we should be using exact? it sorta depends we could use...
            // with a +- 10% or something I don't know if that's good...
            alarmManager.setExact(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + (delay * 1000), pendingIntent)
        }
    }

    override fun onReceive(context: Context, intent: Intent) {
        Log.d(TAG, "Received broadcast")
        val notification = intent.getParcelableExtra<Notification>(NOTIFICATION_KEY) ?: return
        with(NotificationManagerCompat.from(context)) {
            // TODO: Set + retrieve ID from intent, so that we can have multiple (i think?)
            notify(1, notification)
        }
    }
}