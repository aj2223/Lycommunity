package com.project.lycommunity.ui.adapters

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.project.lycommunity.ui.adminAnnouncement.AdminAnnouncementFragment
import com.project.lycommunity.ui.adminEvents.AdminEventsFragment
import com.project.lycommunity.ui.analytics.AnalyticsFragment

class AdminTabAdapter (fragment:Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> AdminAnnouncementFragment()
            1 -> AdminEventsFragment()
            2 -> AnalyticsFragment()
            else -> throw IllegalArgumentException("Invalid position")
        }
    }
}