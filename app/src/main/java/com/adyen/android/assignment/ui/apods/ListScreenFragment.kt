package com.adyen.android.assignment.ui.apods

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
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

    private val viewModel: ApodsViewModel by viewModels()

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

        initObservers()

        initListeners()

        initViews()

        return binding.root

    }

    private fun initViews() {

        //setupAdapter()

        getApods()

    }

    private fun initObservers() {

        viewModel.allApods().observe(viewLifecycleOwner,allApodsObserver())

    }

    private fun initListeners() {


    }


    private fun getApods() {

        viewModel.getApods()

    }

    private fun allApodsObserver(): Observer<Resource<List<AstronomyPicture>>> {

        return Observer { result ->

            when(result.status){

                Resource.Status.LOADING ->{

                    //dialog.showLoading(getString(R.string.getting_apods))
                    dialog.show(R.layout.loading_screen,parentFragmentManager)

                    return@Observer
                }

                Resource.Status.SUCCESS ->{

                    dialog.dismiss()

                    val astronomyPictures = result.data!!

                    //adapter.notifyItemRangeChanged(0,astronomyPictures.size)

                }

                Resource.Status.ERROR -> {

                    dialog.show(R.layout.error_screen,parentFragmentManager)

                }

                Resource.Status.NO_NETWORK -> {

                    dialog.show(R.layout.network_error_screen,parentFragmentManager)
                    //dialog.showNoNetwork()

                }

            }

        }

    }

    private fun setupAdapter() {

        //val viewModel = binding.viewmodel

        if (viewModel != null) {

            adapter = ApodsAdapter()

            //binding.billServiceList.adapter = adapter

        } else {

            Log.w("ViewModel Error","ViewModel not initialized when attempting to set up adapter.")

        }
    }

}