package com.project.lycommunity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.project.lycommunity.databinding.ActivityMainBinding
import com.project.lycommunity.ui.login.LoginFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val currentFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment)

                // If current fragment is LoginFragment, show exit confirmation
                if (currentFragment is LoginFragment) {
                    showExitConfirmationDialog()
                } else {
                    // If not on LoginFragment, navigate back as usual
                    onBackPressedDispatcher.onBackPressed()
                }
            }
        })
    }



    private fun showExitConfirmationDialog() {
        MaterialAlertDialogBuilder(this)
//        val builder = AlertDialog.Builder(this)
            .setMessage("Are you sure you want to exit?")
            .setTitle("Exit Message")
            .setCancelable(false)
            .setPositiveButton("Yes") { dialog, _ ->
                // Close the app if "Yes" is clicked
                finishAffinity() // Close all activities and exit the app
            }
            .setNegativeButton("No") { dialog, _ ->
                // If "No" is clicked, dismiss the dialog and stay on the LoginFragment
                dialog.dismiss()
            }

            .show()
    }
}