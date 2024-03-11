package com.example.demo04.api

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface MerchantAccountService {
    @POST("merchant/account/login")
    fun login(
        @Body params: LoginParam,
    ): Call<LoginResponse>

    @POST("merchant/account/short/token")
    fun shortToken(
        @Body params: ShortTokenParam,
    ): Call<ShortTokenResponse>
}

data class LoginParam(
    val phone: String,
    val password: String,
    val codeId: Long,
    val codeValue: Int,
)

data class LoginResponse(
    val token: String,
    val roles: List<String>,
)

data class ShortTokenParam(
    val token: String,
)

data class ShortTokenResponse(
    val token: String,
    val roles: List<String>,
)
