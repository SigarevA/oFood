package ru.vsu.ofood.ui.order_registration

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.channels.Channel
import ru.vsu.ofood.di.ComponentHolder
import ru.vsu.ofood.ui.DatePicker
import ru.vsu.ofood.ui.NavigationOperation
import java.text.SimpleDateFormat
import java.util.*

data class OrderRegistrationAction(
    val changeTextPhone: (String) -> Unit,
    val changeTextName: (String) -> Unit,
    val changeTextCity: (String) -> Unit,
    val changeTextStreet: (String) -> Unit,
    val changeTextHouse: (String) -> Unit,
    val changeTextEntrance: (String) -> Unit,
    val changeTextFlat: (String) -> Unit,
    val changeTextComment: (String) -> Unit,
    val checkPreOrder: () -> Unit,
    val openDialog: () -> Unit,
    val changePreOrderDate: (PreOrderDate) -> Unit
)

@Composable
fun OrderRegistrationScreen(basketNavigate: (NavigationOperation) -> Unit) {
    val orderRegistrationState = rememberSaveable { mutableStateOf(OrderRegistrationState()) }
    val eventsChannel = remember { Channel<OrderRegistrationEvent>(Channel.CONFLATED) }
    val orderRegistrationMiddleware =
        ComponentHolder.basketTabComponent!!.getOrderRegistrationMiddleware()
    val orderRegistration =
        produceState<OrderRegistration>(OrderRegistration.Empty, orderRegistrationMiddleware) {
            for (event in eventsChannel) {
                when (event) {
                    is OrderRegistrationEvent.ClickCreateOrder -> {
                        if (orderRegistrationState.checkFields()) {
                            value = OrderRegistration.PlaceOrder
                            value = orderRegistrationMiddleware.processCreateOrder(event)
                        }
                    }
                }
            }
        }
    OrderRegistrationContent(
        orderRegistrationState = orderRegistrationState.value,
        orderRegistrationAction = orderRegistrationState.getOrderRegistrationTextChange(),
        basketNavigate,
        {
            eventsChannel.offer(OrderRegistrationEvent.ClickCreateOrder(orderRegistrationState.value))
        },
        orderRegistration.value
    )
}

@Composable
fun OrderRegistrationContent(
    orderRegistrationState: OrderRegistrationState,
    orderRegistrationAction: OrderRegistrationAction,
    basketNavigate: (NavigationOperation) -> Unit,
    placeOrder: () -> Unit,
    orderRegistration: OrderRegistration,
    scaffoldState: ScaffoldState = rememberScaffoldState()
) {
    Log.d("OrderContent", "$orderRegistrationState")
    val format = remember { SimpleDateFormat("dd.MM.yyyy", Locale("ru")) }

    if (orderRegistration is OrderRegistration.Error) {
        ProcessError(orderRegistration.msg, scaffoldState)
    }

    if (orderRegistration is OrderRegistration.Success) {
        LaunchedEffect(scaffoldState.snackbarHostState) {
            scaffoldState.snackbarHostState.showSnackbar(
                message = "Заказ отправлен!",
            )
        }
    }

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(
                title = { Text("Оформление") },
                navigationIcon = {
                    IconButton(onClick = { basketNavigate(NavigationOperation.Back) }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = null)
                    }
                }
            )
        }
    ) {
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
        ) {
            InputItem(
                textFieldValue = orderRegistrationState.phone.text,
                label = "Телефон",
                onTextChanged = { changeText ->
                    orderRegistrationAction.changeTextPhone(
                        changeText
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                keyboardType = KeyboardType.Phone,
                error = orderRegistrationState.phone.invalid
            )
            InputItem(
                textFieldValue = orderRegistrationState.name.text,
                label = "Имя",
                onTextChanged = { changeText ->
                    orderRegistrationAction.changeTextName(
                        changeText
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                error = orderRegistrationState.name.invalid
            )
            InputItem(
                textFieldValue = orderRegistrationState.city.text,
                label = "Город",
                onTextChanged = { changeText ->
                    orderRegistrationAction.changeTextCity(
                        changeText
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                error = orderRegistrationState.city.invalid
            )
            Row {
                InputItem(
                    textFieldValue = orderRegistrationState.street.text,
                    label = "Улица",
                    onTextChanged = { changeText ->
                        orderRegistrationAction.changeTextStreet(
                            changeText
                        )
                    },
                    modifier = Modifier
                        .weight(0.5f)
                        .padding(8.dp),
                    error = orderRegistrationState.street.invalid
                )
                InputItem(
                    textFieldValue = orderRegistrationState.house.text,
                    label = "Дом",
                    onTextChanged = { changeText ->
                        orderRegistrationAction.changeTextHouse(
                            changeText
                        )
                    },
                    modifier = Modifier
                        .weight(0.5f)
                        .padding(8.dp),
                    error = orderRegistrationState.house.invalid
                )
            }
            Row {
                InputItem(
                    textFieldValue = orderRegistrationState.entrance,
                    label = "Подъезд",
                    onTextChanged = { changeText ->
                        orderRegistrationAction.changeTextEntrance(
                            changeText
                        )
                    },
                    modifier = Modifier
                        .weight(0.5f)
                        .padding(8.dp),
                )
                InputItem(
                    textFieldValue = orderRegistrationState.flat,
                    label = "Квартира",
                    onTextChanged = { changeText ->
                        orderRegistrationAction.changeTextFlat(
                            changeText
                        )
                    },
                    modifier = Modifier
                        .weight(0.5f)
                        .padding(8.dp),
                )
            }
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(96.dp)
            ) {
                OutlinedTextField(
                    value = orderRegistrationState.comment,
                    onValueChange = { orderRegistrationAction.changeTextComment(it) },
                    label = { CreateLabel(label = "Комментарий") },
                    modifier = Modifier.fillMaxSize()
                )
            }
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.padding(16.dp)
            ) {
                Checkbox(
                    checked = orderRegistrationState.isPreOrder,
                    onCheckedChange = { orderRegistrationAction.checkPreOrder() }
                )
                Text("Предзаказ", modifier = Modifier.padding(start = 8.dp))
            }
            if (orderRegistrationState.isPreOrder) {
                PreOrder(
                    preOrderDate = orderRegistrationState.date,
                    modifier = Modifier.padding(16.dp),
                    orderRegistrationAction.openDialog
                )
                if (orderRegistrationState.isOpenedDatePicker) {
                    DatePicker(onDateSelected = {
                        val preOrderDate = PreOrderDate(it.time, format.format(it.time))
                        orderRegistrationAction.changePreOrderDate(preOrderDate)
                    }) {
                        orderRegistrationAction.openDialog()
                    }
                }
            }
            Spacer(modifier = Modifier.height(40.dp))
            if (orderRegistration is OrderRegistration.PlaceOrder) {
                CircularProgressIndicator()
            }
            Button(
                onClick = {
                    placeOrder()
                },
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .fillMaxWidth()
            ) {
                Text("Заказать")
            }
        }
    }
}

@Composable
fun ProcessError(
    msg: String,
    scaffoldState: ScaffoldState
) {
    if (msg == "Empty basket") {
        LaunchedEffect(scaffoldState.snackbarHostState) {
            scaffoldState.snackbarHostState.showSnackbar(
                message = "Корзина пуста!",
            )
        }
    }
}

@Composable
fun PreOrder(
    preOrderDate: PreOrderDate?,
    modifier: Modifier = Modifier,
    openDialog: () -> Unit
) {
    Surface(modifier = modifier) {
        Row {
            Text(
                buildAnnotatedString {
                    append("Дата: ")
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                        append(preOrderDate?.dateRepresent ?: "Не выбрано")
                    }
                },
                modifier = Modifier.weight(1f)
            )
            Button(
                onClick = { openDialog() },
                shape = RoundedCornerShape(8.dp),
                contentPadding = PaddingValues(8.dp, 6.dp),
                modifier = Modifier
                    .widthIn(1.dp)
                    .heightIn(1.dp)
            ) {
                Text("Выбрать")
            }
        }
    }
}

@Composable
fun CreateLabel(label: String) {
    CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
        Row {
            Text(
                text = label,
                style = MaterialTheme.typography.body2
            )
        }
    }
}

@Composable
fun InputItem(
    textFieldValue: String,
    label: String,
    onTextChanged: (String) -> Unit,
    modifier: Modifier = Modifier,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    textStyle: TextStyle = MaterialTheme.typography.body1,
    keyboardType: KeyboardType = KeyboardType.Text,
    error: Boolean = false
) {
    OutlinedTextField(
        value = textFieldValue,
        onValueChange = { onTextChanged(it) },
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType,
            imeAction = ImeAction.Next
        ),
        textStyle = textStyle,
        maxLines = 1,
        singleLine = true,
        label = {
            CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                Row {
                    Text(
                        text = label,
                        style = MaterialTheme.typography.body2
                    )
                }
            }
        },
        modifier = modifier,
        visualTransformation = visualTransformation,
        isError = error
    )
}

fun MutableState<OrderRegistrationState>.checkFields(): Boolean {
    value = value.copy(
        phone = value.phone.copy(invalid = !value.phone.text.matches(Regex("^(\\+7|8)[0-9]{10}\$"))),
        name = value.name.copy(invalid = value.name.checkInvalid()),
        city = value.city.copy(invalid = value.city.checkInvalid()),
        street = value.street.copy(invalid = value.street.checkInvalid()),
        house = value.house.copy(invalid = value.street.checkInvalid())
    )
    return value.checkValid()
}

fun OrderRegistrationState.checkValid(): Boolean =
    !(phone.invalid || name.invalid || city.invalid || street.invalid || house.invalid)

fun TextField.checkInvalid(): Boolean =
    text.trim() == ""

fun MutableState<OrderRegistrationState>.getOrderRegistrationTextChange(): OrderRegistrationAction {
    return OrderRegistrationAction(
        {
            value = value.copy(
                phone = value.phone.copy(
                    text = it,
                    invalid = !it.matches(Regex("^(\\+7|8)[0-9]{10}\$"))
                )
            )
        },
        { value = value.copy(name = value.name.copy(text = it, invalid = it.trim() == "")) },
        { value = value.copy(city = value.city.copy(text = it, invalid = it.trim() == "")) },
        { value = value.copy(street = value.street.copy(text = it, invalid = it.trim() == "")) },
        { value = value.copy(house = value.house.copy(text = it, invalid = it.trim() == "")) },
        { value = value.copy(entrance = it) },
        { value = value.copy(flat = it) },
        { value = value.copy(comment = it) },
        { value = value.copy(isPreOrder = !value.isPreOrder) },
        { value = value.copy(isOpenedDatePicker = !value.isOpenedDatePicker) },
        { value = value.copy(date = it, isOpenedDatePicker = true) }
    )
}