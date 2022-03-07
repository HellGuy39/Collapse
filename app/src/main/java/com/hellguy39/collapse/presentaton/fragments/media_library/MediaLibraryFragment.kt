package com.hellguy39.collapse.presentaton.fragments.media_library

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.hellguy39.collapse.R
import com.hellguy39.collapse.databinding.MediaLibraryFragmentBinding

class MediaLibraryFragment : Fragment(R.layout.media_library_fragment) {

    companion object {
        fun newInstance() = MediaLibraryFragment()
    }

    private lateinit var _viewModel: MediaLibraryViewModel
    private lateinit var _binding: MediaLibraryFragmentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _viewModel = ViewModelProvider(this)[MediaLibraryViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = MediaLibraryFragmentBinding.bind(view)
    }

    override fun onStart() {
        super.onStart()

        _binding.fabAdd.setOnClickListener {
            openSomeActivityForResult()
        }
    }

    private var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            // There are no request codes
            val data: Intent? = result.data

            if (data != null) {
                val uri = data.data
                Log.d("DEBUG", "Uri: $uri")
            }

        }
    }

    private fun openSomeActivityForResult() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "*/*"
        resultLauncher.launch(intent)
    }

}