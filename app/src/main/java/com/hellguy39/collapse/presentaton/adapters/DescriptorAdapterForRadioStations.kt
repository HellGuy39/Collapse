package com.hellguy39.collapse.presentaton.adapters

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ui.PlayerNotificationManager
import com.hellguy39.collapse.presentaton.activities.track.TrackActivity
import com.hellguy39.domain.models.RadioStation

class DescriptorAdapterForRadioStations (
    private val context: Context,
    private val radioStation: RadioStation
): PlayerNotificationManager.MediaDescriptionAdapter {

    override fun getCurrentContentTitle(player: Player): CharSequence {
        return radioStation.name
    }

    override fun createCurrentContentIntent(player: Player): PendingIntent? {
        return PendingIntent.getActivity(
            context, 0, Intent(context, TrackActivity::class.java),
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    override fun getCurrentContentText(player: Player): CharSequence {
        return ""
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