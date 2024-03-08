package com.example.demo04.models

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.Dp
import androidx.lifecycle.ViewModel
import com.example.demo04.common.MUtil
import com.example.demo04.http.HUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

class LoginViewModel : ViewModel(){

    var clickCaptcha:ClickCaptcha? by mutableStateOf(null)

    var clickPosition:List<Pair<Float,Float>> by mutableStateOf(emptyList())

    var key : ClickCaptchaKey? by mutableStateOf(null)
    var keyTime by mutableLongStateOf(System.currentTimeMillis())

    fun getClickCaptcha() {
        val result =  HUtils.getRetrofit().create(ClickCaptchaService::class.java).clickCaptchaGenerate()
        result.enqueue(object : Callback<ClickCaptcha>{
            override fun onResponse(call: Call<ClickCaptcha>, response: Response<ClickCaptcha>) {
                clickCaptcha = response.body()
                Log.e("getClickCaptchaService", "resp : $clickCaptcha", )
            }

            override fun onFailure(call: Call<ClickCaptcha>, t: Throwable) {
                Log.e("getClickCaptchaService", "onFailure: ${t.message}", )
            }
        })
    }

    fun verifyCaptcha (dps:List<Pair<Dp,Dp>>) {
        val dots = emptyMap<String,Dot>().toMutableMap()
        dps.forEachIndexed { index, pair ->
            dots[index.toString()] = Dot(
                x = pair.first.value.toLong(),
                y = pair.second.value.toLong(),
            )
        }
        clickCaptcha?.let {
            val result = HUtils.getRetrofit().create(ClickCaptchaService::class.java).clickCaptchaVerify(params = ClickCaptchaVerifyMsg(
                key = it.key,
                dots = dots
            ))
            Log.e("verifyCaptcha", "clickPosition: ${dots}")
            result.enqueue(object :Callback<ClickCaptchaKey> {
                override fun onResponse(
                    call: Call<ClickCaptchaKey>,
                    response: Response<ClickCaptchaKey>
                ) {
                    Log.e("verifyCaptcha", "onResponse: ${response.raw().body()}")
                    when (response.code()){
                        200 -> {
                            key = response.body()
                            keyTime = System.currentTimeMillis()
                            Log.e("verifyCaptcha", "mutil : ${MUtil.kv.getString("click_captcha_key","")}")
                        }
                    }
                }

                override fun onFailure(call: Call<ClickCaptchaKey>, t: Throwable) {
                    Log.e("verifyCaptcha", "onFailure: ${t.message}")
                }

            })
        }
    }

    fun loginByPassword(u:String,p:String) {
        key?.let {
            val result = HUtils.getRetrofit().create(UserService::class.java).loginWithPassword(LoginWithPasswordReq(
                key = it.value,
                phone = u,
                password = p,
            ))
            result.enqueue(object :Callback<String>{
                override fun onResponse(call: Call<String>, response: Response<String>) {
                    Log.e("loginByPassword", "onResponse: ${response.body()}")
                    when (response.code()){
                        200 -> {
                            MUtil.kv.encode("long_token",response.body())
                        }
                    }
                }

                override fun onFailure(call: Call<String>, t: Throwable) {
                    TODO("Not yet implemented")
                }
            })
        }
    }

    fun getShortToken() {
        val longToken = MUtil.kv.getString("long_token","")
        if (longToken != "") {
            val result = HUtils.getRetrofit().create(UserService::class.java).shortTokenCreate(ShortTokenCreateReq(
                token = longToken.toString()
            ))
            result.enqueue(object :Callback<ShortTokenCreateRes>{
                override fun onResponse(
                    call: Call<ShortTokenCreateRes>,
                    response: Response<ShortTokenCreateRes>
                ) {
                    when (response.code()) {
                        200 -> {
                            MUtil.kv.putString("short_token", response.body()?.token)
                        }
                    }
                }

                override fun onFailure(call: Call<ShortTokenCreateRes>, t: Throwable) {
                    TODO("Not yet implemented")
                }
            })
        }
    }

}

interface UserService {
    @POST("user/login/v1/password")
    fun loginWithPassword(@Body req: LoginWithPasswordReq): Call<String>

    @POST("user/short/token")
    fun shortTokenCreate(@Body req: ShortTokenCreateReq): Call<ShortTokenCreateRes>
}

data class ShortTokenCreateReq (
    val token :String
)

data class ShortTokenCreateRes(
    val roles: List<String>,
    val token: String
)

data class LoginWithPasswordReq (
    val key      :String ,
    val phone    :String ,
    val password :String ,
)

interface ClickCaptchaService {
    @GET("clickCaptcha/generate")
    fun clickCaptchaGenerate(): Call<ClickCaptcha>

    @POST("clickCaptcha/verify")
    fun clickCaptchaVerify( @Body params : ClickCaptchaVerifyMsg): Call<ClickCaptchaKey>
}


data class ClickCaptcha(
    val key: String,        // 唯一标识
    val image: String,      // 验证码大图
    val thumbImage: String  // 验证码小图
)

data class ClickCaptchaVerifyMsg(
    val key: String,        // 唯一标识
    val dots: Map<String, Dot> // 点集合，map的key值是顺序，value是Dot的数据
)

data class Dot(
    val x: Long, // x轴
    val y: Long  // y轴
)

data class ClickCaptchaKey (
    val value: String
)
