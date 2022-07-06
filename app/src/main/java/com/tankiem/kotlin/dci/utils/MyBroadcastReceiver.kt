package com.tankiem.kotlin.dci.utils

import android.app.AlertDialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.navigation.Navigation
import com.tankiem.kotlin.dci.R

class MyBroadcastReceiver: BroadcastReceiver() {
    companion object {
        const val SESSION_EXPIRED = "com.example.fakeslink" + "SESSION_EXPIRED"
    }
    override fun onReceive(context: Context?, intent: Intent?) {
        println("hehehehehe " + intent?.action)
        when(intent?.action) {
            "android.net.conn.CONNECTIVITY_CHANGE" -> {
                val builder = AlertDialog.Builder(context)
                    .setTitle(R.string.warning)
                    .setMessage(R.string.no_internet_connection)
                    .setPositiveButton(R.string.try_again) { dialog, which ->
                        val con: ConnectivityManager? = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
                        if (android.os.Build.VERSION.SDK_INT > 23) {
                            val networkInfor = con?.activeNetwork
                            val activeNetwork = con?.getNetworkCapabilities(networkInfor)
                            val isConnected = when {
                                activeNetwork?.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ?: false -> true
                                activeNetwork?.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ?: false -> true
                                //for other device how are able to connect with Ethernet
                                activeNetwork?.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) ?: false -> true
                                //for check internet over Bluetooth
                                activeNetwork?.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH) ?: false -> true
                                else -> false
                            }
                            if (isConnected)
                                dialog.cancel()
                        } else {
                            val isConnected = con?.activeNetworkInfo?.isConnected ?: false
                            if (isConnected)
                                dialog.cancel()
                        }
                    }
                    .setIcon(android.R.drawable.ic_dialog_alert)

                if (android.os.Build.VERSION.SDK_INT > 23) {
                    val con: ConnectivityManager? = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
                    val networkInfor = con?.activeNetwork
                    val activeNetwork = con?.getNetworkCapabilities(networkInfor)
                    val isConnected = when {
                        activeNetwork?.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ?: false -> true
                        activeNetwork?.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ?: false -> true
                        //for other device how are able to connect with Ethernet
                        activeNetwork?.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) ?: false -> true
                        //for check internet over Bluetooth
                        activeNetwork?.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH) ?: false -> true
                        else -> false
                    }
                    if (!isConnected)
                        builder.show()
                } else {
                    val con: ConnectivityManager? = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
                    val isConnected = con?.activeNetworkInfo?.isConnected ?: false
                    if (!isConnected)
                        builder.show()
                }
            }
            SESSION_EXPIRED -> {
                val builder = AlertDialog.Builder(context)
                    .setTitle(R.string.session_expired)
                    .setMessage(R.string.session_expired_description)
                    .setPositiveButton(android.R.string.ok) { dialog, which ->
                        context?.let {
//                            it.startActivity(Intent(it, LoginActivity::class.java))
                        }
                    }
                    .setNegativeButton(android.R.string.cancel) { dialog, _ ->
                        dialog.cancel()
                    }
                    .setIcon(android.R.drawable.ic_dialog_alert)
                builder.show()
            }
        }
    }
}

class MyLocalBroadcastReceiver: BroadcastReceiver() {
    companion object {
        const val SESSION_EXPIRED = "com.example.fakeslink" + "SESSION_EXPIRED"
    }
    override fun onReceive(context: Context?, intent: Intent?) {
        when(intent?.action) {
            SESSION_EXPIRED -> {
                val builder = AlertDialog.Builder(context)
                    .setTitle(R.string.session_expired)
                    .setMessage(R.string.session_expired_description)
                    .setPositiveButton(android.R.string.ok) { dialog, which ->
                        context?.let {
//                            it.startActivity(Intent(it, LoginActivity::class.java))
                        }
                    }
                    .setNegativeButton(android.R.string.cancel) { dialog, _ ->
                        dialog.cancel()
                    }
                    .setIcon(android.R.drawable.ic_dialog_alert)
                builder.show()
            }
        }
    }

}