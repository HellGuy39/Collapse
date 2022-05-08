package com.hellguy39.domain.usecases

import android.media.MediaPlayer
import android.media.audiofx.BassBoost
import android.media.audiofx.Equalizer
import android.media.audiofx.Virtualizer
import com.hellguy39.domain.models.EqualizerPreset
import com.hellguy39.domain.models.EqualizerProperties

class GetEqualizerPropertiesUseCase {
    operator fun invoke(): EqualizerProperties {

        val presets: MutableList<EqualizerPreset> = mutableListOf()
        val bandsCenterFreq: ArrayList<Int> = ArrayList(0)

        val mediaPlayer = MediaPlayer()
        val id = mediaPlayer.audioSessionId

        val equalizer = Equalizer(0, id)
        val virtualizer = Virtualizer(0, id)
        val bassBoost = BassBoost(0, id)

        (0 until equalizer.numberOfBands)
            .map { equalizer.getCenterFreq(it.toShort()) }
            .mapTo(bandsCenterFreq) { it / 1000 }

        presets.add(EqualizerPreset("None", -1))

        (0 until equalizer.numberOfPresets).forEach { n ->
            presets.add(getPresetBandsValue(equalizer, n.toShort()))
        }

        return EqualizerProperties(
            bassBoostSupport = bassBoost.strengthSupported,
            virtualizerSupport = virtualizer.strengthSupported,
            numberOfBands = equalizer.numberOfBands,
            lowestBandLevel = equalizer.bandLevelRange[0],
            upperBandLevel = equalizer.bandLevelRange[1],
            numberOfPresets = equalizer.numberOfPresets,
            bandsCenterFreq = bandsCenterFreq,
            presets = presets
        ).also {
            equalizer.release()
            bassBoost.release()
            virtualizer.release()
            mediaPlayer.release()
        }
    }

    private fun getPresetBandsValue(equalizer: Equalizer, preset: Short): EqualizerPreset {
        equalizer.usePreset(preset)
        return EqualizerPreset(
            name = equalizer.getPresetName(preset),
            presetNumber = preset,
            band1Level = equalizer.getBandLevel(0),
            band2Level = equalizer.getBandLevel(1),
            band3Level = equalizer.getBandLevel(2),
            band4Level = equalizer.getBandLevel(3),
            band5Level = equalizer.getBandLevel(4)
        )
    }
}