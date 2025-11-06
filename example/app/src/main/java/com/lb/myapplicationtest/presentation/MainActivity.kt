package com.lb.myapplicationtest.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.lb.myapplicationtest.presentation.extensions.checkAndRequestBluetoothPermissions
import com.lb.myapplicationtest.presentation.view.ConnectionScreen
import com.lb.myapplicationtest.presentation.view.HomeScreen
import com.lb.myapplicationtest.ui.theme.MyApplicationTEstTheme
import com.lb.sendersdk.BluetoothService

class MainActivity : ComponentActivity() {

    private val bluetoothService: BluetoothService by lazy {
        BluetoothService(this)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        enableEdgeToEdge()

        setContent {

            val state by bluetoothService.state.collectAsState()

            this.checkAndRequestBluetoothPermissions {
                bluetoothService.startScan()
            }

            MyApplicationTEstTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    if (state.isConnected) {
                        ConnectionScreen(
                            modifier = Modifier.padding(innerPadding),
                            bluetoothService = bluetoothService
                        )
                    } else {
                        HomeScreen(
                            modifier = Modifier.padding(innerPadding),
                            state = state,
                            bluetoothService = bluetoothService
                        )
                    }
                }
            }
        }
    }
}