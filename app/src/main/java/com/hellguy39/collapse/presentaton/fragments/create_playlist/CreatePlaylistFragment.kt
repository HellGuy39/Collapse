package com.hellguy39.collapse.presentaton.fragments.create_playlist

import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.google.android.material.transition.platform.MaterialArcMotion
import com.google.android.material.transition.platform.MaterialContainerTransform
import com.hellguy39.collapse.R
import com.hellguy39.collapse.databinding.CreatePlaylistFragmentBinding
import com.hellguy39.collapse.presentaton.activities.main.MainActivity
import com.hellguy39.collapse.presentaton.view_models.MediaLibraryDataViewModel
import com.hellguy39.collapse.utils.Action
import com.hellguy39.collapse.utils.setMaterialFadeThoughtAnimations
import com.hellguy39.collapse.utils.setOnBackFragmentNavigation
import com.hellguy39.domain.models.Playlist
import com.hellguy39.domain.models.SelectedTracks
import com.hellguy39.domain.models.Track
import com.hellguy39.domain.usecases.ConvertBitmapToByteArrayUseCase
import com.hellguy39.domain.usecases.ConvertByteArrayToBitmapUseCase
import com.hellguy39.domain.usecases.GetColorFromThemeUseCase
import com.hellguy39.domain.utils.PlaylistType
import dagger.hilt.android.AndroidEntryPoint
import java.io.InputStream
import javax.inject.Inject


@AndroidEntryPoint
class CreatePlaylistFragment : Fragment(R.layout.create_playlist_fragment), View.OnClickListener {

    @Inject
    lateinit var convertByteArrayToBitmapUseCase: ConvertByteArrayToBitmapUseCase

    @Inject
    lateinit var convertBitmapToByteArrayUseCase: ConvertBitmapToByteArrayUseCase

    @Inject
    lateinit var getColorFromThemeUseCase: GetColorFromThemeUseCase

    companion object {
        fun newInstance() = CreatePlaylistFragment()
    }

    private lateinit var dataViewModel: MediaLibraryDataViewModel
    private lateinit var createViewModel: CreatePlaylistViewModel
    private lateinit var binding: CreatePlaylistFragmentBinding

    private lateinit var pickImage: ActivityResultLauncher<String>

    private var tracks = mutableListOf<Track>()

    private val args: CreatePlaylistFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setMaterialFadeThoughtAnimations()

        sharedElementEnterTransition = MaterialContainerTransform().apply {
            drawingViewId = R.id.fragmentContainer
            //scrimColor = Color.TRANSPARENT
            setAllContainerColors(getColorFromThemeUseCase.invoke(
                requireActivity().theme,
                com.google.android.material.R.attr.colorSurface
                )
            )
            pathMotion = MaterialArcMotion()
        }

        dataViewModel = ViewModelProvider(activity as MainActivity)[MediaLibraryDataViewModel::class.java]
        createViewModel = ViewModelProvider(this)[CreatePlaylistViewModel::class.java]

        pickImage = registerForActivityResult(ActivityResultContracts.GetContent()) {
            if (it != null) {
                val imageStream: InputStream? = context?.contentResolver?.openInputStream(it)
                val selectedImage = BitmapFactory.decodeStream(imageStream)
                binding.ivImage.setImageBitmap(selectedImage)

                val bytes = convertBitmapToByteArrayUseCase.invoke(selectedImage)

                if (bytes != null)
                    createViewModel.setPicture(bytes)
            }
        }

        tracks = args.playlist.tracks
    }

    private fun updateUI(playlist: Playlist) {
        binding.etName.setText(playlist.name)
        binding.etDesc.setText(playlist.description)

        val bytes = playlist.picture

        if (bytes != null) {
            val bitmap = convertByteArrayToBitmapUseCase.invoke(bytes)
            Glide.with(this).load(bitmap).into(binding.ivImage)
        } else
            binding.ivImage.setImageResource(R.drawable.ic_round_queue_music_24)

        updateTracksCounter(tracks.size)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = CreatePlaylistFragmentBinding.bind(view)

        binding.topAppBar.setOnBackFragmentNavigation(findNavController())

        binding.btnSelectTracks.setOnClickListener(this)
        binding.btnCreate.setOnClickListener(this)
        binding.ivImage.setOnClickListener(this)

        when (args.action) {
            Action.Create -> {
                binding.topAppBar.title = "New playlist"
            }
            Action.Update -> {
                binding.topAppBar.title = "Edit"
                if (args.playlist.picture != null)
                    createViewModel.setPicture(args.playlist.picture!!)
                updateUI(args.playlist)
            }
            else -> {}
        }

        setFragmentResultListener("pick_tracks") { requestKey, bundle ->
            tracks = bundle.get("tracks") as MutableList<Track>
            createViewModel.setSelectedTracks(tracks)
        }

        setObservers()
    }

    private fun setObservers() {
        createViewModel.getPicture().observe(viewLifecycleOwner) {
            if (it != null) {
                val bitmap = convertByteArrayToBitmapUseCase.invoke(it)
                binding.ivImage.setImageBitmap(bitmap)
            }
        }
        createViewModel.getSelectedTracks().observe(viewLifecycleOwner) {
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
                    CreatePlaylistFragmentDirections.actionCreatePlaylistFragmentToSelectTracksFragment(
                        SelectedTracks(trackList = tracks)
                    )
                )
            }
            R.id.ivImage -> {
                pickImage.launch("image/*")
            }
        }
    }

    private fun createNewPlaylist() {
        dataViewModel.addNewPlaylist(Playlist(
            name = binding.etName.text.toString(),
            description = binding.etDesc.text.toString(),
            tracks = tracks,
            picture = createViewModel.getPicture().value,
            type = PlaylistType.Custom
        ))
    }

    private fun updatePlaylist() {
        val updatedPlaylist = Playlist(
            id = args.playlist.id,
            name = binding.etName.text.toString(),
            description = binding.etDesc.text.toString(),
            tracks = tracks,
            picture = createViewModel.getPicture().value,
            type = PlaylistType.Custom
        )
        dataViewModel.updateExistingPlaylist(updatedPlaylist)
        setFragmentResult("updatedPlaylist",
            bundleOf("playlist" to updatedPlaylist)
        )
    }
}