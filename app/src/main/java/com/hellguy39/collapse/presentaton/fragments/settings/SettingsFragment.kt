package com.hellguy39.collapse.presentaton.fragments.settings

//import android.os.Bundle
//import android.view.View
//import androidx.fragment.app.Fragment
//import androidx.lifecycle.ViewModelProvider
//import com.hellguy39.collapse.R
//import com.hellguy39.collapse.databinding.FragmentSettingsBinding
//import com.hellguy39.collapse.utils.setMaterialFadeThoughtAnimation
//import com.hellguy39.collapse.utils.setOnBackFragmentNavigation
//import dagger.hilt.android.AndroidEntryPoint
//
//@AndroidEntryPoint
//class SettingsFragment : Fragment(R.layout.fragment_settings), View.OnClickListener {
//
//    private lateinit var binding: FragmentSettingsBinding
//
//    private lateinit var viewModel: SettingsFragmentViewModel
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//        viewModel = ViewModelProvider(this)[SettingsFragmentViewModel::class.java]
//
//        setMaterialFadeThoughtAnimation()
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        binding = FragmentSettingsBinding.bind(view)
//
//        binding.topAppBar.setOnBackFragmentNavigation()
//
//        binding.switchAnimations.setOnClickListener(this)
//        binding.switchSaveState.setOnClickListener(this)
//        binding.switchAdaptableBackgroundColor.setOnClickListener(this)
//
//        setObserver()
//    }
//
//    private fun setObserver() {
//        viewModel.getSettings().observe(viewLifecycleOwner) {
//            binding.switchSaveState.isChecked = it.isSaveStateEnabled
//            binding.switchAnimations.isChecked = it.isAnimationsEnabled
//            binding.switchAdaptableBackgroundColor.isChecked = it.isAdaptableBackgroundEnabled
//        }
//    }
//
//    override fun onClick(p0: View?) {
//        when(p0?.id) {
//            binding.switchSaveState.id -> {
//                viewModel.saveIsSaveStateEnabled(binding.switchSaveState.isChecked)
//            }
//            binding.switchAnimations.id -> {
//                viewModel.saveIsAnimationsEnabled(binding.switchAnimations.isChecked)
//            }
//            binding.switchAdaptableBackgroundColor.id -> {
//                viewModel.saveIsAdaptableBackground(binding.switchAdaptableBackgroundColor.isChecked)
//            }
//        }
//    }
//}