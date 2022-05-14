package com.hellguy39.collapse.presentaton.fragments.about_app

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.hellguy39.collapse.BuildConfig
import com.hellguy39.collapse.R
import com.hellguy39.collapse.databinding.FragmentAboutAppBinding
import com.hellguy39.collapse.utils.setMaterialFadeThoughtAnimation
import com.hellguy39.collapse.utils.setOnBackFragmentNavigation

class AboutAppFragment : Fragment(R.layout.fragment_about_app) {

    companion object {
        @JvmStatic
        fun newInstance() = AboutAppFragment()
    }

    private lateinit var binding: FragmentAboutAppBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setMaterialFadeThoughtAnimation()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentAboutAppBinding.bind(view)

        binding.topAppBar.setOnBackFragmentNavigation()

        binding.tvVersion.text = BuildConfig.VERSION_NAME
    }

}