package ru.vsu.ofood.ui.info

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.ButtonDefaults.buttonColors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.vsu.ofood.R
import ru.vsu.ofood.di.ComponentHolder
import ru.vsu.ofood.entities.Restaurant
import ru.vsu.ofood.ui.InfoDestination
import ru.vsu.ofood.ui.NavigationOperation
import ru.vsu.ofood.ui.theme.Black05


@Composable
fun InfoScreen(navigate: (NavigationOperation) -> Unit) {
    val middleware = ComponentHolder.infoComponent!!.getInfoMiddleware()
    val stateScreen = middleware.infoState.collectAsState()
    InfoScreenState(
        infoState = stateScreen.value,
        middleware = middleware,
        navigate
    )
}

@Composable
fun InfoScreenState(
    infoState: InfoState,
    middleware: InfoMiddleware,
    navigate: (NavigationOperation) -> Unit
) {
    Log.d("InfoScreenState", "$infoState")
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Контакты") },
            )
        }
    ) {
        when (infoState) {
            is InfoState.Empty -> {
            }
            is InfoState.Loading -> {
            }
            is InfoState.Error -> {
            }
            is InfoState.Content -> {
                InfoContent(
                    complete = middleware::complete,
                    restaurant = infoState.restaurant,
                ) {
                    navigate(
                        NavigationOperation.Destination(
                            InfoDestination.Localization(infoState.restaurant.coordinate)
                        )
                    )
                }
            }
        }
    }
}

@Composable
fun InfoContent(
    complete: (InfoAction) -> Unit,
    restaurant: Restaurant,
    clickOpenMap: () -> Unit
) {
    Box {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp),
            shape = RoundedCornerShape(bottomStart = 64.dp, bottomEnd = 64.dp),
            color = MaterialTheme.colors.primary
        ) {}
        Column(
            modifier = Modifier.padding(
                top = 16.dp,
                start = 16.dp,
                end = 16.dp,
                bottom = 8.dp
            )
        ) {
            Surface(
                color = Color.White,
                shape = RoundedCornerShape(16.dp),
                elevation = 4.dp
            ) {
                Column {
                    Row(
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Outlined.Phone, null, modifier = Modifier
                                .padding(end = 16.dp)
                                .size(14.dp), tint = Color(0xFF2FF3E0)
                        )
                        TextInfo(restaurant.phone, modifier = Modifier.weight(1f))
                        Button(
                            onClick = { complete(InfoAction.ClickPhoneBtn) },
                            contentPadding = PaddingValues(
                                start = 12.dp,
                                top = 6.dp,
                                end = 12.dp,
                                bottom = 6.dp
                            ),
                            modifier = Modifier
                                .widthIn(min = 1.dp)
                                .heightIn(min = 1.dp),
                            shape = RoundedCornerShape(8.dp),
                            colors = buttonColors(
                                backgroundColor = Color(0xFFEFF7FC),
                                contentColor = Color(0xFF1E96C8)
                            )
                        ) {
                            Text("Позвонить")
                        }
                    }
                    DividingLine()
                    Row(
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Filled.Email, null, modifier = Modifier
                                .padding(end = 16.dp)
                                .size(14.dp), tint = Color(0xFF2FF3E0)
                        )
                        TextInfo(restaurant.email)
                    }
                    DividingLine()
                    Row(
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_clock),
                            null,
                            modifier = Modifier
                                .padding(end = 16.dp)
                                .size(14.dp),
                            tint = Color(0xFF2FF3E0)
                        )
                        TextInfo("Режим работы: c 10:00 до 23:00")
                    }
                }
            }
            Surface(
                color = Color.White,
                shape = RoundedCornerShape(16.dp),
                elevation = 4.dp,
                modifier = Modifier.padding(top = 16.dp)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Outlined.LocationOn,
                        null,
                        modifier = Modifier
                            .padding(end = 16.dp)
                            .size(14.dp),
                        tint = Color(0xFF2FF3E0)
                    )
                    TextInfo(
                        restaurant.address,
                        Modifier.weight(1f),
                        2
                    )
                    Surface(
                        modifier = Modifier.size(24.dp),
                        shape = RoundedCornerShape(12.dp),
                        color = MaterialTheme.colors.primary.copy(alpha = 0.45f)
                    ) {
                        IconButton(onClick = { clickOpenMap() }) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_next),
                                contentDescription = null,
                                modifier = Modifier.padding(4.dp),
                                tint = MaterialTheme.colors.primary
                            )
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun TextInfo(
    textValue: String,
    modifier: Modifier = Modifier,
    maxLines: Int = 1
) {
    Text(
        textValue,
        modifier = modifier,
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp,
        maxLines = maxLines
    )
}

@Composable
fun DividingLine(modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .height(1.dp),
        color = Black05
    ) {}
}