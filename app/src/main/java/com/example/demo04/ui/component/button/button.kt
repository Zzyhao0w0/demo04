@file:Suppress ("ktlint:standard:function-naming") package com.example.demo04.ui.component.button

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun Button(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enable: Boolean = true,
    contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    content: @Composable RowScope.() -> Unit,
) {
    Surface(
        onClick = onClick, // 点击事件
        modifier = modifier.semantics { role = Role.Button },
        enabled = enable,
        color = Color(0xFF1D6FE9),
        shape = RoundedCornerShape(size = 8.dp),
        interactionSource = interactionSource,
    ) {
        Row(
            Modifier
                .defaultMinSize(
                    minWidth = ButtonDefaults.MinWidth,
                    minHeight = ButtonDefaults.MinHeight,
                )
                .padding(contentPadding),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            content = content,
        )
    }
}

@Composable
fun HollowButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    enable: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
    content: @Composable RowScope.() -> Unit,
) {
    Surface(
        onClick = onClick, // 点击事件
        modifier = modifier.semantics { role = Role.Button },
        enabled = enable,
        color = Color.Transparent,
        shape = RoundedCornerShape(size = 8.dp),
        border = BorderStroke(1.dp, Color(0xFF1D6FE9)),
        interactionSource = interactionSource,
    ) {
        Row(
            Modifier
                .defaultMinSize(
                    minWidth = ButtonDefaults.MinWidth,
                    minHeight = ButtonDefaults.MinHeight,
                )
                .padding(contentPadding),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            content = content,
        )
    }
}

@Preview
@Composable
fun HollowButtonPrev() {
    HollowButton(
        onClick = {},
        modifier =
            Modifier
                .widthIn(min = 343.dp),
    ) {
        Text(
            text = "联系车主",
            style =
                TextStyle(
                    fontSize = 16.sp,
                    fontWeight = FontWeight(400),
                    color = Color(0xFF1D6FE9),
                    textAlign = TextAlign.Center,
                ),
        )
    }
}

@Preview
@Composable
fun ButtonPreview() {
    Button(
        onClick = {},
        modifier =
            Modifier
                .widthIn(min = 343.dp),
    ) {
        Text(
            text = "登录",
            style =
                TextStyle(
                    fontSize = 16.sp,
                    fontWeight = FontWeight(400),
                    color = Color(0xFFFFFFFF),
                    textAlign = TextAlign.Center,
                ),
        )
    }
}
