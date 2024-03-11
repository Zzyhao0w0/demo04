package com.example.demo04.api

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface PhoneCodeService {
    @POST("phone/code/send/cc/unlimited")
    fun sendCode(
        @Body req: CodeSendReq,
    ): Call<CodeSendRes>
}

data class CodeSendReq(
    val phone: String,
    val key: String,
)

data class CodeSendRes(
    val id: Int,
)
