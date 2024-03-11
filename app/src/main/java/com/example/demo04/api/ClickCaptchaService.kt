package com.example.demo04.api

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ClickCaptchaService {
    @GET("clickCaptcha/generate")
    fun clickCaptchaGenerate(): Call<ClickCaptcha>

    @POST("clickCaptcha/verify")
    fun clickCaptchaVerify(
        @Body params: ClickCaptchaVerifyMsg,
    ): Call<ClickCaptchaKey>
}

data class ClickCaptcha(
    val key: String, // 唯一标识
    val image: String, // 验证码大图
    val thumbImage: String, // 验证码小图
)

data class ClickCaptchaVerifyMsg(
    val key: String, // 唯一标识
    val dots: Map<String, Dot>, // 点集合，map的key值是顺序，value是Dot的数据
)

data class Dot(
    val x: Long, // x轴
    val y: Long, // y轴
)

data class ClickCaptchaKey(
    val value: String,
)
