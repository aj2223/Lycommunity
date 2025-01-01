package com.project.lycommunity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.OnBackPressedCallback
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.FirebaseApp
import com.project.lycommunity.databinding.ActivityMainBinding
import com.project.lycommunity.ui.login.LoginFragment
import com.project.lycommunity.ui.parent.ParentFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        FirebaseApp.initializeApp(this)
        Log.d("FirebaseInit", "Firebase initialized successfully")

//        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
//            override fun handleOnBackPressed() {
//                val currentFragment =
//                    supportFragmentManager.findFragmentById(R.id.nav_host_fragment)?.childFragmentManager?.fragments?.firstOrNull()
//
//                // If current fragment is LoginFragment, show exit confirmation
//                if (currentFragment is LoginFragment) {
//                    showExitConfirmationDialog()
//                } else {
//                    // Safely check if navigation can go back
//                    val navController = currentFragment?.findNavController()
//                    if (navController?.navigateUp() == false) {
//                        finish() // Exit app if no more destinations in the stack
//                    }
//                }
//            }
//        })
//        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
//            override fun handleOnBackPressed() {
//                val currentFragment =
//                    supportFragmentManager.findFragmentById(R.id.nav_host_fragment)
//
//                // If current fragment is LoginFragment, show exit confirmation
//                if (currentFragment is LoginFragment) {
//                    showExitConfirmationDialog()
//                } else {
//                    val navController = currentFragment?.findNavController()
//                    if (navController?.navigateUp() == false) {
//                        finish() // Exit app if no more destinations in the stack
//                    }
//                }
//            }
//        }
//        )
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val currentFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment)
                if (currentFragment is LoginFragment) {
                    showExitConfirmationDialog()
                } else {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.nav_host_fragment, LoginFragment())
                        .addToBackStack(null)
                        .commit()

                }

            }
        })


    }


    private fun showExitConfirmationDialog() {
        MaterialAlertDialogBuilder(this)
            .setMessage("Are you sure you want to exit?")
            .setTitle("Exit Message")
            .setCancelable(false)
            .setPositiveButton("Yes") { dialog, _ ->
                finishAffinity()
            }
            .setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }

            .show()
    }
}