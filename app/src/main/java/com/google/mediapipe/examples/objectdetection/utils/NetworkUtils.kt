package com.google.mediapipe.examples.objectdetection.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities

/**
 * Helper utilities for retrieving network information (IP address, Transport type).
 */
object NetworkUtils {

    // Returns the IPv4 of the currently active transport (Wi-Fi/cellular), if available
    fun getPhoneIpv4(context: Context): String? {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = cm.activeNetwork ?: return null
        val caps = cm.getNetworkCapabilities(network) ?: return null

        val lp = cm.getLinkProperties(network) ?: return null
        val ipv4 = lp.linkAddresses
            .map { it.address }
            .firstOrNull { it.hostAddress?.contains('.') == true } // quick IPv4 check

        return ipv4?.hostAddress
    }

    fun getActiveTransport(context: Context): String {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = cm.activeNetwork ?: return "NONE"
        val caps = cm.getNetworkCapabilities(network) ?: return "NONE"
        return when {
            caps.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> "WIFI"
            caps.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> "CELLULAR"
            caps.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> "ETHERNET"
            else -> "OTHER"
        }
    }
}
