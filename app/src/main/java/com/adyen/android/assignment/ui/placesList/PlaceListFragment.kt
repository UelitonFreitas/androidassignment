package com.adyen.android.assignment.ui.placesList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.adyen.android.assignment.R
import com.adyen.android.assignment.binding.AutoClearedValue
import com.adyen.android.assignment.binding.FragmentDataBindingComponent
import com.adyen.android.assignment.databinding.FragmentPlacesListBinding
import com.adyen.android.assignment.di.Injectable
import com.adyen.android.assignment.executors.AppExecutors
import com.adyen.android.assignment.ui.placesList.adapters.PlacesListAdapter
import com.google.android.material.snackbar.Snackbar
import javax.inject.Inject

class PlaceListFragment : Fragment(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var appExecutors: AppExecutors

    private var dataBindingComponent: DataBindingComponent = FragmentDataBindingComponent(this)

    var binding by autoCleared<FragmentPlacesListBinding>()

    var adapter by autoCleared<PlacesListAdapter>()

    private val placesListViewModel: PlacesListViewModel by viewModels {
        viewModelFactory
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_places_list,
            container,
            false,
            dataBindingComponent
        )


        placesListViewModel.shouldShowSpinner.observe(viewLifecycleOwner) { show ->
            binding.loadingLayout.visibility = if (show) View.VISIBLE else View.GONE
        }

        placesListViewModel.snackbar.observe(viewLifecycleOwner) { text ->
            text?.let {
                Snackbar.make(binding.root, text, Snackbar.LENGTH_SHORT).show()
                placesListViewModel.onSnackbarShown()
            }
        }

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.lifecycleOwner = viewLifecycleOwner

        initRecyclerView()

        val placesListAdapter = PlacesListAdapter(
            dataBindingComponent = dataBindingComponent,
            appExecutors = appExecutors
        ) { place ->
        }
        binding.places = placesListViewModel.places
        binding.recyclerViewPlacesList.adapter = placesListAdapter
        adapter = placesListAdapter
    }

    private fun initRecyclerView() {

        binding.places = placesListViewModel.places
        placesListViewModel.places.observe(viewLifecycleOwner, { result ->
            adapter.submitList(result)
        })
    }

    interface RetryButtonCallback {
        fun retry()
    }

    override fun onResume() {
        super.onResume()
        placesListViewModel.loadPlaces()
    }
}

fun <T : Any> Fragment.autoCleared() = AutoClearedValue<T>(this)
