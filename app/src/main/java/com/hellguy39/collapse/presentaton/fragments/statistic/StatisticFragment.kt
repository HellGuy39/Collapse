package com.hellguy39.collapse.presentaton.fragments.statistic

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.android.material.transition.platform.MaterialContainerTransform
import com.hellguy39.collapse.R
import com.hellguy39.collapse.databinding.FragmentStatisticBinding
import com.hellguy39.collapse.utils.StatisticController
import com.hellguy39.collapse.utils.setOnBackFragmentNavigation
import com.hellguy39.domain.models.StatisticsModel
import com.hellguy39.domain.usecases.DateFormatUseCase
import com.hellguy39.domain.usecases.GetColorFromThemeUseCase
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class StatisticFragment : Fragment(R.layout.fragment_statistic) {

    companion object {
        @JvmStatic fun newInstance() = StatisticFragment()
    }

    @Inject
    lateinit var statisticController: StatisticController

    @Inject
    lateinit var getColorFromThemeUseCase: GetColorFromThemeUseCase

    @Inject
    lateinit var dateFormatUseCase: DateFormatUseCase

    private lateinit var binding: FragmentStatisticBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedElementEnterTransition = MaterialContainerTransform().apply {
            drawingViewId = R.id.fragmentContainer
            //scrimColor = Color.TRANSPARENT
            setAllContainerColors(getColorFromThemeUseCase.invoke(
                requireActivity().theme,
                com.google.android.material.R.attr.colorSurface
            ))
        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentStatisticBinding.bind(view)

        binding.topAppBar.setOnBackFragmentNavigation(findNavController())

        setTotalListeningTime()
    }

    private fun setTotalListeningTime() = statisticController.getTotalListeningTime().observe(viewLifecycleOwner) {
        updateTotalListeningTime(it)
    }

    private fun updateTotalListeningTime(time: Long) {
        binding.tvTotalListeningTime.text = "Total listening time: ${dateFormatUseCase.invoke(time)}"
    }

}