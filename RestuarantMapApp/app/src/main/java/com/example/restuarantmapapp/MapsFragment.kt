package com.example.restuarantmapapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.restuarantmapapp.ui.addplace.AddPlaceFragment
import com.example.restuarantmapapp.ui.addplace.AddPlaceFragment.Companion.isFromAddPlaceFragment
import com.example.restuarantmapapp.ui.addplace.AddPlaceFragment.Companion.placeList
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapsFragment : Fragment() {

    private val callback = OnMapReadyCallback { googleMap ->
        val place = AddPlaceFragment.place
        if (isFromAddPlaceFragment)
            place?.latLng?.let {
                val sydney = LatLng(it.latitude, it.longitude)
                googleMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(sydney, 12f))
            }
        else
            placeList.forEach {
                it.latLng?.let { it1 ->
                    val sydney = LatLng(it1.latitude, it1.longitude)
                    googleMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(sydney, 12f))
                }
            }


    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_maps, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }
}