package com.adyen.android.assignment.ui.apods

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.adyen.android.assignment.R
import com.adyen.android.assignment.api.model.AstronomyPicture
import com.adyen.android.assignment.databinding.FragmentListScreenBinding

class ListScreenFragment : Fragment() {

    private lateinit var binding: FragmentListScreenBinding

    private val viewModel: ApodsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentListScreenBinding.inflate(inflater, container, false)

        //val adapter = GardenPlantingAdapter()

        //binding..adapter = adapter

        initObservers()

        initListeners()

        initViews()

        return binding.root

    }

    private fun initViews() {

        getApods()

    }

    private fun initObservers() {

        viewModel.allApods().observe(viewLifecycleOwner,allApodsObserver())

    }

    private fun initListeners() {

        /*binding.addPlant.setOnClickListener {
            navigateToPlantListPage()
        }*/

    }


    private fun getApods() {


    }

    private fun allApodsObserver(): Observer<List<AstronomyPicture>> {

    }

}