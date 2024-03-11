@file:Suppress("ktlint:standard:function-naming")

package com.example.demo04.ui.component.login.merchant

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.demo04.models.merchant.LoginViewModel
import com.example.demo04.ui.component.login.ClickCaptchaDialog
import com.example.demo04.ui.component.login.LoginWithPhoneCode
import com.example.demo04.ui.component.login.TopBar
import kotlin.math.pow
import kotlin.math.sqrt

@Composable
fun LoginDialog(
    lvw: LoginViewModel = viewModel(),
    onDismissRequest: () -> Unit = {},
) {
    var showClickCaptcha by remember { mutableStateOf(false) }

    val refresh: () -> Unit = {
        lvw.getClickCaptcha()
        lvw.clickPosition = emptyList()
    }

    Dialog(
        onDismissRequest = onDismissRequest,
        properties =
            DialogProperties(
                usePlatformDefaultWidth = false, // experimental
            ),
    ) {
        Scaffold(
            topBar = { TopBar(title = "", onNavigationIconClick = {}) },
        ) {
            val current = LocalDensity.current
            if (showClickCaptcha) {
                lvw.clickCaptcha?.let { it1 ->
                    ClickCaptchaDialog(
                        image = it1.image,
                        thumbImage = it1.thumbImage,
                        clickPosition = lvw.clickPosition,
                        clickPositionAction = { offset ->
                            // 检查点击位置是否在圆点的范围内
                            val clickedCircleIndex =
                                lvw.clickPosition.indexOfFirst { (x, y) ->
                                    val distance = sqrt((x - offset.x).pow(2) + (y - offset.y).pow(2))
                                    distance <= 35f
                                }

                            if (lvw.clickPosition.size <= 3 || clickedCircleIndex != -1) {
                                // 如果在圆点的范围内,删除该圆点,否则添加该点
                                lvw.clickPosition =
                                    if (clickedCircleIndex != -1) {
                                        lvw.clickPosition
                                            .toMutableList()
                                            .also { it.removeAt(clickedCircleIndex) }
                                    } else {
                                        // 获取用户点击的位置
                                        lvw.clickPosition + Pair(offset.x, offset.y)
                                    }
                            }
                        },
                        refreshOnClick = refresh,
                        onDismissRequest = {
                            showClickCaptcha = !showClickCaptcha
                            lvw.clickPosition = emptyList()
                        },
                        verifyOnClick = {
                            val dps = emptyList<Pair<Dp, Dp>>().toMutableList()
                            it.forEach {
                                val dp = Pair(first = with(current) { it.first.toDp() }, second = with(current) { it.second.toDp() })
                                dps += dp
                            }
                            lvw.verifyCaptcha(dps)
                            if (lvw.key != null && (System.currentTimeMillis() - lvw.keyTime) / 6000 < 1) {
                                lvw.getClickCaptcha()
                                showClickCaptcha = false
                            } else {
                                refresh()
                            }
                        },
                    )
                }
            }
            LoginWithPhoneCode(topPadding = it.calculateTopPadding(), trailClick = {
                showClickCaptcha = !showClickCaptcha
                lvw.getClickCaptcha()
            })
        }
    }
}

@Preview
@Composable
fun LoginDialogPrev() {
    LoginDialog()
}
