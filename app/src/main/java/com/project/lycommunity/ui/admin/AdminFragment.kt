package com.project.lycommunity.ui.admin

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.tabs.TabLayoutMediator
import com.project.lycommunity.databinding.FragmentAdminBinding
import com.project.lycommunity.ui.adapters.AdminTabAdapter

class AdminFragment : Fragment() {

    private var _binding: FragmentAdminBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAdminBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupTabLayout()
    }

    private fun setupTabLayout() {
        val tabAdapter = AdminTabAdapter(this)
        binding.adminViewPager.adapter = tabAdapter

        TabLayoutMediator(binding.adminTabLayout, binding.adminViewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "Announcements"
                1 -> "Events"
                else -> null
            }
        }.attach()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}