package com.example.demo04.ui.component.itinerary

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.navigation.NavController

@Composable
fun OrderItineraryList(
    topPadding: Dp,
    navController: NavController
) {
    Row (
        modifier = Modifier.padding(top = topPadding)
    ){
        Text(text = "order页面")
    }
}