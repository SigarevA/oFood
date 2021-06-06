package ru.vsu.oFoodAdmin.ui.detailorder

import android.util.Log
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.channels.Channel
import ru.vsu.oFoodAdmin.domain.OrderRepo
import ru.vsu.oFoodAdmin.domain.Result
import ru.vsu.oFoodAdmin.domain.succeeded
import ru.vsu.oFoodAdmin.network.response.DetailOrderResponse
import ru.vsu.oFoodAdmin.ui.order.InfoField
import ru.vsu.oFoodAdmin.ui.state.UiState
import ru.vsu.oFoodAdmin.ui.state.copyWithResult
import ru.vsu.oFoodAdmin.ui.utils.ErrorState
import ru.vsu.oFoodAdmin.ui.utils.Loading

@Composable
fun DetailOrderScreen(
    orderId: String?,
    orderRepo: OrderRepo,
    upPress: () -> Unit
) {
    val eventChannel = remember { Channel<DetailOrderEvent>(Channel.CONFLATED) }
    val order = produceState(UiState<DetailOrderResponse>(loading = true), orderRepo) {
        eventChannel.send(DetailOrderEvent.INIT)
        for (event in eventChannel) {
            when (event) {
                is DetailOrderEvent.INIT -> {
                    val result = orderRepo.getOrderById(orderId!!)
                    value = value.copyWithResult(result)
                    Log.d("DetailScreen", "${value}")
                }
                is DetailOrderEvent.ACCEPTED_ORDER -> {
                    val result = orderRepo.acceptOrder(value.data!!.id)
                    if (result.succeeded) {
                        if ((result as Result.Success).data.status == "success")
                            upPress()
                    }
                }
                is DetailOrderEvent.REJECTED_ORDER -> {
                    val result = orderRepo.rejectedOrder(value.data!!.id)
                    if (result.succeeded) {
                        if ((result as Result.Success).data.status == "success")
                            upPress()
                    }
                }
            }
        }
    }
    DetailOrderScreen(orderState = order.value,
        { eventChannel.offer(DetailOrderEvent.ACCEPTED_ORDER) },
        { eventChannel.offer(DetailOrderEvent.REJECTED_ORDER) }
    )
}

@Composable
fun DetailOrderScreen(
    orderState: UiState<DetailOrderResponse>,
    accept: () -> Unit,
    reject: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Заказы") })
        }
    ) {
        if (orderState.loading) {
            Loading()
        } else if (orderState.hasError)
            ErrorState()
        else {
            DetailOrderContent(orderState.data!!, accept, reject)
        }
    }
}

@Composable
fun DetailOrderContent(
    order: DetailOrderResponse, accept: () -> Unit,
    reject: () -> Unit
) {
    LazyColumn(contentPadding = PaddingValues(8.dp)) {
        item {
            Surface(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                color = Color(0xFFEBEBEB)
            ) {
                Column(
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth()
                ) {
                    InfoField("Имя: ", order.name)
                    InfoField("Телефон: ", order.phone)
                    InfoField("Адрес: ", order.address)
                    if (order.surrender != 0.0) {
                        InfoField("Сдача", content = order.surrender.toString())
                    }
                }
            }
            if (order.date != null) {
                Surface(
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    color = Color(0xFFEBEBEB)
                ) {
                    Column(
                        modifier = Modifier
                            .padding(8.dp)
                            .fillMaxWidth()
                    ) {
                        InfoField("Предзаказ: ", "order.date")
                    }
                }
            }
        }
        item {
            RowTable(name = "Название", price = "Цена", count = "Кол")
        }
        order.goods.forEach {
            item {
                RowTable(name = it.name, price = it.price.toString(), count = it.count.toString())
            }
        }
        item {
            Row {
                Button(
                    onClick = { accept() },
                    modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .weight(0.5f)
                ) {
                    Text("Принять")
                }
                Button(
                    onClick = { reject() },
                    modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .weight(0.5f)
                ) {
                    Text("Отклонить")
                }
            }
        }
    }
}

@Composable
fun RowTable(
    name: String,
    price: String,
    count: String
) {
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            name,
            modifier = Modifier
                .weight(0.7f)
                .border(1.dp, Color.Black)
                .padding(4.dp)
                .fillMaxHeight()
        )
        Text(
            price,
            modifier = Modifier
                .weight(0.2f)
                .border(1.dp, Color.Black)
                .padding(4.dp)
                .fillMaxHeight()
        )
        Text(
            count,
            modifier = Modifier
                .weight(0.1f)
                .border(1.dp, Color.Black)
                .padding(4.dp)
                .fillMaxHeight()
        )
    }
}