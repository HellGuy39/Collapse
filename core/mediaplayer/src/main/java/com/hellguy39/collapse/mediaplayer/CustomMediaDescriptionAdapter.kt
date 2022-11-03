package com.hellguy39.collapse.mediaplayer

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.graphics.Bitmap
import androidx.media3.common.Player
import androidx.media3.ui.PlayerNotificationManager

@SuppressLint("UnsafeOptInUsageError")
class CustomMediaDescriptionAdapter(): PlayerNotificationManager.MediaDescriptionAdapter {
    override fun getCurrentContentTitle(player: Player): CharSequence {
        return ""
    }

    override fun createCurrentContentIntent(player: Player): PendingIntent? {
        return null
    }

    override fun getCurrentContentText(
        player: Player
    ): CharSequence? {
        return ""
    }

    override fun getCurrentLargeIcon(
        player: Player,
        callback: PlayerNotificationManager.BitmapCallback
    ): Bitmap? {
        return null
    }
}