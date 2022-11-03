package com.hellguy39.collapse.mediaplayer

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.support.v4.media.session.MediaSessionCompat
import androidx.core.app.NotificationCompat
import androidx.media3.common.util.NotificationUtil
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerNotificationManager

object MediaPlayerNotificationManager {

    private const val PLAYBACK_NOTIFICATION_CHANNEL_ID = "collapse_playback_channel"
    private const val PLAYBACK_NOTIFICATION_CHANNEL_NAME = "Collapse playback notification channel"

    fun createMediaPlayerNotificationChannel(context: Context) {
        val channel = NotificationChannel(
            PLAYBACK_NOTIFICATION_CHANNEL_ID,
            PLAYBACK_NOTIFICATION_CHANNEL_NAME,
            NotificationManager.IMPORTANCE_DEFAULT
        )
        channel.description = ""

        val manager = context.getSystemService(NotificationManager::class.java)
        manager.createNotificationChannel(channel)
    }

    @SuppressLint("UnsafeOptInUsageError")
    fun initPlayerNotificationManager(
        service: PlaybackService,
        player: ExoPlayer,
        mediaSessionToken: MediaSessionCompat.Token
    ): PlayerNotificationManager {
        return PlayerNotificationManager.Builder(service, 1, PLAYBACK_NOTIFICATION_CHANNEL_ID)
            .setChannelImportance(NotificationUtil.IMPORTANCE_HIGH)
//            .setSmallIconResourceId(R.drawable.music)
//            .setChannelDescriptionResourceId(R.string.app_name)
//            .setChannelNameResourceId(R.string.app_name)
            //.setMediaDescriptionAdapter(CustomMediaDescriptionAdapter())
            //.setNotificationListener(CustomNotificationListener())
            .build().apply {
                setPlayer(player)
                setMediaSessionToken(mediaSessionToken)
                setPriority(NotificationCompat.PRIORITY_HIGH)
                setUseRewindAction(true)
                setUseFastForwardAction(false)
                setUsePlayPauseActions(true)
                setUsePreviousAction(false)
            }
    }

}