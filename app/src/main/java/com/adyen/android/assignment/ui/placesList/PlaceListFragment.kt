package com.adyen.android.assignment.ui.placesList

import android.content.Context
import android.os.Bundle
import android.os.IBinder
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.adyen.android.assignment.R
import com.adyen.android.assignment.binding.AutoClearedValue
import com.adyen.android.assignment.binding.FragmentDataBindingComponent
import com.adyen.android.assignment.databinding.FragmentPlacesListBinding
import com.adyen.di.Injectable
import com.adyen.executors.AppExecutors
import com.adyen.android.assignment.ui.placesList.adapters.PlacesListAdapter
import com.google.android.material.snackbar.Snackbar
import javax.inject.Inject

class PlaceListFragment : Fragment(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var appExecutors: AppExecutors

    var dataBindingComponent: DataBindingComponent = FragmentDataBindingComponent(this)

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
            binding.progressBar.visibility = if (show) View.VISIBLE else View.GONE
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

        binding.query = placesListViewModel.query

        initSearchInputListener()
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

    private fun initSearchInputListener() {
        binding.input.setOnEditorActionListener { view: View, actionId: Int, _: KeyEvent? ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                doSearch(view)
                true
            } else {
                false
            }
        }
        binding.input.setOnKeyListener { view: View, keyCode: Int, event: KeyEvent ->
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                doSearch(view)
                true
            } else {
                false
            }
        }
    }

    private fun doSearch(v: View) {
        val query = binding.input.text.toString()
        // Dismiss keyboard
        dismissKeyboard(v.windowToken)
        placesListViewModel.setQuery(query)
    }

    private fun dismissKeyboard(windowToken: IBinder) {
        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        imm?.hideSoftInputFromWindow(windowToken, 0)
    }

    override fun onResume() {
        super.onResume()
    }
}

fun <T : Any> Fragment.autoCleared() = AutoClearedValue<T>(this)
