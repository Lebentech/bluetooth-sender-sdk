package com.lb.myapplicationtest.presentation.extensions

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.ComponentActivity
import androidx.core.content.ContextCompat
import com.lb.myapplicationtest.presentation.MainActivity

fun MainActivity.getRequiredBluetoothPermissions(): Array<String> {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        // Android 12+ (API 31)
        arrayOf(
            Manifest.permission.BLUETOOTH_CONNECT,
            Manifest.permission.BLUETOOTH_SCAN
        )
    } else {
        // Android 6.0 (API 23) hasta Android 11 (API 30)
        arrayOf(
            Manifest.permission.BLUETOOTH,
            Manifest.permission.BLUETOOTH_ADMIN,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
    }
}

fun MainActivity.checkAndRequestBluetoothPermissions(onBluetoothPermissionsGranted: () -> Unit) {
    val permissionsToRequest = getRequiredBluetoothPermissions()
        .filter { ContextCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED }
        .toTypedArray()

    if (permissionsToRequest.isNotEmpty()) {
        this.requestPermissions(permissionsToRequest, 1001)
    } else {
        onBluetoothPermissionsGranted()
    }
}