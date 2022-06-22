package com.adyen.android.assignment.ui.apods

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.adyen.android.assignment.R
import com.adyen.android.assignment.api.dao.AstronomyPictureDao
import com.adyen.android.assignment.data.extensions.loadImage
import com.adyen.android.assignment.databinding.FragmentApodsDetailBinding
import com.adyen.android.assignment.ui.bindings.bindImageFromUrl
import com.adyen.android.assignment.util.Globals

class ApodsDetailFragment : Fragment() {

    private lateinit var astronomyData : AstronomyPictureDao

    private val viewModel: ApodsViewModel by activityViewModels()

    private lateinit var binding: FragmentApodsDetailBinding

    private val args : ApodsDetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentApodsDetailBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initListeners()

        initViews()
    }

    private fun initViews() {

        viewModel.allApods().value?.data?.let { apodData ->

            //args.astronomyId index starts at one so we use -1 to get its index
            astronomyData = apodData[args.astronomyId - 1]

            binding.imageViewBackground.loadImage(astronomyData.url)

            val favouriteResource = if(astronomyData.favourite) R.drawable.ic_favorite_filled else R.drawable.ic_favorite_border

            binding.imageViewLove.setImageDrawable(ResourcesCompat.getDrawable(resources, favouriteResource ,context?.theme))

            binding.textViewApodTitle.text = astronomyData.title

            binding.textViewDate.text = Globals.convertDate(astronomyData.date.toString())

            binding.textViewExplanation.text = astronomyData.explanation

        }

    }

    private fun initListeners() {

        binding.back.setOnClickListener {

            findNavController().popBackStack()

        }

        binding.imageViewLove.setOnClickListener{

            val favouriteResource = if(astronomyData.favourite) R.drawable.ic_favorite_border else R.drawable.ic_favorite_filled

            binding.imageViewLove.setImageDrawable(ResourcesCompat.getDrawable(resources, favouriteResource ,context?.theme))

            astronomyData.favourite = !astronomyData.favourite

            viewModel.addFavourite(astronomyData.id - 1)

        }

    }
}