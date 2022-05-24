package com.hellguy39.collapse.presentaton.fragments.create_playlist

import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.hellguy39.collapse.R
import com.hellguy39.collapse.databinding.CreatePlaylistFragmentBinding
import com.hellguy39.collapse.presentaton.activities.main.MainActivity
import com.hellguy39.collapse.presentaton.view_models.MediaLibraryDataViewModel
import com.hellguy39.collapse.presentaton.view_models.PlaylistSharedViewModel
import com.hellguy39.collapse.utils.*
import com.hellguy39.domain.models.Playlist
import com.hellguy39.domain.models.Track
import com.hellguy39.domain.utils.PlaylistType
import dagger.hilt.android.AndroidEntryPoint
import java.io.InputStream

@AndroidEntryPoint
class CreatePlaylistFragment : Fragment(R.layout.create_playlist_fragment), View.OnClickListener {

    companion object {
        fun newInstance() = CreatePlaylistFragment()
    }

    private lateinit var dataViewModel: MediaLibraryDataViewModel
    private val sharedViewModel: PlaylistSharedViewModel by activityViewModels()
    private lateinit var binding: CreatePlaylistFragmentBinding

    private lateinit var pickImage: ActivityResultLauncher<String>

    private var tracks = mutableListOf<Track>()

    private val args: CreatePlaylistFragmentArgs by navArgs()

    private var isLoaded: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedViewModel.clearData()

        setMaterialFadeThoughtAnimation()

        setSharedElementTransitionAnimation()

        dataViewModel = ViewModelProvider(activity as MainActivity)[MediaLibraryDataViewModel::class.java]

        pickImage = registerForActivityResult(ActivityResultContracts.GetContent()) {
            it?.let {
                val imageStream: InputStream? = context?.contentResolver?.openInputStream(it)
                val selectedImage = BitmapFactory.decodeStream(imageStream)
                sharedViewModel.updatePicture(selectedImage.toByteArray())
            }
        }

        tracks = args.playlist.tracks
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = CreatePlaylistFragmentBinding.bind(view)

        binding.topAppBar.setOnBackFragmentNavigation()

        binding.btnSelectTracks.setOnClickListener(this)
        binding.btnCreate.setOnClickListener(this)
        binding.ivImage.setOnClickListener(this)

        when (args.action) {
            Action.Create -> {
                binding.topAppBar.title = "New playlist"
            }
            Action.Update -> {
                if (!isLoaded) {
                    binding.topAppBar.title = "Edit"
                    sharedViewModel.updateExistingPlaylist(args.playlist)
                    binding.etDesc.setText(args.playlist.description)
                    binding.etName.setText(args.playlist.name)
                    isLoaded = true
                }
            }
            else -> {}
        }

        setObservers()
    }

    private fun setObservers() {
        sharedViewModel.getPicture().observe(viewLifecycleOwner) {
            Glide.with(this)
                .load(it)
                .placeholder(R.drawable.ic_round_queue_music_24)
                .into(binding.ivImage)
        }
        sharedViewModel.getSelectedTracks().observe(viewLifecycleOwner) {
            updateTracksCounter(it.size)
        }
    }

    private fun updateTracksCounter(n: Int) { binding.tvSelectedTracks.text = "Tracks: $n"}

    override fun onClick(p0: View?) {
        when(p0?.id) {
            R.id.btnCreate -> {
                when(args.action) {
                    Action.Update -> {
                        updatePlaylist()
                    }
                    Action.Create -> {
                        createNewPlaylist()
                    }
                    else -> {}
                }
                findNavController().popBackStack()
            }
            R.id.btnSelectTracks -> {
                findNavController().navigate(
                    CreatePlaylistFragmentDirections.actionCreatePlaylistFragmentToSelectTracksFragment()
                )
            }
            R.id.ivImage -> {
                pickImage.launch("image/*")
            }
        }
    }

    private fun createNewPlaylist() {
        val picture = sharedViewModel.getPicture().value

        dataViewModel.addNewPlaylist(Playlist(
            name = binding.etName.text.toString(),
            description = binding.etDesc.text.toString(),
            tracks = sharedViewModel.getSelectedTracks().value ?: mutableListOf(),
            picture = if (picture?.size == 0) null else picture,
            type = PlaylistType.Custom
        ))
    }

    private fun updatePlaylist() {
        val picture = sharedViewModel.getPicture().value

        dataViewModel.updateExistingPlaylist(Playlist(
            id = args.playlist.id,
            name = binding.etName.text.toString(),
            description = binding.etDesc.text.toString(),
            tracks = sharedViewModel.getSelectedTracks().value ?: mutableListOf(),
            picture = if (picture?.size == 0) null else picture,
            type = PlaylistType.Custom
        ))
    }
}