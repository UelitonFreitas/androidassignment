package com.adyen.android.assignment.ui.placesList.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import com.adyen.android.assignment.R
import com.adyen.android.assignment.databinding.PlaceItemBinding
import com.adyen.executors.AppExecutors
import com.adyen.model.Place

class PlacesListAdapter(
    private val dataBindingComponent: DataBindingComponent,
    appExecutors: AppExecutors,
    private val repoClickCallback: ((Place) -> Unit)?
) : DataBoundListAdapter<Place, PlaceItemBinding>(
    appExecutors = appExecutors,
    diffCallback = object : DiffUtil.ItemCallback<Place>() {
        override fun areItemsTheSame(oldItem: Place, newItem: Place): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: Place, newItem: Place): Boolean {
            return oldItem.name == newItem.name
        }
    }
) {

    override fun createBinding(parent: ViewGroup): PlaceItemBinding {
        val binding = DataBindingUtil.inflate<PlaceItemBinding>(
            LayoutInflater.from(parent.context),
            R.layout.place_item,
            parent,
            false,
            dataBindingComponent
        )
        binding.root.setOnClickListener {
            binding.place?.let {
                repoClickCallback?.invoke(it)
            }
        }
        return binding
    }

    override fun bind(binding: PlaceItemBinding, place: Place) {
        binding.place = place
    }
}