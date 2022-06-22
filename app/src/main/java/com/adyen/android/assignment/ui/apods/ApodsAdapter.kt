package com.adyen.android.assignment.ui.apods

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.adyen.android.assignment.data.MAIN_LIST
import com.adyen.android.assignment.data.db.AstronomyPictureEnt
import com.adyen.android.assignment.databinding.ApodItemBinding

class ApodsAdapter : ListAdapter<AstronomyPictureEnt, RecyclerView.ViewHolder>(ApodsDiffCallback()) {

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
            astronomyData : AstronomyPictureEnt,
            view: View
        ) {
            val direction =
                ListScreenFragmentDirections.actionListScreenFragmentToApodsDetailFragment(
                    astronomyData.id,
                    MAIN_LIST
                )
            view.findNavController().navigate(direction)
        }

        fun bind(item: AstronomyPictureEnt) {
            binding.apply {
                apods = item
                executePendingBindings()
            }
        }
    }
}

class ApodsDiffCallback : DiffUtil.ItemCallback<AstronomyPictureEnt>() {

    override fun areItemsTheSame(oldItem: AstronomyPictureEnt, newItem: AstronomyPictureEnt): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: AstronomyPictureEnt, newItem: AstronomyPictureEnt): Boolean {
        return oldItem == newItem
    }
}
