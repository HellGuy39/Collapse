package com.hellguy39.collapse.presentaton.fragments.home

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.icu.util.Calendar
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import com.google.android.material.transition.platform.MaterialFadeThrough
import com.hellguy39.collapse.R
import com.hellguy39.collapse.databinding.EqualizerHomeFragmentCardviewBinding
import com.hellguy39.collapse.databinding.FragmentHomeBinding
import com.hellguy39.collapse.utils.AudioEffectController
import com.hellguy39.collapse.utils.setMaterialFadeThoughtAnimations
import com.hellguy39.collapse.utils.setupIcons
import com.hellguy39.domain.usecases.GetColorFromThemeUseCase
import com.hellguy39.domain.usecases.eq_settings.EqualizerSettingsUseCases
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home),
    View.OnClickListener {

    @Inject
    lateinit var getColorFromThemeUseCase: GetColorFromThemeUseCase

    @Inject
    lateinit var equalizerSettingsUseCases: EqualizerSettingsUseCases

    @Inject
    lateinit var effectController: AudioEffectController

    private lateinit var binding: FragmentHomeBinding
    private lateinit var eqCardBinding: EqualizerHomeFragmentCardviewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setMaterialFadeThoughtAnimations()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHomeBinding.bind(view)
        binding.topAppBar.title = getTittle()

        setupEqCard()

        binding.cardStat.setOnClickListener(this)

        setupToolbarMenu()

        checkIsEffectsAvailable()
        setEqObserver()
    }

    private fun setupEqCard() {
        eqCardBinding = EqualizerHomeFragmentCardviewBinding.bind(binding.cardEq.root)
        eqCardBinding.root.setOnClickListener(this)
        eqCardBinding.eqSwitch.setOnClickListener(this)
        eqCardBinding.bassSwitch.setOnClickListener(this)
        eqCardBinding.virtualizerSwitch.setOnClickListener(this)
    }

    @SuppressLint("RestrictedApi")
    private fun setupToolbarMenu() {
        binding.topAppBar.menu.setupIcons(resources)

        binding.topAppBar.setOnMenuItemClickListener { item ->
            when(item.itemId) {
                R.id.settings -> {
                    navigateToSettings()
                    true
                }
                R.id.aboutApp -> {
                    navigateToAboutApp()
                    true
                }
                else -> false
            }
        }
    }

    private fun setEqObserver() {
        effectController.getCurrentSettings().observe(viewLifecycleOwner) { settings ->
            eqCardBinding.eqSwitch.isChecked = settings.isEqEnabled
            eqCardBinding.bassSwitch.isChecked = settings.isBassEnabled
            eqCardBinding.virtualizerSwitch.isChecked = settings.isVirtualizerEnabled
        }
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

    override fun onClick(p0: View?) {
        when(p0?.id) {
            eqCardBinding.root.id -> navigateToEqualizer()

            binding.cardStat.id -> navigateToStatistic()

            eqCardBinding.eqSwitch.id -> {
                val p1 = eqCardBinding.eqSwitch.isChecked
                effectController.setEqEnabled(p1)
                setupIconColor(eqCardBinding.eqSwitch.id, p1)
            }
            eqCardBinding.bassSwitch.id -> {
                val p1 = eqCardBinding.bassSwitch.isChecked
                effectController.setBassEnabled(p1)
                setupIconColor(eqCardBinding.bassSwitch.id, p1)
            }
            eqCardBinding.virtualizerSwitch.id -> {
                val p1 = eqCardBinding.virtualizerSwitch.isChecked
                effectController.setVirtualizeEnabled(p1)
                setupIconColor(eqCardBinding.virtualizerSwitch.id, p1)
            }
        }
    }

    private fun setupIconColor(id: Int, enabled: Boolean) {
        val primaryColorStateList = ColorStateList.valueOf(
            getColorFromThemeUseCase.invoke(
                requireActivity().theme,
                com.google.android.material.R.attr.colorPrimary
            )
        )
        val onSurfaceColorStateList = ColorStateList.valueOf(
            getColorFromThemeUseCase.invoke(
                requireActivity().theme,
                com.google.android.material.R.attr.colorOnSurface
            )
        )
        val enableColorStateList = if (enabled) primaryColorStateList else onSurfaceColorStateList
        when(id) {
            eqCardBinding.eqSwitch.id -> {
                eqCardBinding.ivEq.imageTintList = enableColorStateList
            }
            eqCardBinding.bassSwitch.id -> {
                eqCardBinding.ivBass.imageTintList = enableColorStateList
            }
            eqCardBinding.virtualizerSwitch.id -> {
                eqCardBinding.ivSurround.imageTintList = enableColorStateList
            }
        }
    }

    private fun checkIsEffectsAvailable() {

        val properties = effectController.getProperties()

        if (!properties.bassBoostSupport) {
            eqCardBinding.bassSwitch.isEnabled = false
        }

        if (!properties.virtualizerSupport) {
            eqCardBinding.virtualizerSwitch.isEnabled = false
        }
    }

    private fun navigateToEqualizer() = findNavController().navigate(
        HomeFragmentDirections.actionHomeFragmentToEqualizerFragment(),
        FragmentNavigatorExtras(eqCardBinding.root to "equalizer_transition")
    )

    private fun navigateToStatistic() = findNavController().navigate(
        HomeFragmentDirections.actionHomeFragmentToStatisticFragment(),
        FragmentNavigatorExtras(binding.cardStat to "statistic_transition")
    )

    private fun navigateToAboutApp() = findNavController().navigate(
        HomeFragmentDirections.actionHomeFragmentToAboutAppFragment()
    )

    private fun navigateToSettings() = findNavController().navigate(
        HomeFragmentDirections.actionHomeFragmentToSettingsFragment()
    )
}