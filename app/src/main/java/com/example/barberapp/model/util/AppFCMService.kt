package com.example.barberapp.model.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.barberapp.R
import com.example.barberapp.view.dashboard.DashboardActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlin.random.Random

class AppFCMService() : FirebaseMessagingService() {

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        Log.e("onMessageReceived", "onMessageReceived")
        val alertType = message.data["alert_type"]
        if(alertType == "InterviewSchedule") {
            val title = message.data["title"]
            val msg = message.data["message"]
            val job_title = message.data["title"]
            val job_id = message.data["job_id"]
            showInterviewScheduledNotification(title, msg, job_title, job_id)

        } else {
            val m = message.notification!!.body
            val t = message.notification!!.title
            showInterviewScheduledNotification(t, m, "1", "1")
        }

        for((k,v) in message.data) {
            Log.d("MyFCMService", "onMessageReceived: $k = $v")
        }
    }

    private fun showInterviewScheduledNotification(
        title: String?,
        msg: String?,
        jobTitle: String?,
        jobId: String?
    ) {
        val id = Random.nextInt(0, Int.MAX_VALUE)

        val idIntent = Intent(baseContext, DashboardActivity::class.java).apply {
            putExtra("msg", msg)
            putExtra("job_title", jobTitle)
            putExtra("job_id", jobId)
        }

        val pendingIntent = PendingIntent.getActivity(
            baseContext,
            id,
            idIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )


        val notification = NotificationCompat.Builder(this, "JobAlerts").apply {
            setContentTitle(title?:"")
            setContentText(msg?:"")
            setSmallIcon(R.drawable.ic_baseline_mail_outline_24)
            setContentIntent(pendingIntent)
            setAutoCancel(true)
        }.build()

        val nm = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel("JobAlerts", "Job Alerts", NotificationManager.IMPORTANCE_HIGH)
            nm.createNotificationChannel(channel)
        }

        nm.notify(id, notification)

    }
}