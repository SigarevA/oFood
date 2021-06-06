package ru.vsu.oFoodAdmin.ui.addingpromotion


import android.util.Log
import kotlinx.coroutines.channels.Channel
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import ru.vsu.oFoodAdmin.domain.DishRepo
import ru.vsu.oFoodAdmin.domain.PromotionRepo
import ru.vsu.oFoodAdmin.domain.Result
import ru.vsu.oFoodAdmin.domain.succeeded
import ru.vsu.oFoodAdmin.entities.Dish
import ru.vsu.oFoodAdmin.network.response.DishResponse
import ru.vsu.oFoodAdmin.ui.components.DatePicker
import ru.vsu.oFoodAdmin.ui.state.UiState
import ru.vsu.oFoodAdmin.ui.state.copyWithResult
import ru.vsu.oFoodAdmin.ui.utils.ErrorState
import ru.vsu.oFoodAdmin.ui.utils.Loading
import java.text.SimpleDateFormat
import java.util.*

data class AddingAction(
    val nameChange: (String) -> Unit,
    val descriptionChange: (String) -> Unit,
    val discountChange: (String) -> Unit,
    val openStartDatePicker: () -> Unit,
    val openEndDatePicker: () -> Unit,
    val changeStartDate: (Date, String) -> Unit,
    val changeEndDate: (Date, String) -> Unit,
    val selectedDish: (DishDTO) -> Unit,
    val GetDishes: (List<DishResponse>) -> Unit,
    val PublishPromotion: () -> Unit
)

@Composable
fun AddingPromotionScreen(
    dishRepo: DishRepo,
    promotionRepo: PromotionRepo
) {
    Log.d("Recomposition", "compose")
    val refreshChannel = remember { Channel<AddingPromotionEvent>(Channel.CONFLATED) }
    val promotionState =
        rememberSaveable { mutableStateOf(AddingPromotionStateContent()) }
    val dishesUIState = produceState(
        UiState<List<DishResponse>>(loading = true), "dishRepo", dishRepo
    ) {
        Log.d("Promotion", "produce UI")
        refreshChannel.send(AddingPromotionEvent.INIT)
        for (event in refreshChannel) {
            Log.d("refreshChannel", "refreshChannel UI")
            Log.d("Screen", "$event")
            when (event) {
                is AddingPromotionEvent.INIT -> {
                    val result = dishRepo.getCurrentDishes()
                    value = value.copyWithResult(result)
                    value.data?.let {
                        promotionState.value =
                            promotionState.value.copy(
                                products = it.map { dish -> dish.toDishDTO() }
                            )
                    }
                }
                is AddingPromotionEvent.REQUEST -> {
                    promotionState.value = promotionState.value.copy(isInvalid = false)
                    promotionState.checkTextFiledData()
                    promotionState.checkData()
                    if (!promotionState.value.isInvalid) {
                        Log.d("TAG", "isInvalid")
                        val result = promotionRepo.createPromotion(promotionState.value.toCreationPromotionDTO())
                        if (result.succeeded) {
                            promotionState.successAdding()
                        }
                        else {
                            promotionState.failureAddig()
                        }
                    }
                }
            }
        }
    }
    val actions = remember { promotionState.getActions(eventChannel = refreshChannel) }

    AddingPromotionScreen(
        promotionState.value,
        dishesUIState.value,
        actions
    )
}

@Composable
fun AddingPromotionScreen(
    addingPromotionStateContent: AddingPromotionStateContent,
    dishes: UiState<List<DishResponse>>,
    addingAction: AddingAction,
    scaffoldState: ScaffoldState = rememberScaffoldState()
) {
    if (addingPromotionStateContent.isInvalid) {
        LaunchedEffect(scaffoldState.snackbarHostState) {
            scaffoldState.snackbarHostState.showSnackbar(
                message = "Заполнены не все поля!"
            )
        }
    }
    if (addingPromotionStateContent.successAdding) {
        LaunchedEffect(scaffoldState.snackbarHostState) {
            scaffoldState.snackbarHostState.showSnackbar(
                message = "Акция успешно добавлена!"
            )
        }
    }
    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(
                title = {
                    Text("Добавление Акции")
                },
                navigationIcon = { Icon(Icons.Filled.ArrowBack, null) }
            )
        }
    ) {
        if (dishes.loading) {
            Loading()
        } else if (dishes.hasError)
            ErrorState()
        else {
            AddingPromotionScreenContent(
                addingPromotionStateContent = addingPromotionStateContent,
                addingAction = addingAction
            )
        }
    }
}

@Composable
fun AddingPromotionScreenContent(
    addingPromotionStateContent: AddingPromotionStateContent,
    addingAction: AddingAction
) {
    LazyColumn(
        modifier = Modifier
            .padding(top = 16.dp, start = 16.dp, end = 16.dp)
    ) {
        item {
            InputItem(
                addingPromotionStateContent.name.text,
                "Название",
                { addingAction.nameChange(it) },
                modifier = Modifier.fillMaxWidth(),
                error = addingPromotionStateContent.name.invalid
            )
        }
        item {
            OutlinedTextField(
                value = addingPromotionStateContent.describe.text,
                onValueChange = { str -> addingAction.descriptionChange(str) },
                maxLines = 1,
                singleLine = true,
                label = {
                    CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                        Row {
                            Text(
                                text = "Описание",
                                style = MaterialTheme.typography.body2
                            )
                        }
                    }
                },
                isError = addingPromotionStateContent.describe.invalid
            )
        }
        item {
            OutlinedTextField(
                value = addingPromotionStateContent.discount.text,
                onValueChange = { str -> addingAction.discountChange(str) },
                maxLines = 1,
                singleLine = true,
                label = {
                    CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                        Row {
                            Text(
                                text = "Скидка",
                                style = MaterialTheme.typography.body2
                            )
                        }
                    }
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                isError = addingPromotionStateContent.discount.invalid
            )
        }
        item {
            Surface {
                Column {
                    Row(
                        verticalAlignment = CenterVertically,
                        modifier = Modifier.padding(top = 16.dp, bottom = 16.dp)
                    ) {
                        Text(
                            "Начало: ${addingPromotionStateContent.start?.represent ?: "Не выбрано"}",
                            modifier = Modifier.weight(1f)
                        )
                        Button(
                            onClick = { addingAction.openStartDatePicker() }
                        )
                        {
                            Text("Выбрать")
                        }
                    }
                    Row(
                        verticalAlignment = CenterVertically,
                        modifier = Modifier.padding(top = 16.dp, bottom = 16.dp)
                    ) {
                        Text(
                            "Конец: ${addingPromotionStateContent.end?.represent ?: "Не выбрано"}",
                            modifier = Modifier.weight(1f)
                        )
                        Button(
                            onClick = { addingAction.openEndDatePicker() },
                            enabled = addingPromotionStateContent.start != null
                        )
                        {
                            Text("Выбрать")
                        }
                    }
                }
            }
        }
        item {
            if (addingPromotionStateContent.flagsDatePicker.isOpenedStartDatePicker) {
                DatePicker(
                    {
                        val format = SimpleDateFormat("dd.MM.yyyy")
                        addingAction.changeStartDate(it.time, format.format(it.time))
                    },
                    {
                        addingAction.openStartDatePicker()
                    }
                )
            }
            if (addingPromotionStateContent.flagsDatePicker.isOpenedEndDatePicker) {
                DatePicker(
                    {
                        val format = SimpleDateFormat("dd.MM.yyyy")
                        addingAction.changeEndDate(it.time, format.format(it.time))
                    },
                    {
                        addingAction.openEndDatePicker()
                    }
                )
            }
        }
        addingPromotionStateContent.products?.forEach { dishDto ->
            item {
                Row(verticalAlignment = CenterVertically) {
                    Checkbox(
                        checked = dishDto.isSelected,
                        onCheckedChange = { addingAction.selectedDish(dishDto) }
                    )
                    Text(dishDto.name, modifier = Modifier.padding(top = 16.dp, bottom = 16.dp))
                }
            }
        }
        item {
            Button(
                onClick = {
                    addingAction.PublishPromotion()
                }
            ) {
                Text("Опубликовать")
            }
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

private fun MutableState<AddingPromotionStateContent>.getActions(eventChannel: Channel<AddingPromotionEvent>): AddingAction {
    return AddingAction(
        {
            value = value.copy(name = value.name.copy(text = it, invalid = it.trim() == ""))
        },
        {
            value = value.copy(describe = value.describe.copy(text = it, invalid = it.trim() == ""))
        },
        {
            value =
                value.copy(discount = value.discount.copy(text = it, invalid = it.trim() == ""))
        },
        {
            value = value.copy(
                flagsDatePicker = value.flagsDatePicker.copy(
                    isOpenedStartDatePicker = !value.flagsDatePicker.isOpenedStartDatePicker
                )
            )
        },
        {
            value =
                value.copy(
                    flagsDatePicker = value.flagsDatePicker.copy(
                        isOpenedEndDatePicker = !value.flagsDatePicker.isOpenedEndDatePicker
                    )
                )
        },
        { date, rep ->
            value = value.copy(
                start = PromotionDate(date, rep),
                flagsDatePicker = value.flagsDatePicker.copy(
                    isOpenedStartDatePicker = true
                )
            )
        },
        { date, rep ->
            value = value.copy(
                end = PromotionDate(date, rep),
                flagsDatePicker = value.flagsDatePicker.copy(
                    isOpenedEndDatePicker = true
                )
            )
        },
        { dishDto ->
            value =
                value.copy(products = value.products?.map {
                    if (it.dishID == dishDto.dishID)
                        it.copy(isSelected = !it.isSelected)
                    else it
                }
                )
        },
        {
            value =
                value.copy(
                    products = it.map { dish -> dish.toDishDTO() }
                )
        },
        {
            Log.d("Channel", "click")
            eventChannel.offer(AddingPromotionEvent.REQUEST)
        }
    )
}

private fun MutableState<AddingPromotionStateContent>.checkTextFiledData() {
    value = value.copy(
        name = value.name.copy(invalid = value.name.text.trim() == ""),
        describe = value.describe.copy(invalid = value.describe.text.trim() == ""),
        discount = value.discount.copy(invalid = value.discount.text.trim() == ""),
    )
}

private fun MutableState<AddingPromotionStateContent>.checkData() {
    val temp = value.end == null || value.start == null || value.describe.invalid ||
            value.name.invalid || value.discount.invalid || value.products?.let { dishes ->
        dishes.filter { it.isSelected }
            .count() == 0
    } ?: true
    value = value.copy(
        isInvalid = temp,
        isRequest = !temp
    )
}

private fun MutableState<AddingPromotionStateContent>.successAdding() {
    value = value.copy(successAdding = true, failureAdding = false, isRequest = false, isInvalid = false)
}

private fun MutableState<AddingPromotionStateContent>.failureAddig() {
    value = value.copy(failureAdding = true, successAdding = false, isRequest = false, isInvalid = false)
}