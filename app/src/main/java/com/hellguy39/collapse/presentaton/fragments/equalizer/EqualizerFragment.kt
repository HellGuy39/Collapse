package com.hellguy39.collapse.presentaton.fragments.equalizer

import android.media.audiofx.AudioEffect
import android.media.audiofx.Equalizer
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hellguy39.collapse.R
import com.hellguy39.collapse.presentaton.services.PlayerService

class EqualizerFragment : Fragment(R.layout.equalizer_fragment) {

    companion object {
        fun newInstance() = EqualizerFragment()
    }

    //private val equalizer = Equalizer(0, PlayerService.getAudioSessionId())

    private lateinit var viewModel: EqualizerViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[EqualizerViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //equalizer.enabled = true
    }
}