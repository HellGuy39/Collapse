package com.hellguy39.collapse.presentaton.fragments.radio

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.exoplayer2.MediaMetadata
import com.google.android.exoplayer2.metadata.Metadata
import com.hellguy39.collapse.R
import com.hellguy39.collapse.databinding.RadioFragmentBinding
import com.hellguy39.collapse.databinding.TrackFragmentBinding
import com.hellguy39.collapse.presentaton.services.PlayerService
import com.hellguy39.collapse.presentaton.services.ServiceContentWrapper
import com.hellguy39.collapse.utils.PlayerType

class RadioFragment : Fragment(R.layout.radio_fragment) {

    companion object {
        fun newInstance() = RadioFragment()
    }

    private lateinit var viewModel: RadioViewModel
    private lateinit var _binding: RadioFragmentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[RadioViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = RadioFragmentBinding.bind(view)

        /*PlayerService.stopService(requireContext())
        PlayerService.startService(requireContext(), ServiceContentWrapper(
            type = PlayerType.Radio,
            url = "http://kastos.cdnstream.com/1345_32"
        ))*/
    }
}