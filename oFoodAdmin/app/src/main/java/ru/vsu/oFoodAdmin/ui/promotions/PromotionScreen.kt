package ru.vsu.oFoodAdmin.ui.promotions

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.runtime.Composable
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.channels.Channel
import ru.vsu.oFoodAdmin.domain.PromotionRepo
import ru.vsu.oFoodAdmin.domain.Result
import ru.vsu.oFoodAdmin.entities.Order
import ru.vsu.oFoodAdmin.ui.order.InfoField
import ru.vsu.oFoodAdmin.ui.order.OrderContent
import ru.vsu.oFoodAdmin.ui.order.OrderEvent
import ru.vsu.oFoodAdmin.ui.state.UiState
import ru.vsu.oFoodAdmin.ui.state.copyWithResult
import ru.vsu.oFoodAdmin.ui.utils.ErrorState
import ru.vsu.oFoodAdmin.ui.utils.Loading
import java.text.SimpleDateFormat

@Composable
fun PromotionScreen(
    promotionRepo: PromotionRepo,
    addingPromotion : () -> Unit
) {
    val eventChannel = remember { Channel<PromotionEvent>(Channel.CONFLATED) }
    val promotionState =
        produceState(UiState<List<PromotionState>>(loading = true), promotionRepo) {
            eventChannel.send(PromotionEvent.INIT)
            for (event in eventChannel) {
                when (event) {
                    is PromotionEvent.INIT -> {
                        val result = promotionRepo.getAllPromotions()
                        val format = SimpleDateFormat("dd.MM.yyyy")
                        value = when (result) {
                            is Result.Success -> value.copy(
                                loading = false,
                                exception = null,
                                data = result.data.map {
                                    PromotionState(
                                        id = it.id,
                                        name = it.name ?: "",
                                        startRep =  it.start?.let { start -> format.format(start) } ?: "" ,
                                        endRep = it.end?.let { end -> format.format(end) } ?: ""
                                    )
                                })
                            is Result.Error -> value.copy(
                                loading = false,
                                exception = value.exception
                            )
                        }
                    }
                }
            }
        }
    PromotionScreen(promotionState.value, addingPromotion)
}

@Composable
fun PromotionScreen(
    promotions: UiState<List<PromotionState>>,
    addingPromotion : () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Акции") },
                actions = {
                    IconButton(onClick = { addingPromotion() }) {
                        Icon(Icons.Filled.Add, contentDescription = null)
                    }
                }
            )
        },

    ) {
        if (promotions.loading) {
            Loading()
        } else if (promotions.hasError)
            ErrorState()
        else {
            PromotionContent(promotions.data!!)
        }
    }
}

@Composable
fun PromotionContent(
    promotions: List<PromotionState>
) {
    LazyColumn {
        promotions.forEach {
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
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Column(
                            modifier = Modifier
                                .padding(8.dp)
                                .weight(1f)
                        ) {
                            InfoField(label = "Имя: ", content = it.name)
                            InfoField(label = "Начало: ", it.startRep)
                            InfoField(label = "Конец:", it.endRep)
                        }
                        IconButton(
                            onClick = { /*TODO*/ },
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