package com.project.lycommunity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.OnBackPressedCallback
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.FirebaseApp
import com.project.lycommunity.databinding.ActivityMainBinding
import com.project.lycommunity.ui.login.LoginFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (FirebaseApp.getApps(this).isEmpty()) {
            Log.e("FirebaseInit", "Firebase is not initialized!")
        } else {
            Log.d("FirebaseInit", "Firebase is initialized.")
        }

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