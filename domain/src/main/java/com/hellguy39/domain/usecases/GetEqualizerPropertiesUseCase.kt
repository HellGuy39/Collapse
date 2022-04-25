package com.hellguy39.domain.usecases

import android.media.MediaPlayer
import android.media.audiofx.BassBoost
import android.media.audiofx.Equalizer
import android.media.audiofx.Virtualizer
import com.hellguy39.domain.models.EqualizerProperties

class GetEqualizerPropertiesUseCase {
    operator fun invoke(): EqualizerProperties {

        val presetNames: MutableList<String> = mutableListOf()
        val bandsCenterFreq: ArrayList<Int> = ArrayList(0)

        val mediaPlayer = MediaPlayer()
        val id = mediaPlayer.audioSessionId

        val equalizer = Equalizer(0, id)
        val virtualizer = Virtualizer(0, id)
        val bassBoost = BassBoost(0, id)

        (0 until equalizer.numberOfBands)
            .map { equalizer.getCenterFreq(it.toShort()) }
            .mapTo(bandsCenterFreq) { it / 1000 }

        (0 until equalizer.numberOfPresets).forEach { n ->
            presetNames.add(equalizer.getPresetName(n.toShort()))
        }

        presetNames.add("Custom")

        return EqualizerProperties(
            bassBoostSupport = bassBoost.strengthSupported,
            virtualizerSupport = virtualizer.strengthSupported,
            numberOfBands = equalizer.numberOfBands,
            lowestBandLevel = equalizer.bandLevelRange[0],
            upperBandLevel = equalizer.bandLevelRange[1],
            numberOfPresets = equalizer.numberOfPresets,
            bandsCenterFreq = bandsCenterFreq,
            presetNames = presetNames
        ).also {
            equalizer.release()
            bassBoost.release()
            virtualizer.release()
            mediaPlayer.release()
        }
    }
}