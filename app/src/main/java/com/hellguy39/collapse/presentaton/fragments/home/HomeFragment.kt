package com.hellguy39.collapse.presentaton.fragments.home

import android.content.res.ColorStateList
import android.icu.util.Calendar
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.CompoundButton
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import com.google.android.material.transition.platform.MaterialFadeThrough
import com.hellguy39.collapse.R
import com.hellguy39.collapse.databinding.FragmentHomeBinding
import com.hellguy39.collapse.presentaton.services.PlayerService
import com.hellguy39.collapse.utils.AudioEffectController
import com.hellguy39.domain.usecases.GetColorFromThemeUseCase
import com.hellguy39.domain.usecases.eq_settings.EqualizerSettingsUseCases
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home),
    View.OnClickListener,
    CompoundButton.OnCheckedChangeListener {

    @Inject
    lateinit var getColorFromThemeUseCase: GetColorFromThemeUseCase

    @Inject
    lateinit var equalizerSettingsUseCases: EqualizerSettingsUseCases

    @Inject
    lateinit var effectController: AudioEffectController

    private lateinit var binding: FragmentHomeBinding
    private lateinit var viewModel: HomeFragmentViewModel

    private lateinit var primaryColorStateList: ColorStateList
    private lateinit var onSurfaceColorStateList: ColorStateList

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupMaterialFadeThought()
        getColors()
        viewModel = ViewModelProvider(this)[HomeFragmentViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHomeBinding.bind(view)
        binding.topAppBar.title = getTittle()

        binding.cardEQ.setOnClickListener(this)
        
        binding.eqSwitch.setOnCheckedChangeListener(this)
        binding.bassSwitch.setOnCheckedChangeListener(this)
        binding.virtualizerSwitch.setOnCheckedChangeListener(this)
    }

    override fun onStart() {
        super.onStart()
        updateEqCardSwitches()
    }

    private fun setupMaterialFadeThought() {
        enterTransition = MaterialFadeThrough()
        reenterTransition = MaterialFadeThrough()
    }

    private fun getTittle(): String {

        val calendar = Calendar.getInstance()

        return when (calendar[Calendar.HOUR_OF_DAY]) {
            in 0..5 -> "Good night!"
            in 6..10 -> "Good morning!"
            in 11..17 -> "Good afternoon!"
            in 18..23 -> "Good evening!"
            else -> "Welcome back!"
        }
    }

    private fun getColors() {
        primaryColorStateList = ColorStateList.valueOf(
            getColorFromThemeUseCase.invoke(
                requireActivity().theme,
                com.google.android.material.R.attr.colorPrimary
            )
        )
        onSurfaceColorStateList = ColorStateList.valueOf(
            getColorFromThemeUseCase.invoke(
                requireActivity().theme,
                com.google.android.material.R.attr.colorOnSurface
            )
        )
    }

    override fun onClick(p0: View?) {
        when(p0?.id) {
            binding.cardEQ.id -> {
                findNavController().navigate(
                    HomeFragmentDirections.actionHomeFragmentToEqualizerFragment(),
                    FragmentNavigatorExtras(binding.cardEQ to "equalizer_transition")
                )
            }
            binding.cardStat.id -> {

            }
        }
    }

    override fun onCheckedChanged(p0: CompoundButton?, p1: Boolean) {
        when(p0?.id) {
            binding.eqSwitch.id -> effectController.setEqEnabled(p1)
            binding.bassSwitch.id -> effectController.setBassEnabled(p1)
            binding.virtualizerSwitch.id -> effectController.setVirtualizeEnabled(p1)
        }
        setupIconColor(p0?.id ?: 0, p1)
    }

    private fun setupIconColor(id: Int, enabled: Boolean) {
        val enableColorStateList = if (enabled) primaryColorStateList else onSurfaceColorStateList
        when(id) {
            binding.eqSwitch.id -> {
                binding.ivEq.imageTintList = enableColorStateList
            }
            binding.bassSwitch.id -> {
                binding.ivBass.imageTintList = enableColorStateList
            }
            binding.virtualizerSwitch.id -> {
                binding.ivSurround.imageTintList = enableColorStateList
            }
        }
    }

    private fun updateEqCardSwitches() {
        val settings = effectController.getCurrentEqualizerSettings()
        val properties = effectController.getProperties()

        binding.eqSwitch.isChecked = settings.isEqEnabled

        if (properties.bassBoostSupport) {
            binding.bassSwitch.isChecked = settings.isBassEnabled
        } else {
            binding.bassSwitch.isEnabled = false
        }

        if (properties.virtualizerSupport) {
            binding.virtualizerSwitch.isChecked = settings.isVirtualizerEnabled
        } else {
            binding.virtualizerSwitch.isEnabled = false
        }
    }
}