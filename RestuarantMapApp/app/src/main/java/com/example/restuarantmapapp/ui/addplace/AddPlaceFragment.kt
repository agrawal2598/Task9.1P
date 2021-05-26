package com.example.restuarantmapapp.ui.addplace

import android.Manifest
import android.content.ContentValues.TAG
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.core.app.ActivityCompat.requestPermissions
import androidx.core.content.ContextCompat
import com.example.restuarantmapapp.R
import com.example.restuarantmapapp.model.MyPlace
import com.example.restuarantmapapp.ui.BaseFragment
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.Status
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import java.util.*


class AddPlaceFragment : BaseFragment() {

    private lateinit var autocompleteFragment: AutocompleteSupportFragment
    private val placesClient by lazy {
        context?.let { Places.createClient(it) }
    }


    companion object {
        var place: MyPlace? = null
        var placeList: ArrayList<MyPlace> = arrayListOf()
        var isFromAddPlaceFragment = true
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_place, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpMap()
        view.findViewById<Button>(R.id.show_on_map_btn).setOnClickListener {
            navController.navigate(R.id.action_addPlaceFragment_to_mapsFragment)
        }

        view.findViewById<Button>(R.id.get_current_location_btn).setOnClickListener {
            getLocation()
        }

        view.findViewById<Button>(R.id.save_btn).setOnClickListener {
            if (place != null) {
                place?.let { it1 -> placeList.add(it1) }
                Toast.makeText(context, "Details added", Toast.LENGTH_SHORT).show()
                place = null
                view.findViewById<EditText>(R.id.place_name_label).text.clear()
                autocompleteFragment.setText("")
                autocompleteFragment.setHint("Location")
            } else
                Toast.makeText(context, "Please enter the address", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getLocation() {
        val progressBar = view?.findViewById<ProgressBar>(R.id.progress_bar)
        val placeFields: List<Place.Field> = listOf(Place.Field.NAME, Place.Field.LAT_LNG)

        val request: FindCurrentPlaceRequest = FindCurrentPlaceRequest.newInstance(placeFields)

        if (context?.let {
                ContextCompat.checkSelfPermission(
                    it,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            } ==
            PackageManager.PERMISSION_GRANTED) {

            progressBar?.visibility = View.VISIBLE
            val placeResponse = placesClient?.findCurrentPlace(request)
            placeResponse?.addOnCompleteListener { task ->
                progressBar?.visibility = View.GONE
                if (task.isSuccessful) {
                    val response = task.result
                    if (response.placeLikelihoods.isNotEmpty()) {
                        val first = response?.placeLikelihoods?.first()
                        place = MyPlace()
                        place?.latLng = first?.place?.latLng
                        val placeLabel = view?.findViewById<EditText>(R.id.place_name_label)
                        placeLabel?.setText(first?.place?.name)
                    }
                } else {
                    val exception = task.exception
                    if (exception is ApiException) {
                        Log.e(TAG, "Place not found: ${exception.statusCode}")
                    }
                }
            }
        } else
            getLocationPermission()

    }

    private fun getLocationPermission() {
        activity?.let {
            requestPermissions(
                it, arrayOf(
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ),
                0
            )
        }
    }

    private fun setUpMap() {
        // initialize the SDK
        if (!Places.isInitialized())
            context?.let {
                Places.initialize(
                    it,
                    getString(R.string.google_maps_key),
                    Locale.getDefault()
                )
            }

         autocompleteFragment =
            childFragmentManager.findFragmentById(R.id.autocomplete_fragment) as AutocompleteSupportFragment
        autocompleteFragment.setPlaceFields(
            listOf(
                Place.Field.ADDRESS,
                Place.Field.NAME,
                Place.Field.LAT_LNG
            )
        );
        autocompleteFragment.setHint("Location")
        autocompleteFragment.setCountry("aus")
        autocompleteFragment.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onPlaceSelected(p0: Place) {
                place = MyPlace()
                place?.latLng = p0.latLng
            }

            override fun onError(p0: Status) {

            }
        })
    }

}