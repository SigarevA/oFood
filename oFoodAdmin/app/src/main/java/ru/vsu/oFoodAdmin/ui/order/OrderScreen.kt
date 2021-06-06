package ru.vsu.oFoodAdmin.ui.order

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.runtime.Composable
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.channels.Channel
import ru.vsu.oFoodAdmin.domain.OrderRepo
import ru.vsu.oFoodAdmin.entities.Order
import ru.vsu.oFoodAdmin.ui.state.UiState
import ru.vsu.oFoodAdmin.ui.state.copyWithResult
import ru.vsu.oFoodAdmin.ui.utils.ErrorState
import ru.vsu.oFoodAdmin.ui.utils.Loading

@Composable
fun OrderScreen(
    orderRepo: OrderRepo,
    openDetailOrder: (Order) -> Unit
) {
    val eventChannel = remember { Channel<OrderEvent>(Channel.CONFLATED) }
    val orderState = produceState(UiState<List<Order>>(loading = true), orderRepo) {
        eventChannel.send(OrderEvent.INIT)
        for (event in eventChannel) {
            when (event) {
                is OrderEvent.INIT -> {
                    value = value.copyWithResult(orderRepo.getNewOrder())
                }
            }
        }
    }
    OrderScreen(orderState = orderState.value, openDetailOrder)
}

@Composable
fun OrderScreen(
    orderState: UiState<List<Order>>,
    openDetailOrder: (Order) -> Unit
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
            OrderContent(orders = orderState.data!!, openDetailOrder)
        }
    }
}

@Composable
fun OrderContent(
    orders: List<Order>,
    openDetailOrder: (Order) -> Unit
) {
    LazyColumn {
        orders.forEach {
            item {
                Surface(
                    color = Color(0xFFEBEBEB),
                    shape = RoundedCornerShape(16.dp),
                    elevation = 4.dp,
                    modifier = Modifier
                        .padding(
                            horizontal = 16.dp,
                            vertical = 8.dp
                        )
                        .fillMaxWidth()
                )
                {
                    Row(verticalAlignment = CenterVertically) {
                        Column(
                            modifier = Modifier
                                .padding(8.dp)
                                .weight(1f)
                        ) {
                            InfoField(label = "Имя: ", content = it.name)
                            InfoField(label = "Телефон: ", it.phone)
                            InfoField(label = "Город:", it.city)
                        }
                        IconButton(
                            onClick = { openDetailOrder(it) },
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Icon(Icons.Filled.ArrowForward, null)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun InfoField(label: String, content: String) {
    Text(
        buildAnnotatedString {
            append(label)
            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                append(content)
            }
        },
        maxLines = 2,
        overflow = TextOverflow.Ellipsis,
        modifier = Modifier.padding(horizontal = 4.dp)
    )
}