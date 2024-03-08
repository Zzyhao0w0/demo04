package com.example.demo04.ui.component.itinerary

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

@Composable
fun ItineraryListDialog(
    onDismissRequest: () -> Unit = {}
) {
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(
            usePlatformDefaultWidth = false // experimental
        ),
    ) {
        Scaffold {
            UsedCarItineraryList(it.calculateTopPadding())
        }
    }
}


