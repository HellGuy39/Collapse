package com.hellguy39.collapse.presentaton.adapters

//class DescriptionAdapter(
//    private val context: Context,
//    private val type: Enum<PlayerType>,
//    private val radioStation: RadioStation?
//    ): PlayerNotificationManager.MediaDescriptionAdapter {
//
//    override fun getCurrentContentTitle(player: Player): CharSequence {
//        return when(type) {
//            PlayerType.LocalTrack -> player.mediaMetadata.title ?: "Unknown" as CharSequence
//            PlayerType.Radio -> radioStation?.name ?: "Unknown"
//            else -> player.mediaMetadata.title ?: "Unknown" as CharSequence
//        }
//    }
//
//    override fun createCurrentContentIntent(player: Player): PendingIntent? {
//        return PendingIntent.getActivity(
//            context, 0, Intent(context, TrackActivity::class.java),
//            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
//        )
//    }
//
//    override fun getCurrentContentText(player: Player): CharSequence {
//        return when(type) {
//            PlayerType.LocalTrack -> player.mediaMetadata.artist ?: "Unknown" as CharSequence
//            PlayerType.Radio -> ""
//            else -> player.mediaMetadata.artist ?: "Unknown" as CharSequence
//        }
//    }
//
//    override fun getCurrentLargeIcon(
//        player: Player,
//        callback: PlayerNotificationManager.BitmapCallback
//    ): Bitmap? {
//        val bytes = when (type) {
//            PlayerType.LocalTrack -> player.mediaMetadata.artworkData
//            PlayerType.Radio -> radioStation?.picture
//            else -> null
//        }
//
//        return if (bytes != null) {
//            callback.onBitmap(BitmapFactory.decodeByteArray(bytes, 0, bytes.size))
//            BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
//        } else {
//            null
//        }
//    }
//}