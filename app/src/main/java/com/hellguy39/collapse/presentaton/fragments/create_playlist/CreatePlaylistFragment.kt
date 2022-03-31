package com.hellguy39.collapse.presentaton.fragments.create_playlist

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.hellguy39.collapse.R
import com.hellguy39.collapse.databinding.CreatePlaylistFragmentBinding
import com.hellguy39.collapse.presentaton.activities.main.MainActivity
import com.hellguy39.collapse.presentaton.view_models.MediaLibraryDataViewModel
import com.hellguy39.collapse.utils.Action
import com.hellguy39.domain.models.Playlist
import com.hellguy39.domain.models.Track
import com.hellguy39.domain.usecases.ConvertBitmapToByteArrayUseCase
import com.hellguy39.domain.usecases.ConvertByteArrayToBitmapUseCase
import com.hellguy39.domain.utils.PlaylistType
import dagger.hilt.android.AndroidEntryPoint
import java.io.ByteArrayOutputStream
import java.io.FileNotFoundException
import java.io.InputStream
import javax.inject.Inject


@AndroidEntryPoint
class CreatePlaylistFragment : Fragment(R.layout.create_playlist_fragment), View.OnClickListener {

    @Inject
    lateinit var convertByteArrayToBitmapUseCase: ConvertByteArrayToBitmapUseCase

    @Inject
    lateinit var convertBitmapToByteArrayUseCase: ConvertBitmapToByteArrayUseCase

    companion object {
        fun newInstance() = CreatePlaylistFragment()
    }

    private lateinit var dataViewModel: MediaLibraryDataViewModel
    private lateinit var binding: CreatePlaylistFragmentBinding

    private lateinit var pickImage: ActivityResultLauncher<String>

    private var selectedImage: Bitmap? = null

    private var tracks = listOf<Track>()

    private val args: CreatePlaylistFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataViewModel = ViewModelProvider(activity as MainActivity)[MediaLibraryDataViewModel::class.java]

        pickImage = registerForActivityResult(ActivityResultContracts.GetContent()) {
            val imageStream: InputStream? = context?.contentResolver?.openInputStream(it)
            selectedImage = BitmapFactory.decodeStream(imageStream)
            binding.ivImage.setImageBitmap(selectedImage)
        }

        when (args.action) {
            Action.Create -> {

            }
            Action.Update -> {
                updateUI(args.playlist)
            }
        }
    }

    private fun updateUI(playlist: Playlist) {
        binding.etName.setText(playlist.name)
        binding.etDesc.setText(playlist.description)

        val bytes = playlist.picture

        if (bytes != null) {
            val bitmap = convertByteArrayToBitmapUseCase.invoke(bytes)
            selectedImage = bitmap
            binding.ivImage.setImageBitmap(bitmap)
        } else
            binding.ivImage.setImageResource(R.drawable.ic_round_audiotrack_24)

        updateTracksCounter(playlist.tracks.size)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = CreatePlaylistFragmentBinding.bind(view)

        binding.topAppBar.setNavigationOnClickListener { findNavController().popBackStack() }
        binding.fabAdd.setOnClickListener(this)
        binding.cardSelectTracks.setOnClickListener(this)
        binding.ivImage.setOnClickListener(this)

        setFragmentResultListener("pick_tracks") { requestKey, bundle ->
            tracks = bundle.get("tracks") as List<Track>
            updateTracksCounter(tracks.size)
        }
    }

    private fun updateTracksCounter(n: Int) { binding.tvSelectedTracks.text = "Selected tracks: $n"}

    override fun onClick(p0: View?) {
        when(p0?.id) {
            R.id.fabAdd -> {
                dataViewModel.addNewPlaylist(Playlist(
                    name = binding.etName.text.toString(),
                    description = binding.etDesc.text.toString(),
                    tracks = tracks,
                    picture = convertBitmapToByteArrayUseCase.invoke(selectedImage),
                    type = PlaylistType.Custom
                ))
                findNavController().popBackStack()
            }
            R.id.cardSelectTracks -> {
                findNavController().navigate(
                    CreatePlaylistFragmentDirections.actionCreatePlaylistFragmentToSelectTracksFragment(
                        args.playlist
                    )
                )
            }
            R.id.ivImage -> {
                pickImage.launch("image/*")
            }
        }
    }
}