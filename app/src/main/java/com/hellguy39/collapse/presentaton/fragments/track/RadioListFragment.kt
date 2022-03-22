package com.hellguy39.collapse.presentaton.fragments.track

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.tabs.TabLayoutMediator
import com.hellguy39.collapse.R
import com.hellguy39.collapse.databinding.RadioFragmentBinding
import com.hellguy39.collapse.presentaton.adapters.RadioStationsPageAdapter

class RadioListFragment : Fragment(R.layout.fragment_radio_list) {

   // private lateinit var binding: Binding

    companion object {
        fun newInstance() = RadioListFragment()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
}