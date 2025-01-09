package com.project.lycommunity.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.google.android.material.tabs.TabLayoutMediator
import com.project.lycommunity.R
import com.project.lycommunity.databinding.FragmentHomeBinding
import com.project.lycommunity.ui.adapters.TabAdapter
import com.project.lycommunity.ui.login.LoginViewModel
import com.project.lycommunity.ui.notifications.NotificationsFragment
import com.project.lycommunity.ui.profile.ProfileFragment


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var tabAdapter: TabAdapter
    private val loginViewModel: LoginViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupTabs()
        setupBottomNavigation()

    }


    private fun setupTabs(){
        val tabLayoutMediator = TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = "Announcements"
                1 -> tab.text = "Events"
            }
        }

        tabAdapter = TabAdapter(this)
        binding.viewPager.adapter = tabAdapter
        tabLayoutMediator.attach()
    }

    private fun setupBottomNavigation() {
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.homeFragment -> {
                    binding.tabLayout.visibility = View.VISIBLE
                    binding.viewPager.visibility = View.VISIBLE
                    binding.fragmentContainer.visibility = View.GONE
                    true
                }
                R.id.profileFragment -> {
                    navigateToProfileFragment()
                    true
                }
                R.id.notificationsFragment -> {
                    navigateTo(NotificationsFragment())
                    true
                }
                else -> false
            }
        }
    }

    private fun navigateTo(fragment: Fragment) {
        binding.tabLayout.visibility = View.GONE
        binding.viewPager.visibility = View.GONE
        binding.fragmentContainer.visibility = View.VISIBLE

        childFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }


    private fun navigateToProfileFragment() {
        val userEmail = loginViewModel.getUserEmail()
        if (userEmail != null) {
            val profileFragment = ProfileFragment().apply {
                arguments = Bundle().apply {
                    putString("USER_EMAIL", userEmail)
                }
            }
            navigateTo(profileFragment)
        } else {
            // Handle the case where userEmail is null (e.g., show an error or redirect to login)
            Toast.makeText(requireContext(), "User email not found. Please login again.", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}