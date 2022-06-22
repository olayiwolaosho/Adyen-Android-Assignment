package com.adyen.android.assignment.ui.apods

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.adyen.android.assignment.data.FAVOURITE_LIST
import com.adyen.android.assignment.data.db.FavouriteAstronomyPictureEnt
import com.adyen.android.assignment.databinding.FavouriteItemBinding

class FavouriteApodsAdapter : ListAdapter<FavouriteAstronomyPictureEnt, RecyclerView.ViewHolder>(FavouriteApodsDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ApodsViewHolder(
            FavouriteItemBinding.inflate(
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
        private val binding: FavouriteItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.setClickListener {
                binding.apods?.let { apods ->
                    navigateToDetails(apods, it)
                }
            }
        }

        private fun navigateToDetails(
            astronomyData : FavouriteAstronomyPictureEnt,
            view: View
        ) {
            val direction =
                ListScreenFragmentDirections.actionListScreenFragmentToApodsDetailFragment(
                    astronomyData.id,
                    FAVOURITE_LIST
                )
            view.findNavController().navigate(direction)
        }

        fun bind(item: FavouriteAstronomyPictureEnt) {
            binding.apply {
                apods = item
                executePendingBindings()
            }
        }
    }
}

class FavouriteApodsDiffCallback : DiffUtil.ItemCallback<FavouriteAstronomyPictureEnt>() {

    override fun areItemsTheSame(oldItem: FavouriteAstronomyPictureEnt, newItem: FavouriteAstronomyPictureEnt): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: FavouriteAstronomyPictureEnt, newItem: FavouriteAstronomyPictureEnt): Boolean {
        return oldItem == newItem
    }
}
