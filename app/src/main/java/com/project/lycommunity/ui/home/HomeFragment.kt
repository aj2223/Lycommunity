package com.project.lycommunity.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.tabs.TabLayoutMediator
import com.project.lycommunity.R
import com.project.lycommunity.databinding.FragmentHomeBinding
import com.project.lycommunity.ui.adapters.TabAdapter
import com.project.lycommunity.ui.login.LoginFragment
import com.project.lycommunity.ui.login.LoginViewModel
import com.project.lycommunity.ui.notifications.NotificationsFragment
import com.project.lycommunity.ui.profile.ProfileFragment
import kotlinx.coroutines.launch


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var tabAdapter: TabAdapter
    private lateinit var drawerToggle: ActionBarDrawerToggle

    private val loginViewModel: LoginViewModel by activityViewModels()
    private val homeViewModel: HomeViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupToolbar()
        setupTabs()
        setupBottomNavigation()
        observeViewModel()
        fetchUserName()

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                showLogoutConfirmationDialog()
            }
        })

    }



    private fun fetchUserName() {
        val userEmail = loginViewModel.getUserEmail()
        if (userEmail != null) {
            homeViewModel.fetchUserFullName(userEmail)
        }
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                homeViewModel.uiState.collect { state ->
                    state.fullName?.let { fullName ->
                        state.department?.let { department ->
                            updateDrawerHeader(fullName, department)
                        }
                    }
                    state.errorMessage?.let {
                        Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
                        homeViewModel.clearErrorMessage()
                    }
                }
            }
        }
    }

    private fun updateDrawerHeader(fullName: String, department: String) {
        val headerView = binding.navigationView.getHeaderView(0)
        val userNameTextView = headerView.findViewById<TextView>(R.id.drawer_user_name)
        val departmentTextView = headerView.findViewById<TextView>(R.id.drawer_user_department)
        userNameTextView.text = fullName
        departmentTextView.text = department
    }



    private fun setupToolbar() {
        val activity = requireActivity() as AppCompatActivity
        activity.setSupportActionBar(binding.toolbar)

        drawerToggle = ActionBarDrawerToggle(
            activity,
            binding.drawerLayout,
            binding.toolbar,
            R.string.drawer_open,
            R.string.drawer_close
        )
        binding.drawerLayout.addDrawerListener(drawerToggle)
        drawerToggle.syncState()
        setupDrawer()
    }

    private fun setupDrawer() {
        binding.navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_profile -> {
                    navigateToProfileFragment()
                    binding.drawerLayout.closeDrawer(GravityCompat.START)
                    true
                }

                R.id.logout -> {
                    handleLogout()
                    binding.drawerLayout.closeDrawer(GravityCompat.START)
                    true
                }

                else -> false
            }
        }
    }

    private fun setupTabs() {
        val tabLayoutMediator =
            TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
                when (position) {
                    0 -> tab.text = "Announcements"
                    1 -> tab.text = "Events"
                    2 -> tab.text = "Forum"
                }
            }

        tabAdapter = TabAdapter(this)
        binding.viewPager.adapter = tabAdapter
        tabLayoutMediator.attach()
    }

    private fun showTabs() {
        binding.tabLayout.visibility = View.VISIBLE
        binding.viewPager.visibility = View.VISIBLE
        binding.fragmentContainer.visibility = View.GONE
    }

    private fun setupBottomNavigation() {
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.homeFragment -> {
                    showTabs()
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
            Toast.makeText(
                requireContext(),
                "User email not found. Please login again.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun handleLogout() {
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.nav_host_fragment, LoginFragment())
            .commit()
        Toast.makeText(requireContext(), "Logged out", Toast.LENGTH_SHORT).show()
    }


    private fun showLogoutConfirmationDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Confirm Logout")
            .setMessage("Are you sure you want to logout?")
            .setPositiveButton("Yes") { _, _ ->
                navigateToLoginFragment()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun navigateToLoginFragment() {
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.nav_host_fragment, LoginFragment())
            .addToBackStack(null)
            .commit()
        Toast.makeText(requireContext(), "Logged out", Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}