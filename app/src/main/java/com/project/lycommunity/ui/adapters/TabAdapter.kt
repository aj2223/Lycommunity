package com.project.lycommunity.ui.adapters

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.project.lycommunity.ui.analytics.AnalyticsFragment
import com.project.lycommunity.ui.forum.ForumFragment
import com.project.lycommunity.ui.parent.announcement.UserAnnouncementFragment
import com.project.lycommunity.ui.parent.events.UserEventsFragment
import com.project.lycommunity.ui.test.TestForumFragment

class TabAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 3 // Number of tabs

    override fun createFragment(position: Int): Fragment {
        Log.d("TabAdapter", "Creating fragment for position: $position")
        return when (position) {
            0 -> UserAnnouncementFragment() // Tab 1: Announcements
            1 -> UserEventsFragment()        // Tab 2: Events
            2 -> ForumFragment()
            else -> throw IllegalStateException("Invalid position: $position")
        }
    }
}