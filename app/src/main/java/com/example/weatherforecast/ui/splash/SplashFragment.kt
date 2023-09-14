package com.example.weatherforecast.ui.splash

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.weatherforecast.R
import com.example.weatherforecast.databinding.SplashFragmentBinding
import com.example.weatherforecast.utils.StoredCity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SplashFragment : Fragment() {
    //Binding
    private var _binding : SplashFragmentBinding ?= null
    private val binding get() = _binding

    @Inject
    lateinit var storedCity: StoredCity

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = SplashFragmentBinding.inflate(layoutInflater)
        return binding!!.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launch {
            delay(3000)
            if (storedCity.getData.first() == null){
                findNavController().popBackStack(R.id.splashFragment, true)
                findNavController().navigate(R.id.searchFragment)
            }else{
                findNavController().popBackStack(R.id.splashFragment, true)
                findNavController().navigate(R.id.homeFragment)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}