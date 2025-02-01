package com.project.lycommunity.ui.notificationsList

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.project.lycommunity.R
import com.project.lycommunity.data.NotificationsRepository
import com.project.lycommunity.data.ProfileRepository
import com.project.lycommunity.databinding.FragmentNotificationsListBinding
import com.project.lycommunity.ui.profile.ProfileViewModel
import com.project.lycommunity.ui.profile.ProfileViewModelFactory


class NotificationsListFragment : Fragment() {

    private var _binding : FragmentNotificationsListBinding? = null
    private val binding get() = _binding!!

    private val viewModel: NotificationsListViewModel by viewModels {
        NotificationsListViewModelFactory(NotificationsRepository())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNotificationsListBinding.inflate(inflater, container, false)
        return binding.root
    }

}