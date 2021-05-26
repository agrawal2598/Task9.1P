package com.example.restuarantmapapp.ui

import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import com.example.restuarantmapapp.BaseActivity

open class BaseFragment : Fragment() {

    val navController: NavController by lazy { (activity as BaseActivity).navController }
}