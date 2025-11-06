package com.lb.myapplicationtest.presentation.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.lb.sendersdk.BluetoothService
import java.util.UUID

@Composable
fun ConnectionScreen(modifier: Modifier = Modifier, bluetoothService: BluetoothService) {

    var currentId by remember { mutableStateOf("") }
    var isFirstCall by remember { mutableStateOf(true) }

    Column (
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = {
                currentId = UUID.randomUUID().toString()
                isFirstCall = false
                bluetoothService.sendMessage("1", currentId)
            },
            enabled = isFirstCall
        ) {
            Text("Iniciar camara")
        }

        Button(
            onClick = {
                isFirstCall = true
                bluetoothService.sendMessage("2", currentId)
            },
            enabled = !isFirstCall
        ) {
            Text("Finalizar camara")
        }

        Button(
            onClick = {
                bluetoothService.disconnectFromDevice()
            }
        ) {
            Text("Salir")
        }
    }
}