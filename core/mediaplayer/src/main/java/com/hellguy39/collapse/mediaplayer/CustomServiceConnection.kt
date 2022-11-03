package com.hellguy39.collapse.mediaplayer

import android.content.ComponentName
import android.content.ServiceConnection
import android.os.IBinder
import android.support.v4.media.session.MediaControllerCompat
import android.support.v4.media.session.MediaSessionCompat
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.session.MediaController
import com.hellguy39.collapse.core.model.Song

class CustomServiceConnection(
    private val songs: List<Song>,
    private val pos: Int
): ServiceConnection {
    override fun onServiceConnected(componentName: ComponentName?, binder: IBinder?) {
        val service = (binder as PlaybackService.ServiceBinder).getPlaybackService()
        service.player?.playWhenReady = true
        service.player?.setMediaItems(songs.map { MediaItem.fromUri(it.contentUri!!) })
        service.player?.seekToDefaultPosition(pos)
        service.player?.prepare()

//        service.player?.addListener(object : Player.Listener {
//
//        })

    }

    override fun onServiceDisconnected(p0: ComponentName?) {

    }
}