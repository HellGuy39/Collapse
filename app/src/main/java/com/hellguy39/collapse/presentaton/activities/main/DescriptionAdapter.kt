package com.hellguy39.collapse.presentaton.activities.main

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ui.PlayerNotificationManager

class DescriptionAdapter(
    private val context: Context
    ): PlayerNotificationManager.MediaDescriptionAdapter {

    override fun getCurrentContentTitle(player: Player): CharSequence {
        return player.currentMediaItem?.mediaMetadata?.title.toString()
    }

    override fun createCurrentContentIntent(player: Player): PendingIntent? {
        return PendingIntent.getActivity(
            context, 0, Intent(context, MainActivity::class.java),
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    override fun getCurrentContentText(player: Player): CharSequence? {
        return "Text"
    }

    override fun getCurrentLargeIcon(
        player: Player,
        callback: PlayerNotificationManager.BitmapCallback
    ): Bitmap? {
        return null
    }
}