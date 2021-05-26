package com.example.restuarantmapapp.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.restuarantmapapp.R
import com.example.restuarantmapapp.ui.BaseFragment
import com.example.restuarantmapapp.ui.addplace.AddPlaceFragment.Companion.isFromAddPlaceFragment

class HomeFragment : BaseFragment() {

    private lateinit var homeViewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<Button>(R.id.add_new_place_button).setOnClickListener {
            navController.navigate(R.id.action_navigation_home_to_addPlaceFragment)
        }
        view.findViewById<Button>(R.id.show_all_button).setOnClickListener {
            isFromAddPlaceFragment  = false
            navController.navigate(R.id.action_navigation_home_to_mapsFragment)
        }
    }

}