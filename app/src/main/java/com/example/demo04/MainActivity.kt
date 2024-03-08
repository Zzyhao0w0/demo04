package com.example.demo04

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.demo04.common.MUtil
import com.example.demo04.models.LoginViewModel
import com.example.demo04.ui.component.button.Button
import com.example.demo04.ui.component.itinerary.ItineraryListDialog
import com.example.demo04.ui.component.login.LoginDialog
import com.tencent.mmkv.MMKV

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MMKV.initialize(this)
        setContent {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background
            ) {
                TestScreen()
            }
        }
    }
}

@Composable
fun TestScreen() {

    var showLogin by remember { mutableStateOf(false) }
    var showItinerary by remember { mutableStateOf(false) }
    val loginViewModel:LoginViewModel = viewModel()

    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        Column (
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Button(onClick = {
                showLogin = !showLogin
            }) {
                Text(text = "登录")
            }

            Button(onClick = { showItinerary = !showItinerary }) {
                Text(text = "二手车行程")
            }
            var shortToken by remember { mutableStateOf(MUtil.kv.getString("short_token","").toString()) }
            Button(onClick = {
                loginViewModel.getShortToken()
            }) {
                Text(text = "刷新token")
            }
            Text(text = "shortToken = ${shortToken}")

            if (showItinerary) {
                ItineraryListDialog()
            }


            if (showLogin) {
                LoginDialog(loginViewModel) {
                    showLogin = !showLogin
                }
            }
        }
    }
}