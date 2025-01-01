package com.project.lycommunity.ui.splash

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.project.lycommunity.R
import com.project.lycommunity.databinding.FragmentSplashBinding
import com.project.lycommunity.ui.login.LoginFragment
import kotlinx.coroutines.launch

class SplashFragment : Fragment() {

    private var _binding : FragmentSplashBinding? = null
    private val binding get() = _binding!!

    private val viewModelOfSplash: SplashViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSplashBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        observed()

    }

    private fun initViews(){
        viewModelOfSplash.showSplash2()
    }

    private fun observed(){
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModelOfSplash.splashStateFlow.collect {
                    if (it.isLoading){
                        binding.splash.playAnimation()
                    }
                    if (it.finishedPlaying){
                        binding.splash.visibility = View.GONE // Hide animation
                        goToLogin()
                    }
//                    else (){
//                        showError(uiState)
//                    }
                }
            }
        }
    }

    private fun goToLogin(){
        val navController = findNavController()
        navController.navigate(R.id.loginFragment)
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.nav_host_fragment, LoginFragment())
            .addToBackStack(null) // Add to backstack so the user can navigate back
            .commit()
    }

    private fun showError(message: String?) {
        message?.let {
            Toast.makeText(requireContext(), "Error: $it", Toast.LENGTH_LONG).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}