package com.hellguy39.collapse.mediaplayer

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.graphics.BitmapFactory
import android.os.Binder
import android.os.IBinder
import android.provider.Settings.Global.putString
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaSessionCompat
import androidx.appcompat.app.AppCompatActivity
import androidx.media3.common.MediaMetadata
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService
import androidx.media3.ui.PlayerNotificationManager
import com.google.android.gms.cast.CastRemoteDisplayLocalService.startService
import com.hellguy39.collapse.core.model.Song

class PlaybackService: MediaSessionService() {

    private val iBinder = ServiceBinder()

    var player: ExoPlayer? = null
    var mediaSession: MediaSession? = null
    var notificationManager: PlayerNotificationManager? = null

    inner class ServiceBinder : Binder() {
        fun getPlaybackService(): PlaybackService = this@PlaybackService
    }

    @SuppressLint("UnsafeOptInUsageError")
    override fun onCreate() {
        super.onCreate()
        player = ExoPlayer.Builder(this).build()
        mediaSession = MediaSession.Builder(this, player!!)
            .build()

        notificationManager = MediaPlayerNotificationManager
            .initPlayerNotificationManager(
                this,
                player!!,
                mediaSession!!.sessionCompatToken as MediaSessionCompat.Token
            )
    }

    @SuppressLint("UnsafeOptInUsageError")
    override fun onDestroy() {
        mediaSession?.run {
            player.release()
            release()
            mediaSession = null
        }
        notificationManager?.setPlayer(null)
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? {
        super.onBind(intent)
        return iBinder
    }

    override fun onGetSession(
        controllerInfo: MediaSession.ControllerInfo
    ): MediaSession? = mediaSession

    companion object {

        private var isRunning = false

//        private fun startService(activity: AppCompatActivity) {
//            activity.startService(
//                Intent(activity, PlaybackService::class.java)
//            )
//        }

        fun bindService(activity: AppCompatActivity, songs: List<Song>, pos: Int) {
            if(!isRunning) {
                isRunning = true

                activity.bindService(
                    Intent(activity, PlaybackService::class.java),
                    CustomServiceConnection(songs, pos),
                    Context.BIND_AUTO_CREATE
                )
            } else {
                isRunning = true

                activity.stopService(Intent(activity, PlaybackService::class.java))

                activity.bindService(
                    Intent(activity, PlaybackService::class.java),
                    CustomServiceConnection(songs, pos),
                    Context.BIND_AUTO_CREATE
                )

            }
        }

        fun bindService(activity: AppCompatActivity, serviceConnection: ServiceConnection) {
            if(!isRunning) {
                isRunning = true

                activity.bindService(
                    Intent(activity, PlaybackService::class.java),
                    serviceConnection,
                    Context.BIND_AUTO_CREATE
                )
            }
        }

        fun unbindService(activity: AppCompatActivity, serviceConnection: ServiceConnection) {
            if(isRunning) {
                activity.unbindService(
                    serviceConnection
                )
            }
        }

    }

}