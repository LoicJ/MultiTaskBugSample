package com.pepper.multitaskbugsample

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.TaskStackBuilder
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        button.setOnClickListener {

            val notification = createBufferActivityNotification()
            with(NotificationManagerCompat.from(this)) {
                // notificationId is a unique int for each notification that you must define
                notify(23110, notification)
            }

        }
    }

    private fun createFaultyIntents(): Array<Intent> {
        val secondActivityIntent = Intent(this, SecondActivity::class.java)
        secondActivityIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP

        val intents: Array<Intent> =
            arrayOf(
                Intent(applicationContext, MainActivity::class.java),
                secondActivityIntent
            )
        intents[0].flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        return intents
    }


    private fun createBufferActivityNotification(): Notification {
        createNotificationChannel()
       val bufferActivityIntent = Intent(this,BufferActivity::class.java).apply {
           addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
           putParcelableArrayListExtra(
               DEST_EXTRA,
               ArrayList<Intent>().apply { addAll(createFaultyIntents()) }
           )
       }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, bufferActivityIntent, 0)

        return NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("click me")
                .setContentText("...")
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .build()!!
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "intent notification channel"
            val descriptionText = "..."
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    companion object {
        private const val CHANNEL_ID = "I am a channel id"
        const val DEST_EXTRA = "destination_extra"
    }
}
