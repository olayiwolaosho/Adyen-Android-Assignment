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
import com.adyen.android.assignment.api.model.AstronomyPicture
import com.adyen.android.assignment.data.Resource
import com.adyen.android.assignment.databinding.FragmentListScreenBinding
import com.adyen.android.assignment.ui.dialog.ShowCustomDialog
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ListScreenFragment : Fragment() {

    private lateinit var binding: FragmentListScreenBinding

    private lateinit var adapter: ApodsAdapter

    private val viewModel: ApodsViewModel by activityViewModels()

    @Inject
    lateinit var dialog: ShowCustomDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentListScreenBinding.inflate(inflater, container, false)

        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initObservers()

        initListeners()

        initViews()
    }

    private fun initViews() {

        setupAdapter()

        getApods()

    }

    private fun initObservers() {

        viewModel.allApods().observe(viewLifecycleOwner,allApodsObserver())

        viewModel.filteredApods().observe(viewLifecycleOwner,filteredApodsObserver())

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

    private fun allApodsObserver(): Observer<Resource<List<AstronomyPicture>>> {

        return Observer { result ->

            when(result.status){

                Resource.Status.LOADING ->{

                    //dialog.showLoading(getString(R.string.getting_apods))
                    dialog.showOnFullScreen(R.layout.loading_screen,parentFragmentManager)

                }

                Resource.Status.SUCCESS ->{

                    dialog.dismiss()

                    val astronomyPictures = result.data!!

                    adapter.submitList(astronomyPictures)

                    return@Observer

                }

                Resource.Status.ERROR -> {

                    dialog.showOnFullScreen(R.layout.error_screen,parentFragmentManager)

                }

                Resource.Status.NO_NETWORK -> {

                    dialog.showOnFullScreen(R.layout.network_error_screen,parentFragmentManager)
                    //dialog.showNoNetwork()

                }

            }

        }

    }

    private fun filteredApodsObserver(): Observer<Resource<List<AstronomyPicture>>> {

        return Observer { result ->

            when(result.status){

                Resource.Status.LOADING ->{

                    dialog.showOnFullScreen(R.layout.loading_screen,parentFragmentManager)

                }

                Resource.Status.SUCCESS ->{

                    dialog.dismiss()

                    val astronomyPictures = result.data!!

                    adapter.submitList(astronomyPictures)

                    return@Observer

                }

                Resource.Status.ERROR -> {

                    dialog.showOnFullScreen(R.layout.error_screen,parentFragmentManager)

                }

                Resource.Status.NO_NETWORK -> {

                    dialog.showOnFullScreen(R.layout.network_error_screen,parentFragmentManager)

                }

            }

        }


    }

    private fun setupAdapter() {

        adapter = ApodsAdapter()

        binding.recyclerViewApodsList.adapter = adapter

    }

}