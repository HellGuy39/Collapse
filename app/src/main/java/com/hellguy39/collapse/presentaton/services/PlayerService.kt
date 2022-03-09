package com.hellguy39.collapse.presentaton.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import android.os.IBinder
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationCompat.VISIBILITY_SECRET
import androidx.core.content.ContextCompat
import com.hellguy39.collapse.R
import com.hellguy39.domain.models.Track
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

private const val SERVICE_CHANNEL_ID = "player_channel_1"

class PlayerService : Service(), MediaPlayer.OnPreparedListener {

    companion object {
        private var isRunning = false
        private var mediaPlayer = MediaPlayer()

        fun startService(context: Context, contentWrapper: ServiceContentWrapper) {
            val service = Intent(context, PlayerService::class.java)
            service.putExtra("track_list", contentWrapper)
            ContextCompat.startForegroundService(context, service)
        }

        fun stopService(context: Context) {
            val service = Intent(context, PlayerService::class.java)
            context.stopService(service)
        }

        fun isRunning(): Boolean = isRunning
    }

    override fun onCreate() {
        super.onCreate()
        createNotificationsChannel()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        isRunning = true
        //val bitmap = BitmapFactory.decodeByteArray(cover, 0, cover.size)

        val trackWrapper = intent?.getSerializableExtra("track_list") as ServiceContentWrapper

        val track = trackWrapper.trackList[0]

        val remoteViews = RemoteViews(packageName, R.layout.notification_player)
        remoteViews.setTextViewText(R.id.tvTittle, "")
        remoteViews.setTextViewText(R.id.tvArtist, "")
        //remoteViews.setImageViewBitmap(R.id.ivCover,)
        //remoteViews.setOnClickFillInIntent(R.id.pl)

        CoroutineScope(Dispatchers.IO).launch {
            val notification = NotificationCompat.Builder(applicationContext, SERVICE_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_round_audiotrack_24)
                .setContent(remoteViews)
                .setSilent(true)
                .setAutoCancel(true)
                .setShowWhen(false)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .build()

            mediaPlayer = MediaPlayer.create(applicationContext, Uri.parse(track.path))
            startForeground(1, notification)
            playerPrepare()
        }

        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        isRunning = false
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    private fun createNotificationsChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val ntfFrgServ = NotificationChannel(
                SERVICE_CHANNEL_ID,
                "Service",
                NotificationManager.IMPORTANCE_MIN
            )

            ntfFrgServ.description = "Foreground service channel"
            ntfFrgServ.lockscreenVisibility = VISIBILITY_SECRET

            val manager: NotificationManager = getSystemService(NotificationManager::class.java)
        }
    }

    override fun onPrepared(p0: MediaPlayer?) {
        mediaPlayer.start()
    }

    private fun playerPrepare() {
        mediaPlayer.prepare()
    }

    private fun playerPause() {
        mediaPlayer.pause()
    }

    private fun playerStop() {
        mediaPlayer.stop()
    }
}