package com.hellguy39.collapse.presentaton.fragments.statistic

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.hellguy39.collapse.R
import com.hellguy39.collapse.controllers.StatisticController
import com.hellguy39.collapse.databinding.FragmentStatisticBinding
import com.hellguy39.collapse.utils.*
import com.hellguy39.collapse.utils.formatAsDate
import com.hellguy39.collapse.utils.setMaterialFadeThoughtAnimation
import com.hellguy39.collapse.utils.setOnBackFragmentNavigation
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class StatisticFragment : Fragment(R.layout.fragment_statistic) {

    companion object {
        @JvmStatic fun newInstance() = StatisticFragment()
    }

    @Inject
    lateinit var statisticController: StatisticController

    private lateinit var binding: FragmentStatisticBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setSharedElementTransitionAnimation()

        setMaterialFadeThoughtAnimation()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentStatisticBinding.bind(view)

        binding.topAppBar.setOnBackFragmentNavigation()

        setTotalListeningTime()
    }

    private fun setTotalListeningTime() = statisticController.getTotalListeningTime().observe(viewLifecycleOwner) {
        updateTotalListeningTime(it)
    }

    private fun updateTotalListeningTime(time: Long) {
        binding.tvTotalListeningTime.text = "Total listening time: ${time.formatAsDate()}"
    }

}