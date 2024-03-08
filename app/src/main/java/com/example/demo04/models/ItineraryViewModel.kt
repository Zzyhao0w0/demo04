package com.example.demo04.models

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.demo04.http.HUtils
import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

class ItineraryViewModel: ViewModel() {

    var itineraryList: List<ItineraryItem> by mutableStateOf(emptyList())

    fun getItineraryList() {
        val result = HUtils.getRetrofit().create(ItineraryService::class.java).itineraryList(ItineraryListReq(
            page = 1L,
            size = 10L,
        ))
        result.enqueue(object :Callback<List<ItineraryItem>> {
            override fun onResponse(
                call: Call<List<ItineraryItem>>,
                response: Response<List<ItineraryItem>>
            ) {
                when (response.code() ){
                    200 -> {
                        response.body()?.let {
                            itineraryList = it
                        }
                        Log.e("getItineraryList", "onResponse: ${itineraryList}", )
                    }
                    400 -> {
                        Log.e("getItineraryList", "onResponse: 400", )
                    }
                    404 -> {
                        Log.e("getItineraryList", "onResponse: 404", )
                    }
                    494 -> {
                        Log.e("getItineraryList", "onResponse: 494", )
                    }
                }
            }

            override fun onFailure(call: Call<List<ItineraryItem>>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })
    }
}

interface ItineraryService {
    @POST("itinerary/list")
    fun itineraryList(@Body params:ItineraryListReq) : Call<List<ItineraryItem>>
}

data class ItineraryListReq(
    @SerializedName("page") val page: Long,
    @SerializedName("size") val size: Long
)

data class ItineraryItem(
    @SerializedName("id") val id: Long,
    @SerializedName("typeId") val typeId: Long,
    @SerializedName("typeName") val typeName: String,
    @SerializedName("toType") val toType: Int,
    @SerializedName("belongType") val belongType: Int,
    @SerializedName("belongId") val belongId: String,
    @SerializedName("image") val image: String,
    @SerializedName("title") val title: String,
    @SerializedName("appointedTime") val appointedTime: Long,
    @SerializedName("lonLat") val lonLat: List<Double>,
    @SerializedName("address") val address: String,
    @SerializedName("status") val status: Int,
    @SerializedName("proposer") val proposer: Long,
    @SerializedName("confirmPerson") val confirmPerson: Long,
    @SerializedName("isProposer") val isProposer: Boolean
)