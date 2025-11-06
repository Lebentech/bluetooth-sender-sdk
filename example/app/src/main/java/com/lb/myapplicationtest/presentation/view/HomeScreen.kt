package com.lb.myapplicationtest.presentation.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lb.myapplicationtest.R
import com.lb.sendersdk.BluetoothService
import com.lb.sendersdk.models.BluetoothUiState

@Composable
fun HomeScreen(modifier: Modifier = Modifier, state: BluetoothUiState, bluetoothService: BluetoothService) {

    LazyColumn (
        modifier = modifier
    ) {
        item {
            Text(
                text = "Dispositivos vinculados.",
                fontWeight = FontWeight.SemiBold,
                fontSize = 18.sp,
                modifier = Modifier.padding(10.dp)
            )
        }

        items(state.pairedDevices) {
            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(shape = RoundedCornerShape(10.dp))
                    .clickable {
                        bluetoothService.connectToDevice(it)
                    },
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    it.name ?: it.address,
                    modifier = Modifier.padding(16.dp)
                )
                Icon(
                    painter = painterResource(R.drawable.ic_go_arrow),
                    contentDescription = "Connect to device",
                    modifier = Modifier.padding(16.dp)
                )
            }
        }

        item {
            Text(
                text = "Dispositivos encontrados.",
                fontWeight = FontWeight.SemiBold,
                fontSize = 18.sp,
                modifier = Modifier.padding(10.dp)
            )
        }

        items(state.scannedDevices) {
            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(shape = RoundedCornerShape(10.dp))
                    .clickable {
                        bluetoothService.connectToDevice(it)
                    },
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    it.name ?: it.address,
                    modifier = Modifier.padding(16.dp)
                )
                Icon(
                    painter = painterResource(R.drawable.ic_go_arrow),
                    contentDescription = "Connect to device",
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    }

}