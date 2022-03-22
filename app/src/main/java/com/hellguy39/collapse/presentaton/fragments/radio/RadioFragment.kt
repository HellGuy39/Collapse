package com.hellguy39.collapse.presentaton.fragments.radio

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.tabs.TabLayoutMediator
import com.hellguy39.collapse.R
import com.hellguy39.collapse.databinding.RadioFragmentBinding
import com.hellguy39.collapse.presentaton.adapters.RadioStationsPageAdapter

class RadioFragment : Fragment(R.layout.radio_fragment) {

    companion object {
        fun newInstance() = RadioFragment()
    }

    private lateinit var viewModel: RadioViewModel
    private lateinit var binding: RadioFragmentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[RadioViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = RadioFragmentBinding.bind(view)

        val adapter = RadioStationsPageAdapter (
            fragment = this,
            stations = listOf()
        )

        binding.viewPager.adapter = adapter

        val tabLayoutMediator = TabLayoutMediator(
            binding.tabLayout,
            binding.viewPager
        ) { tab, position ->

        }

        tabLayoutMediator.attach()

        /*PlayerService.stopService(requireContext())
        PlayerService.startService(requireContext(), ServiceContentWrapper(
            type = PlayerType.Radio,
            url = "http://kastos.cdnstream.com/1345_32"
        ))*/
    }
}