package com.hellguy39.collapse.presentaton.adapters

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.hellguy39.collapse.presentaton.fragments.track.RadioListFragment
import com.hellguy39.domain.models.RadioStation

class RadioStationsPageAdapter(
    fragment: Fragment,
    private val stations: List<RadioStation>
): FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = stations.size

    override fun createFragment(position: Int): Fragment = RadioListFragment.newInstance()
}