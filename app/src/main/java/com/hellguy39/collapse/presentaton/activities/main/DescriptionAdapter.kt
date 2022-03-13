package com.hellguy39.collapse.presentaton.activities.main

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ui.PlayerNotificationManager
import com.hellguy39.collapse.R

class DescriptionAdapter(
    private val context: Context,
    private val resources: Resources
    ): PlayerNotificationManager.MediaDescriptionAdapter {

    override fun getCurrentContentTitle(player: Player): CharSequence {
        return player.mediaMetadata.title ?: "Unknown" as CharSequence
    }

    override fun createCurrentContentIntent(player: Player): PendingIntent? {
        return PendingIntent.getActivity(
            context, 0, Intent(context, MainActivity::class.java),
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    override fun getCurrentContentText(player: Player): CharSequence {
        return player.mediaMetadata.artist ?: "Unknown" as CharSequence
    }

    override fun getCurrentLargeIcon(
        player: Player,
        callback: PlayerNotificationManager.BitmapCallback
    ): Bitmap? {
        val bytes = player.mediaMetadata.artworkData

        return if (bytes != null) {
            callback.onBitmap(BitmapFactory.decodeByteArray(bytes, 0, bytes.size))
            BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
        } else {
            null
        }
    }
}