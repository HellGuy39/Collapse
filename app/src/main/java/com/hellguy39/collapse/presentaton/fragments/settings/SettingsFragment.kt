package com.hellguy39.collapse.presentaton.fragments.settings

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.hellguy39.collapse.R
import com.hellguy39.collapse.databinding.FragmentSettingsBinding
import com.hellguy39.collapse.utils.setMaterialFadeThoughtAnimation
import com.hellguy39.collapse.utils.setOnBackFragmentNavigation

class SettingsFragment : Fragment(R.layout.fragment_settings) {

    private lateinit var binding: FragmentSettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setMaterialFadeThoughtAnimation()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSettingsBinding.bind(view)

        binding.topAppBar.setOnBackFragmentNavigation()
    }
}