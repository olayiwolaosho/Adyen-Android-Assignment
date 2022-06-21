package com.adyen.android.assignment.ui.apods

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.adyen.android.assignment.R
import com.adyen.android.assignment.api.dao.AstronomyPictureDao
import com.adyen.android.assignment.data.Resource
import com.adyen.android.assignment.databinding.FragmentListScreenBinding
import com.adyen.android.assignment.ui.callbacks.RefreshListener
import com.adyen.android.assignment.ui.dialog.ShowCustomDialog
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ListScreenFragment : Fragment(),
    RefreshListener {

    private lateinit var binding: FragmentListScreenBinding

    private lateinit var adapter: ApodsAdapter

    private val viewModel: ApodsViewModel by activityViewModels()

    @Inject
    lateinit var dialog: ShowCustomDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentListScreenBinding.inflate(inflater, container, false)

        return binding.root

    }

    override fun onStart() {
        super.onStart()

        initObservers()

        initListeners()

        initViews()

    }

    private fun initViews() {

        setupAdapter()

        getApods()

    }

    private fun initObservers() {

        viewModel.filteredApods().observe(viewLifecycleOwner,filteredApodsObserver())

        viewModel.allApods().observe(viewLifecycleOwner,allApodsObserver())

    }

    private fun initListeners() {

        binding.reorderList.setOnClickListener {

            val action =
                ListScreenFragmentDirections.actionListScreenFragmentToReorderFragment()

            findNavController().navigate(action)

        }


    }


    private fun getApods() {

        viewModel.getApods()

    }

    private fun allApodsObserver(): Observer<Resource<List<AstronomyPictureDao>>> {

        return Observer { result ->

            dialog.dismiss()

            when(result.status){

                Resource.Status.LOADING ->{

                    dialog.showOnFullScreen(R.layout.loading_screen,this)

                }

                Resource.Status.SUCCESS ->{

                    val astronomyPictures = result.data!!

                    adapter.submitList(astronomyPictures)

                    return@Observer

                }

                Resource.Status.ERROR -> {

                    dialog.showOnFullScreen(R.layout.error_screen,this)

                }

                Resource.Status.NO_NETWORK -> {

                    dialog.showOnFullScreen(R.layout.network_error_screen,this)

                }

            }

        }

    }

    private fun filteredApodsObserver(): Observer<Resource<List<AstronomyPictureDao>>> {

        return Observer { result ->

            when(result.status){

                Resource.Status.LOADING ->{

                    dialog.showOnFullScreen(R.layout.loading_screen,this)

                }

                Resource.Status.SUCCESS ->{

                    val astronomyPictures = result.data!!

                    adapter.submitList(astronomyPictures)

                    return@Observer

                }

                Resource.Status.ERROR -> {

                    dialog.showOnFullScreen(R.layout.error_screen,this)

                }

                Resource.Status.NO_NETWORK -> {

                    dialog.showOnFullScreen(R.layout.network_error_screen,this)

                }

            }

        }


    }

    private fun setupAdapter() {

        adapter = ApodsAdapter()

        binding.recyclerViewApodsList.adapter = adapter

    }

    override fun refresh() {

        getApods()

    }

}