@file:Suppress("ktlint:standard:function-naming")

package com.example.demo04.ui.component.textField

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp

@Composable
fun TextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String = "",
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    trailing: @Composable (() -> Unit)? = null,
    trailClick: () -> Unit = {},
) {
    val rectangleColor = Color(0x0A000000)

    var isFocused by remember { mutableStateOf(false) }
    val isShow = (isFocused && value.isNotBlank()) || value.isNotBlank()

    val borderModifier =
        modifier.border(
            width = 1.dp,
            color = if (isFocused) Color(0xFF1D6FE9) else Color(0x00000000),
            shape = RoundedCornerShape(6.dp),
        )

    Box(
        modifier =
            borderModifier
                .background(
                    color = rectangleColor,
                    shape = RoundedCornerShape(6.dp),
                ),
        contentAlignment = Alignment.CenterStart,
    ) {
        Box(
            modifier =
                Modifier
                    .offset(x = 8.dp),
        ) {
            Box(
                modifier = Modifier.fillMaxWidth(),
            ) {
                if (!isShow) {
                    Text(
                        text = label,
                        color = Color(0xFF808080),
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
                BasicTextField(
                    value = value,
                    onValueChange = onValueChange,
                    visualTransformation = visualTransformation,
                    keyboardOptions = keyboardOptions,
                    modifier =
                        Modifier
                            .onFocusChanged { focusState ->
                                isFocused = focusState.isFocused
                            }
                            .fillMaxWidth(),
                )
            }
        }
        if (trailing != null) {
            Row(
                modifier =
                    Modifier
                        .align(Alignment.CenterEnd)
                        .pointerInput(Unit) {
                            detectTapGestures(
                                onTap = {
                                    trailClick()
                                },
                            )
                        }
                        .fillMaxHeight(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Spacer(modifier = Modifier.width(8.dp))
                trailing()
                Spacer(modifier = Modifier.width(8.dp))
            }
        }
    }
}
