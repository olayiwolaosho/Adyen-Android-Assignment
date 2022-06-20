package com.adyen.android.assignment.ui.apods

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.adyen.android.assignment.api.model.AstronomyPicture
import com.adyen.android.assignment.databinding.ApodItemBinding

class ApodsAdapter : ListAdapter<AstronomyPicture, RecyclerView.ViewHolder>(ApodsDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ApodsViewHolder(
            ApodItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val astronomyData = getItem(position)

        (holder as ApodsViewHolder).bind(astronomyData)
    }

    class ApodsViewHolder(
        private val binding: ApodItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.setClickListener {
                binding.apods?.let { apods ->
                    navigateToDetails(apods, it)
                }
            }
        }

        private fun navigateToDetails(
            plant: AstronomyPicture,
            view: View
        ) {
            /*val direction =
                HomeViewPagerFragmentDirections.actionViewPagerFragmentToPlantDetailFragment(
                    plant.plantId
                )
            view.findNavController().navigate(direction)*/
        }

        fun bind(item: AstronomyPicture) {
            binding.apply {
                apods = item
                executePendingBindings()
            }
        }
    }
}

private class ApodsDiffCallback : DiffUtil.ItemCallback<AstronomyPicture>() {

    override fun areItemsTheSame(oldItem: AstronomyPicture, newItem: AstronomyPicture): Boolean {
        return oldItem.title == newItem.title
    }

    override fun areContentsTheSame(oldItem: AstronomyPicture, newItem: AstronomyPicture): Boolean {
        return oldItem == newItem
    }
}
