package com.project.lycommunity.ui.adapters

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.project.lycommunity.ui.adminAnnouncement.AdminAnnouncementFragment
import com.project.lycommunity.ui.adminEvents.AdminEventsFragment

class AdminTabAdapter (fragment:Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> AdminAnnouncementFragment()
            1 -> AdminEventsFragment()
            else -> throw IllegalArgumentException("Invalid position")
        }
    }
}