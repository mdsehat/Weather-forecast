package com.example.weatherforecast.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.weatherforecast.R
import com.example.weatherforecast.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity: AppCompatActivity() {
    //Binding
    private var _binding : ActivityMainBinding?= null
    private val binding get() = _binding

    //Other
    private lateinit var navHostFragment:NavHostFragment
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        //Navigation
        navHostFragment = supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment
        //Bottom nav
        binding!!.btmNav.apply {
            setupWithNavController(navHostFragment.navController)
            setOnNavigationItemReselectedListener {}
        }
        //Destination
        navHostFragment.navController.addOnDestinationChangedListener{_,destination,_ ->
            if (destination.id == R.id.splashFragment){
                binding?.btmNav?.visibility = View.INVISIBLE
            }else{
                binding?.btmNav?.visibility = View.VISIBLE
            }

        }
    }

    override fun onNavigateUp(): Boolean {
        return super.onNavigateUp() || navHostFragment.navController.navigateUp()
    }
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}