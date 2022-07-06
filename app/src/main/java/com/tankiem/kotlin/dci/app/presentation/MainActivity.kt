package com.tankiem.kotlin.dci.app.presentation

import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.tankiem.kotlin.dci.R
import com.tankiem.kotlin.dci.app.presentation.main.MainFragment
import com.tankiem.kotlin.dci.utils.MyBroadcastReceiver
import com.tankiem.kotlin.dci.utils.MyLocalBroadcastReceiver

class MainActivity : AppCompatActivity() {

    private lateinit var myBroadcastReceiver: MyBroadcastReceiver
    private lateinit var myLocalBroadcastReceiver: MyLocalBroadcastReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        /// Register broadcast
        myBroadcastReceiver = MyBroadcastReceiver()
        myLocalBroadcastReceiver = MyLocalBroadcastReceiver()
        val intentFilterBroadcast = IntentFilter()
        intentFilterBroadcast.addAction("android.net.conn.CONNECTIVITY_CHANGE")
        registerReceiver(myBroadcastReceiver, intentFilterBroadcast)
        val intentFilterLocalBroadcast = IntentFilter(MyLocalBroadcastReceiver.SESSION_EXPIRED)
        LocalBroadcastManager.getInstance(this)
            .registerReceiver(myLocalBroadcastReceiver, intentFilterLocalBroadcast)
        var fragment = supportFragmentManager.findFragmentById(R.id.fragment_main)
        if (fragment == null) {
            fragment = MainFragment()
            supportFragmentManager.beginTransaction().add(R.id.fragment_main, fragment).commit()
        }
    }

    override fun onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(myLocalBroadcastReceiver)
        unregisterReceiver(myBroadcastReceiver)
        super.onDestroy()
    }
}