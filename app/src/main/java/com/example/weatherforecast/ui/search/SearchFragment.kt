package com.example.weatherforecast.ui.search

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherforecast.databinding.FragmentSearchBinding
import com.example.weatherforecast.utils.showKeyboard
import com.example.weatherforecast.view.search.SearchIntent
import com.example.weatherforecast.view.search.SearchState
import com.example.weatherforecast.view.search.SearchViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SearchFragment : Fragment() {

    //Binding
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    //Other
    private val viewModel by viewModels<SearchViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentSearchBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //InitViews
        binding.apply {
            //Show keyboard
            searchEdt.showKeyboard(requireActivity())
            //Setting of search box
            searchLay.isStartIconVisible = false
            searchEdt.addTextChangedListener {txt->
                if (txt.toString().isNotEmpty()){
                    searchLay.isStartIconVisible = true
                    //Get cities
                    lifecycleScope.launch {
                        viewModel.intent.send(SearchIntent.Searching("%${txt.toString()}"))
                    }
                }else{
                    searchLay.isStartIconVisible = false
                }

            }
        }
        //Get data
        lifecycleScope.launch {
            viewModel.searchState.collect{state->
                when(state){
                    is SearchState.Idle->{}
                }
            }
        }

    }



    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}