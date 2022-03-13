package com.hellguy39.collapse.presentaton.services

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import androidx.core.content.ContextCompat
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.ui.PlayerNotificationManager
import com.google.android.exoplayer2.upstream.DataSource
import com.hellguy39.collapse.presentaton.activities.main.DescriptionAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PlayerService : Service() {

    private lateinit var exoPlayer: ExoPlayer
    private lateinit var dataSourceFactory: DataSource.Factory
    private lateinit var playerNotificationManager: PlayerNotificationManager

    private val notificationId = 101
    private val channelId = "player_channel"
    private val channelName = "foreground_player_channel"

    companion object {
        fun startService(context: Context, contentWrapper: ServiceContentWrapper) {
            val service = Intent(context, PlayerService::class.java)
            service.putExtra("track_list", contentWrapper)
            ContextCompat.startForegroundService(context, service)
        }

        fun stopService(context: Context) {
            val service = Intent(context, PlayerService::class.java)
            context.stopService(service)
        }
    }

    override fun onCreate() {
        super.onCreate()
        exoPlayer = ExoPlayer.Builder(this).build()

        createNotificationsChannel(channelId, channelName)

        val playerNotificationManager = PlayerNotificationManager.Builder(
            this,
            notificationId,
            channelId
        ).setMediaDescriptionAdapter(DescriptionAdapter(this, resources))
            .setNotificationListener(object : PlayerNotificationManager.NotificationListener {
                override fun onNotificationPosted(
                    notificationId: Int,
                    notification: Notification,
                    ongoing: Boolean
                ) {
                    startForeground(notificationId, notification)
                }

                override fun onNotificationCancelled(
                    notificationId: Int,
                    dismissedByUser: Boolean
                ) {
                    stopSelf()
                }
            })
            .build()

        playerNotificationManager.setPlayer(exoPlayer)
        exoPlayer.playWhenReady = true
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        val trackWrapper = intent?.getSerializableExtra("track_list") as ServiceContentWrapper
        val trackList = trackWrapper.trackList

        for (n in trackList.indices) {
            exoPlayer.addMediaItem(MediaItem.fromUri(trackList[n].path))
        }

        exoPlayer.seekToDefaultPosition(trackWrapper.position)
        exoPlayer.prepare()

        return START_STICKY
    }

    private fun getTracks() {

    }

    private fun createNotificationsChannel(channelId: String, channelName: String) {
        val channel = NotificationChannel(
            channelId,
            channelName,
            NotificationManager.IMPORTANCE_LOW
        )
        channel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
        val service = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        service.createNotificationChannel(channel)
    }

    override fun onDestroy() {
        exoPlayer.release()
        super.onDestroy()
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        super.onTaskRemoved(rootIntent)
        //playerNotificationManager.setPlayer(null)
        stopSelf()
    }
}
