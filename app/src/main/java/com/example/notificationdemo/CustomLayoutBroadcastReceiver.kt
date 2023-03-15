package com.example.notificationdemo

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

class CustomLayoutBroadcastReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if(intent!!.action!! == "MY_ACTION") {
            Toast.makeText(context, "Custom Layout Notification", Toast.LENGTH_SHORT).show()
        }
    }
}