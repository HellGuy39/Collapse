package com.hellguy39.collapse.presentaton.fragments.media_library

import android.app.Activity
import android.content.Intent
import android.media.MediaMetadataRetriever
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.hellguy39.collapse.R
import com.hellguy39.collapse.databinding.MediaLibraryFragmentBinding
import com.hellguy39.collapse.presentaton.adapters.TrackListAdapter
import com.hellguy39.domain.models.Track
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MediaLibraryFragment : Fragment(R.layout.media_library_fragment) {

    companion object {
        fun newInstance() = MediaLibraryFragment()
    }

    private val _viewModel: MediaLibraryViewModel by viewModels()
    private lateinit var _binding: MediaLibraryFragmentBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = MediaLibraryFragmentBinding.bind(view)
        setObservers()
    }

    private fun setObservers() {
        _viewModel.getTracksList().observe(viewLifecycleOwner) {
            _binding.rvTracks.apply {
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                adapter = TrackListAdapter(trackList = it, resources = resources)
            }
        }
    }

    override fun onStart() {
        super.onStart()

        _binding.fabAdd.setOnClickListener {
            openFileBrowser()
        }
    }

    private var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data

            if (data != null) {
                val uri = data.data

                val mmr = MediaMetadataRetriever()

                try {
                    mmr.setDataSource(context, uri)

                    val cover = mmr.embeddedPicture
                    var tittle = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE)
                    var artist = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST)

                    if (tittle.isNullOrEmpty())
                        tittle = ""

                    if (artist.isNullOrEmpty())
                        artist = ""

                    val track = Track(
                        name = tittle,
                        artist = artist,
                        path = uri.toString(),
                        embeddedPicture = cover
                    )

                    _viewModel.addTrack(track)
                } catch (e: Exception) {
                    Toast.makeText(context, "Something went wrong...", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun openFileBrowser() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "*/*"
        resultLauncher.launch(intent)
    }

    private fun navigateToTrack(track: Track) = findNavController()
        .navigate(MediaLibraryFragmentDirections.actionMediaLibraryFragmentToTrackFragment(track))

}