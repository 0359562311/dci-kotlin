package com.tankiem.kotlin.dci.app.presentation.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.tankiem.kotlin.dci.R
import com.tankiem.kotlin.dci.app.presentation.notifications.ListNotificationsFragment
import com.tankiem.kotlin.dci.app.presentation.settings.SettingFragment
import com.tankiem.kotlin.dci.utils.MyBroadcastReceiver
import com.tankiem.kotlin.dci.utils.MyLocalBroadcastReceiver

class MainFragment : Fragment() {

    private lateinit var myBroadcastReceiver: MyBroadcastReceiver
    private lateinit var myLocalBroadcastReceiver: MyLocalBroadcastReceiver
    private var currentBottomBarId = R.id.bottom_nav_home
    private lateinit var fragments: MutableList<Fragment>
    private lateinit var navController: NavController
    private val bottomId = listOf(
        R.id.bottom_nav_home,
        R.id.bottom_nav_notification,
        R.id.bottom_nav_study_corner,
        R.id.bottom_nav_facility,
        R.id.bottom_nav_personal_info,
    )
    private val fragmentTags = listOf("MainHome", "MainNotification", "MainStudyCorner", "MainEvent", "MainPersonalInfo")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_main, container, false)

        if(savedInstanceState != null) {
            currentBottomBarId = savedInstanceState.getInt("currentBottomBarId", currentBottomBarId)
            fragments = mutableListOf()
            for (i in 0 until 5) {
                fragments.add(childFragmentManager.findFragmentByTag(fragmentTags[i])!!)
            }
        } else {
            fragments = mutableListOf(
                SettingFragment(),
                ListNotificationsFragment(),
                ListNotificationsFragment(),
                ListNotificationsFragment(),
                ListNotificationsFragment(),
            )
            for (i in 0 until fragments.size) {
                childFragmentManager.beginTransaction()
                    .add(R.id.main_fragment, fragments[i], fragmentTags[i])
                    .hide(fragments[i])
                    .commit()
            }
        }

        val bottomNavigationView = view.findViewById<BottomNavigationView>(R.id.bottom_nav_bar)
        childFragmentManager.beginTransaction()
            .show(fragments[0])
            .commit()
        bottomNavigationView.setOnItemSelectedListener { listener ->
            if (listener.itemId == currentBottomBarId) {
                return@setOnItemSelectedListener false
            }
            childFragmentManager.beginTransaction()
                .show(fragments[0])
            childFragmentManager.beginTransaction()
                .show(fragments[bottomId.indexOf(listener.itemId)])
                .hide(fragments[bottomId.indexOf(currentBottomBarId)])
                .commit()
            currentBottomBarId = listener.itemId
            true
        }
        // Inflate the layout for this fragment
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        navController = Navigation.findNavController(view)
        navController.navigate(R.id.action_mainFragment_to_loginFragment)
        super.onViewCreated(view, savedInstanceState)
    }
}