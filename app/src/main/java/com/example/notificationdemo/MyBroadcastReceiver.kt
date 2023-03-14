package com.example.notificationdemo

import android.app.NotificationManager
import android.app.PendingIntent
import android.app.PendingIntent.CanceledException
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast


class MyBroadcastReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if(intent!!.action!! == "ACTION_SNOOZE"){
            Toast.makeText(context, "Demo Notification", Toast.LENGTH_SHORT).show()

            // Show Activity Foreground + Background
            val intent = Intent(context, AlertDetailsAct::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            val pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent. FLAG_IMMUTABLE)
            try {
                pendingIntent.send()
            } catch (e: CanceledException) {
                Log.e("Logger", "Failed to start activity", e)
            }


            // Dismiss dialog notification when click addAction
            val notificationID = 101
            val notificationManager = context!!.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.cancel(notificationID)
        }
    }
}