package com.example.weatherforecast.ui.search

import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherforecast.data.database.CityEntity
import com.example.weatherforecast.databinding.FragmentSearchBinding
import com.example.weatherforecast.ui.search.adapter.CitiesAdapter
import com.example.weatherforecast.utils.StoredCity
import com.example.weatherforecast.view.search.SearchIntent
import com.example.weatherforecast.view.search.SearchState
import com.example.weatherforecast.view.search.SearchViewModel
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SearchFragment : Fragment() {

    //Binding
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var citiesAdapter: CitiesAdapter

    @Inject
    lateinit var storedCity: StoredCity

    //Other
    private val viewModel by viewModels<SearchViewModel>()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSearchBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //InitViews
        binding.apply {
            //Get data
            lifecycleScope.launch {
                viewModel.searchState.collect { state ->
                    when (state) {
                        is SearchState.Idle -> {}
                        is SearchState.ShowCities -> {
                            loadData(state.cities)
                        }
                    }
                }
            }
            //Setting of search box
            searchLay.isStartIconVisible = false
            searchEdt.addTextChangedListener { txt ->
                if (txt.toString().isNotEmpty()) {
                    //List is visible
                    citiesRv.isVisible = true
                    //Icon delete is visible
                    searchLay.isStartIconVisible = true
                    //Get cities
                    lifecycleScope.launch {
                        viewModel.intent.send(SearchIntent.Searching("%${txt.toString()}"))
                    }
                } else {
                    //List is not visible
                    citiesRv.isVisible = false
                    //Icon delete is not visible
                    searchLay.isStartIconVisible = false
                }
            }
            //Get location
            tvLocation.setOnClickListener {
                getLocation()
            }
        }
    }

    private fun loadData(cities: List<CityEntity>) {
        //Init adapter
        citiesAdapter.setData(cities)
        //InitViews
        binding.apply {
            citiesRv.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = citiesAdapter
            }
        }
        //Click
        citiesAdapter.setClickOnItem { cityEntity ->
            lifecycleScope.launch {
                storedCity.saveData(cityEntity.lat!!.toDouble(), cityEntity.lon!!.toDouble())
                val direction = SearchFragmentDirections.SearchToHome()
                findNavController().navigate(direction)
            }
        }
    }

    private fun getLocation(){
        if (ActivityCompat.checkSelfPermission(
                requireActivity(),
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) !=
            PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                requireActivity(),
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) !=
            PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(), arrayOf(
                    android.Manifest.permission.ACCESS_COARSE_LOCATION,
                    android.Manifest.permission.ACCESS_FINE_LOCATION
                ), 100
            )
        }
        //Get location
        val getLocation = LocationServices.getFusedLocationProviderClient(requireActivity()).lastLocation
        getLocation.addOnSuccessListener { location ->
            if (location == null) {
                showAlert()
            } else {
                lifecycleScope.launch {
                    storedCity.saveData(location.latitude, location.longitude)
                    val direction = SearchFragmentDirections.SearchToHome()
                    findNavController().navigate(direction)
                }
            }
        }
    }

    private fun showAlert() {
        val alertDialog = AlertDialog.Builder(requireContext())
        alertDialog.setTitle("Enable Location")
            .setMessage("Your Locations Settings is set to 'Off'.\nPlease Enable Location to " + "use this app")
            .setPositiveButton("Location Settings") { dialog, which ->
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                requireActivity().startActivity(intent)
            }
            .setNegativeButton("Cancel") { dialog, which ->
                dialog.dismiss()
            }
        alertDialog.show()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}