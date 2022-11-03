package com.hellguy39.collapse.mediaplayer

import android.annotation.SuppressLint
import android.app.Notification
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerNotificationManager

@SuppressLint("UnsafeOptInUsageError")
class CustomNotificationListener(
    private val service: PlaybackService,
    private val player: ExoPlayer?
): PlayerNotificationManager.NotificationListener {
    override fun onNotificationCancelled(notificationId: Int, dismissedByUser: Boolean) {
        super.onNotificationCancelled(notificationId, dismissedByUser)
//        service.stopForeground(true)
//        if (player?.isPlaying == true) {
//            player.stop()
//            player.release()
//        }
    }

    override fun onNotificationPosted(
        notificationId: Int,
        notification: Notification,
        ongoing: Boolean
    ) {
        super.onNotificationPosted(notificationId, notification, ongoing)
        service.startForeground(notificationId, notification)
    }
}