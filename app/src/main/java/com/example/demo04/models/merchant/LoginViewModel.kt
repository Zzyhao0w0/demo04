package com.example.demo04.models.merchant

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.Dp
import androidx.lifecycle.ViewModel
import com.example.demo04.api.ClickCaptcha
import com.example.demo04.api.ClickCaptchaKey
import com.example.demo04.api.ClickCaptchaService
import com.example.demo04.api.ClickCaptchaVerifyMsg
import com.example.demo04.api.Dot
import com.example.demo04.common.MUtil
import com.example.demo04.http.HUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel : ViewModel() {
    var clickCaptcha: ClickCaptcha? by mutableStateOf(null)
    var clickPosition: List<Pair<Float, Float>> by mutableStateOf(emptyList())

    var key: ClickCaptchaKey? by mutableStateOf(null)
    var keyTime by mutableStateOf(System.currentTimeMillis())
    var phoneCodeId by mutableStateOf(0)

    fun sendPhoneCode(phone: String) {
    }

    fun getClickCaptcha() {
        val result = HUtils.getRetrofit().create(ClickCaptchaService::class.java).clickCaptchaGenerate()
        result.enqueue(
            object : Callback<ClickCaptcha> {
                override fun onResponse(
                    call: Call<ClickCaptcha>,
                    response: Response<ClickCaptcha>,
                ) {
                    clickCaptcha = response.body()
                    Log.e("getClickCaptchaService", "resp : $clickCaptcha")
                    Log.e("getClickCaptchaService", "resp : ${response.code()}")
                }

                override fun onFailure(
                    call: Call<ClickCaptcha>,
                    t: Throwable,
                ) {
                    Log.e("getClickCaptchaService", "onFailure: ${t.message}")
                }
            },
        )
    }

    fun verifyCaptcha(dps: List<Pair<Dp, Dp>>) {
        val dots = emptyMap<String, Dot>().toMutableMap()
        dps.forEachIndexed { index, pair ->
            dots[index.toString()] =
                Dot(
                    x = pair.first.value.toLong(),
                    y = pair.second.value.toLong(),
                )
        }
        clickCaptcha?.let {
            val result =
                HUtils.getRetrofit().create(ClickCaptchaService::class.java).clickCaptchaVerify(
                    params =
                        ClickCaptchaVerifyMsg(
                            key = it.key,
                            dots = dots,
                        ),
                )
            Log.e("verifyCaptcha", "clickPosition: $dots")
            result.enqueue(
                object : Callback<ClickCaptchaKey> {
                    override fun onResponse(
                        call: Call<ClickCaptchaKey>,
                        response: Response<ClickCaptchaKey>,
                    ) {
                        Log.e("verifyCaptcha", "onResponse: ${response.raw().body()}")
                        when (response.code()) {
                            200 -> {
                                key = response.body()
                                keyTime = System.currentTimeMillis()
                                Log.e("verifyCaptcha", "mutil : ${MUtil.kv.getString("click_captcha_key","")}")
                            }
                        }
                    }

                    override fun onFailure(
                        call: Call<ClickCaptchaKey>,
                        t: Throwable,
                    ) {
                        Log.e("verifyCaptcha", "onFailure: ${t.message}")
                    }
                },
            )
        }
    }
}
