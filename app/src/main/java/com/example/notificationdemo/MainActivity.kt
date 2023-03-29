package com.example.notificationdemo

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Build
import android.os.Bundle
import android.widget.RemoteViews
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        // When click notification
        val intent = Intent(this, AlertDetailsAct::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)


        // Set the expandable content title  & picture for the notification.
        val bigPictureStyle: NotificationCompat.BigPictureStyle = NotificationCompat.BigPictureStyle()
        bigPictureStyle.bigPicture(getBitmap(R.drawable.ic_launcher_background))
            .setBigContentTitle("Big Content Title")
            .setSummaryText("My long notification message that needs to be expanded goes here.");


        // Build NotificationCompat
        val channelId = "channelId"
        val builder: NotificationCompat.Builder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle("Notification Title")
            .setContentText("Notification Content")
            .setLargeIcon(getBitmap(R.drawable.ic_launcher_background))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            // Set the intent that will fire when the user taps the notification
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            // Set style
            .setStyle(bigPictureStyle)
            



        // Create an intent for the action
        val snoozeIntent = Intent(this, MyBroadcastReceiver::class.java).apply {
            action = "ACTION_SNOOZE"
        }
        val snoozePendingIntent: PendingIntent =
            PendingIntent.getBroadcast(this, 0, snoozeIntent, PendingIntent.FLAG_IMMUTABLE)

        // Add the action to the notification
        val action = NotificationCompat.Action.Builder(R.drawable.ic_launcher_background, "Action", snoozePendingIntent).build()
        builder.addAction(action)


        // Create NotificationChannel & NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.channel_name)
            val descriptionText = getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(channelId, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
            notificationManager.notify(101, builder.build())
        }


        // Event
        findViewById<TextView>(R.id.tvCustomLayoutNotification).setOnClickListener {
            handleCustomLayoutNotification()
        }

    }

    private fun handleCustomLayoutNotification() {
        // Create a RemoteViews object and set its layout.
        val remoteViews = RemoteViews(packageName, R.layout.layout_custom_notification)

        // Set the notification icon, title, and text.
        remoteViews.setImageViewResource(R.id.notification_icon, R.drawable.ic_notification)
        remoteViews.setTextViewText(R.id.notification_title, "Custom Title")
        remoteViews.setTextViewText(R.id.notification_text, "Custom Text")

        // Set the button text and click event.
        remoteViews.setTextViewText(R.id.notification_button, "Click")
        val buttonIntent = Intent(this, CustomLayoutBroadcastReceiver::class.java)
        buttonIntent.action = "MY_ACTION";
        val buttonPendingIntent = PendingIntent.getBroadcast(this, 0, buttonIntent, PendingIntent. FLAG_IMMUTABLE)
        remoteViews.setOnClickPendingIntent(R.id.notification_button, buttonPendingIntent)

        // Build NotificationCompat
        val channelId = "customLayoutChannelId"
        val builder: NotificationCompat.Builder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_notification)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setStyle(NotificationCompat.DecoratedCustomViewStyle())
            .setCustomContentView(remoteViews)
            .setAutoCancel(true)

        // Create NotificationChannel & NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.channel_name)
            val descriptionText = getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(channelId, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
            notificationManager.notify(102, builder.build())
        }
    }

    private fun getBitmap(drawableRes: Int): Bitmap? {
        val drawable = resources.getDrawable(drawableRes)
        val canvas = Canvas()
        val bitmap = Bitmap.createBitmap(
            drawable.intrinsicWidth,
            drawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )
        canvas.setBitmap(bitmap)
        drawable.setBounds(0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight)
        drawable.draw(canvas)
        return bitmap
    }

}

