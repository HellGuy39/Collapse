package com.hellguy39.collapse.presentaton.fragments.track

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hellguy39.collapse.R
import com.hellguy39.collapse.databinding.TrackFragmentBinding

class TrackFragment : Fragment(R.layout.track_fragment) {

    companion object {
        fun newInstance() = TrackFragment()
    }

    private lateinit var _viewModel: TrackViewModel
    private lateinit var _binding: TrackFragmentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _viewModel = ViewModelProvider(this)[TrackViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = TrackFragmentBinding.bind(view)

    }


}