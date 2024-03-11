@file:Suppress("ktlint:standard:function-naming")

package com.example.demo04.ui.component.login

import android.util.Log
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.demo04.common.MUtil
import com.example.demo04.models.LoginViewModel
import com.example.demo04.ui.component.button.Button
import com.example.demo04.ui.component.textField.TextField
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import kotlin.math.pow
import kotlin.math.sqrt

@Composable
fun LoginDialog(
    lvw: LoginViewModel = viewModel(),
    onDismissRequest: () -> Unit = {},
) {
    var showVerifier by remember { mutableStateOf(false) }

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
        ) { it ->
            if (showVerifier) {
                val current = LocalDensity.current
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
                            showVerifier = !showVerifier
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
                                showVerifier = false
                            } else {
                                refresh()
                            }
                        },
                    )
                }
            }
            LoginWithPassword(topPadding = it.calculateTopPadding()) { u, p ->
                if (lvw.key == null || (System.currentTimeMillis() - lvw.keyTime) / 6000 > 1) {
                    lvw.getClickCaptcha()
                    showVerifier = true
                } else {
                    lvw.loginByPassword(u, p)
                    if (MUtil.kv.decodeString("long_token") != "") {
                        onDismissRequest()
                        Log.e("loginByPassword", "LoginDialog: ${MUtil.kv.decodeString("long_token")}")
                    }
                }
            }
        }
    }
}

@Composable
@Preview
fun LoginDialogPreview() {
    LoginDialog()
}

@Composable
fun LoginWithPassword(
    topPadding: Dp,
    loginOnclick: (String, String) -> Unit,
) {
    var user by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    var read by remember { mutableStateOf(false) }

    Surface(
        modifier =
            Modifier
                .padding(top = topPadding)
                .fillMaxSize(),
    ) {
        Column(
            modifier =
                Modifier
                    .background(Color.White)
                    .padding(start = 24.dp),
        ) {
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "手机号密码登录",
                modifier =
                    Modifier
                        .fillMaxWidth(),
                lineHeight = 24.sp,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
            )
            Spacer(modifier = Modifier.height(12.dp))
            TextField(
                value = user,
                onValueChange = { user = it },
                label = "请输入手机号",
                modifier =
                    Modifier
                        .requiredHeight(44.dp)
                        .requiredWidth(343.dp),
            )
            Spacer(modifier = Modifier.height(12.dp))
            TextField(
                value = password,
                onValueChange = { password = it },
                label = "请输入密码",
                modifier =
                    Modifier
                        .requiredHeight(44.dp)
                        .requiredWidth(343.dp),
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "手机号或密码错误，请重试",
                color = Color.Red,
            )
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                horizontalArrangement = Arrangement.Absolute.Center,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                RadioButton(
                    selected = read,
                    onClick = { read = !read },
                    modifier =
                        Modifier
                            .size(30.dp)
                            .animateContentSize { initialValue, targetValue ->
                            },
                )
                Text(
                    text = "已阅读并同意《用户协议》和《隐私政策》",
                    modifier = Modifier,
                )
            }
            Spacer(modifier = Modifier.height(14.dp))
            Button(
                onClick = { loginOnclick(user, password) },
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
    }
}

@Suppress("ktlint:standard:function-naming")
@Composable
fun LoginWithPhoneCode(
    topPadding: Dp,
    trailClick: () -> Unit = {},
) {
    var phone by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var code by remember { mutableStateOf("") }

    var countdown by remember { mutableIntStateOf(-99) }

    var read by remember { mutableStateOf(false) }
    Surface(
        modifier =
            Modifier
                .padding(top = topPadding)
                .fillMaxSize(),
    ) {
        Column(
            modifier =
                Modifier
                    .background(Color.White),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Column(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(top = 68.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                // 聚车圈商家端
                Text(
                    text = "聚车圈商家端",
                    color = Color(0xFF1D6FE9),
                    fontSize = 24.sp,
                    lineHeight = 32.sp, // 替换 lineSpacingExtra
                    fontWeight = FontWeight.Medium, // 替换 textFontWeight
                    textAlign = TextAlign.Center, // 替换 android:gravity="undefined"
                    modifier =
                        Modifier
                            .width(144.dp)
                            .height(32.dp),
                )

                Spacer(modifier = Modifier.height(3.dp)) // 添加间隔

                // 汇天下精品，融八方商机
                Text(
                    text = "汇天下精品，融八方商机",
                    color = Color(0xFF1D6FE9),
                    fontSize = 10.sp,
                    lineHeight = 18.sp, // 替换 lineSpacingExtra
                    fontWeight = FontWeight.Normal, // 替换 textFontWeight
                    textAlign = TextAlign.Center, // 替换 android:gravity="undefined"
                    modifier =
                        Modifier
                            .width(140.dp)
                            .height(18.dp),
                )
            }
            Spacer(modifier = Modifier.height(37.dp))
            TextField(
                value = phone,
                onValueChange = { phone = it },
                label = "请输入手机号",
                modifier =
                    Modifier
                        .requiredHeight(44.dp)
                        .requiredWidth(343.dp),
            )
            Spacer(modifier = Modifier.height(12.dp))
            TextField(
                value = password,
                onValueChange = { password = it },
                label = "请输入密码",
                modifier =
                    Modifier
                        .requiredHeight(44.dp)
                        .requiredWidth(343.dp),
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            )
            Spacer(modifier = Modifier.height(12.dp))
            TextField(
                value = code,
                onValueChange = { code = it },
                label = "请输入验证码",
                modifier =
                    Modifier
                        .requiredHeight(44.dp)
                        .requiredWidth(343.dp),
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                trailing = {
                    if (countdown == -99) {
                        Text(
                            text = "获取验证码",
                            color = Color.Red,
                        )
                    } else {
                        Text(
                            text = "$countdown",
                            color = Color.Red,
                        )
                    }
                },
                trailClick = {
                    if (countdown == -99) {
                        CoroutineScope(Dispatchers.Default).launch {
                            countdown = 10
                            while (countdown > 0) {
                                delay(TimeUnit.SECONDS.toMillis(1))
                                countdown -= 1
                            }
                            countdown = -99
                        }
                    }
                    trailClick()
                },
            )
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                horizontalArrangement = Arrangement.Absolute.Center,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                RadioButton(
                    selected = false,
                    onClick = { },
                    modifier =
                        Modifier
                            .size(30.dp)
                            .animateContentSize { initialValue, targetValue ->
                            },
                )
                Text(
                    text = "已阅读并同意《用户协议》和《隐私政策》",
                    modifier = Modifier,
                )
            }
            Spacer(modifier = Modifier.height(14.dp))
            Button(
                onClick = { },
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
    }
}

@Preview
@Composable
fun LoginWithPhoneCodePreview() {
    LoginWithPhoneCode(1.dp)
}
