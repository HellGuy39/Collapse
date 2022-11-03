package com.hellguy39.collapse.home

import android.icu.util.Calendar
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.hellguy39.collapse.home.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment(), View.OnClickListener {

//    @Inject
//    lateinit var effectController: AudioEffectController

    private var binding: FragmentHomeBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHomeBinding.bind(view)
        binding?.topAppBar?.title = getTittle()

//        setupEqCard()
//
//        binding.cardStat.setOnClickListener(this)
//
//        setupToolbarMenu()
//
//        checkIsEffectsAvailable()
//        setEqObserver()
    }

//    private fun setupEqCard() {
//        eqCardBinding = EqualizerHomeFragmentCardviewBinding.bind(binding.cardEq.root)
//        eqCardBinding.root.setOnClickListener(this)
//
//        eqCardBinding.eqSwitch.setOnClickListener(this)
//        eqCardBinding.bassSwitch.setOnClickListener(this)
//        eqCardBinding.virtualizerSwitch.setOnClickListener(this)
//    }

//    @SuppressLint("RestrictedApi")
//    private fun setupToolbarMenu() {
//        binding.topAppBar.menu.setupIcons(resources)
//
//        binding.topAppBar.setOnMenuItemClickListener { item ->
//            when(item.itemId) {
//                R.id.settings -> {
//                    navigateToSettings()
//                    true
//                }
//                R.id.aboutApp -> {
//                    navigateToAboutApp()
//                    true
//                }
//                else -> false
//            }
//        }
//    }
//
//    private fun setEqObserver() {
//        effectController.eqState.getIsEnabled().observe(viewLifecycleOwner) { isEnabled ->
//            eqCardBinding.eqSwitch.isChecked = isEnabled
//        }
//
//        effectController.bassBoostState.getIsEnabled().observe(viewLifecycleOwner) { isEnabled ->
//            eqCardBinding.bassSwitch.isChecked = isEnabled
//        }
//
//        effectController.virtualizerState.getIsEnabled().observe(viewLifecycleOwner) { isEnabled ->
//            eqCardBinding.virtualizerSwitch.isChecked = isEnabled
//        }
//    }

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
//            eqCardBinding.root.id -> navigateToEqualizer()
//
//            binding.cardStat.id -> navigateToStatistic()
//
//            eqCardBinding.eqSwitch.id -> {
//                effectController.setEqEnabled(eqCardBinding.eqSwitch.isChecked)
//            }
//            eqCardBinding.bassSwitch.id -> {
//                effectController.setBassEnabled(eqCardBinding.bassSwitch.isChecked)
//            }
//            eqCardBinding.virtualizerSwitch.id -> {
//                effectController.setVirtualizeEnabled(eqCardBinding.virtualizerSwitch.isChecked)
//            }
            else -> Unit
        }
    }
//
//    private fun checkIsEffectsAvailable() {
//
//        val properties = effectController.getProperties()
//
//        if (!properties.bassBoostSupport) {
//            eqCardBinding.bassSwitch.isEnabled = false
//        }
//
//        if (!properties.virtualizerSupport) {
//            eqCardBinding.virtualizerSwitch.isEnabled = false
//        }
//    }

//    private fun navigateToEqualizer() = findNavController().navigate(
//        //HomeFragmentDirections.actionHomeFragmentToEqualizerFragment(),
//        //FragmentNavigatorExtras(eqCardBinding.root to "equalizer_transition")
//    )
//
//    private fun navigateToStatistic() = findNavController().navigate(
//        //HomeFragmentDirections.actionHomeFragmentToStatisticFragment(),
//        //FragmentNavigatorExtras(binding.cardStat to "statistic_transition")
//    )
//
//    private fun navigateToAboutApp() = findNavController().navigate(
//        //HomeFragmentDirections.actionHomeFragmentToAboutAppFragment()
//    )
//
//    private fun navigateToSettings() = findNavController().navigate(
//        //HomeFragmentDirections.actionHomeFragmentToSettingsFragment()
//    )

//    override fun onCheckedChanged(p0: CompoundButton?, p1: Boolean) {
//        when(p0?.id) {
//            eqCardBinding.eqSwitch.id -> {
//                effectController.setEqEnabled(p1)
//            }
//            eqCardBinding.bassSwitch.id -> {
//                effectController.setBassEnabled(p1)
//            }
//            eqCardBinding.virtualizerSwitch.id -> {
//                effectController.setVirtualizeEnabled(p1)
//            }
//        }
//    }
}