package com.example.demo04.ui.component.itinerary

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.demo04.R
import com.example.demo04.common.convertTimestampToDateString
import com.example.demo04.models.ItineraryViewModel
import com.example.demo04.ui.component.button.HollowButton

@Composable
fun UsedCarItineraryList(
    topPadding: Dp,
    ivm: ItineraryViewModel = viewModel()
) {
    ivm.getItineraryList()
    LazyColumn(
        modifier = Modifier.padding(top = topPadding),
        contentPadding = PaddingValues(start = 8.dp, top = 10.dp,end = 8.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items(ivm.itineraryList.size) {
            ivm.itineraryList[it].let {item->
                ItineraryCard(
                    title = item.title,
                    status = item.status,
                    time = item.appointedTime,
                )
            }
        }
    }
}

@Composable
fun ItineraryCard (
    title: String = "店铺标题",
    price: Int = 10,
    status: Int = 2,
    time: Long = 0,
) {
    var statusText by remember { mutableStateOf("待同意") }
    statusText = when(status) {
        0 -> "待同意"
        1 -> "预约成功"
        2 -> "预约失败"
        else -> "错误"
    }
    Column (
        modifier = Modifier
            .border(1.dp, Color.Black)
            .padding(8.dp)
    ){
        Row {
            Box(
                modifier = Modifier.size(width = 80.dp, height = 60.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.img),
                    contentDescription = "",
                    modifier = Modifier.fillMaxSize()
                )
            }
            Column {
                Row (
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ){
                    Text(
                        text = title,
                        style = TextStyle(
                            color = Color(0xFFD9000000),
                            fontSize = 14.sp,
                            lineHeight = 19.2.sp, // 14sp + 5.2sp
                            fontWeight = FontWeight.Normal, // 对应400
                        ),
                        modifier = Modifier
                            .padding(start = 8.dp, top = 0.dp) // 设置边距
                            .width(203.dp) // 设置宽度
                    )
                    Text(
                        text = statusText,
                        style = TextStyle(
                            color = Color(0xFFFFF5894E),
                            fontSize = 12.sp,
                            lineHeight = 17.6.sp, // 12sp + 5.6sp
                            fontWeight = FontWeight.Normal, // 对应400
                        ),
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1,
                        modifier = Modifier
                            .padding(start = 8.dp, end = 8.dp) // 设置边距
                    )
                }
                Text(
                    text = "￥${price}分",
                    style = TextStyle(
                        color = Color(0xFFFFF55F4E),
                        fontSize = 12.sp,
                        lineHeight = 17.6.sp, // 12sp + 5.6sp
                        fontWeight = FontWeight.Normal, // 对应400
                    ),
                    modifier = Modifier
                        .padding(start = 8.dp, top = 12.dp) // 设置边距
                        .width(247.dp) // 设置宽度
                        .height(26.dp) // 设置高度
                )
            }
        }
        Divider(
            modifier = Modifier
                .padding(top = 10.dp, bottom = 10.dp)
        )
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "预约时间：${convertTimestampToDateString(time)}",
                modifier = Modifier
                    .height(24.dp)
                    .padding(top = 3.dp)
                    .sizeIn(minHeight = 24.dp, minWidth = 155.dp),
                color = Color(0xFFd9000000),
                fontSize = 14.sp,
                lineHeight = 21.2.sp, // 14sp + 7.2sp
                fontWeight = FontWeight.Normal,
                textAlign = TextAlign.Center,
            )
            HollowButton(onClick = {},
                modifier = Modifier
                    .sizeIn(minWidth = 88.dp, minHeight = 30.dp)) {
                Text(
                    text = "联系车主",
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontWeight = FontWeight(400),
                        color = Color(0xFF1D6FE9),
                        textAlign = TextAlign.Center
                    )
                )
            }
        }
    }

}

@Preview
@Composable
fun UsedCarItineraryListPrev() {
    UsedCarItineraryList(0.dp)
}
