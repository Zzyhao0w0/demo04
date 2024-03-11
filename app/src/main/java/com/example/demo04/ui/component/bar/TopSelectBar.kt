@file:Suppress ("ktlint:standard:function-naming")
package com.example.demo04.ui.component.bar

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun TopSelectBar(topPadding: Dp) {
    Row(
        horizontalArrangement = Arrangement.Center,
        modifier =
            Modifier
                .fillMaxWidth()
                .height(44.dp)
                .padding(top = topPadding)
                .border(1.dp, Color.Green, RectangleShape),
    ) {
        Text(text = "上门预约")
        Text(text = "到店预约")
    }
}
