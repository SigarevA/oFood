package ru.vsu.oFoodAdmin.ui.addingdish

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import kotlinx.coroutines.channels.Channel
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Create
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.google.accompanist.glide.rememberGlidePainter
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import ru.vsu.oFoodAdmin.di.exceptions.NotCategoryException
import ru.vsu.oFoodAdmin.domain.CategoryRepo
import ru.vsu.oFoodAdmin.domain.DishRepo
import ru.vsu.oFoodAdmin.domain.Result


import ru.vsu.oFoodAdmin.ui.MainActivity
import ru.vsu.oFoodAdmin.ui.addingpromotion.TextField
import ru.vsu.oFoodAdmin.ui.state.UiState
import ru.vsu.oFoodAdmin.ui.utils.ErrorState
import ru.vsu.oFoodAdmin.ui.utils.Loading

class Action(channel: Channel<AddingDishEvent>) {
    val changeName: (String) -> Unit = {
        Log.d("AddingDish", "fdsfsd fsf sd")
        channel.offer(AddingDishEvent.ChangeName(it))
    }
    val changeDescription: (String) -> Unit = {
        channel.offer(AddingDishEvent.ChangeDescription(it))
    }
    val changePrice: (String) -> Unit = {
        channel.offer(AddingDishEvent.ChangePrice(it))
    }
    val selectCategory: (String) -> Unit = {
        channel.offer(AddingDishEvent.SelectCategory(it))
    }
    val placeDish: () -> Unit = {
        channel.offer(AddingDishEvent.PlaceDish)
    }
}

@Composable
fun AddingDishScreen(
    categoryRepo: CategoryRepo,
    dishRepo: DishRepo
) {
    Log.d("AddingDish", "haidsa")
    val uriChannel = (LocalContext.current as MainActivity).uriChannel

    val refreshChannel = remember { Channel<AddingDishEvent>(Channel.CONFLATED) }
    val actions = remember { Action(refreshChannel) }
    val selectedImage = (LocalContext.current as MainActivity).selectedImageLauncher
    val screenState =
        produceState<UiState<AddingDishState>>(
            UiState<AddingDishState>(loading = true),
            categoryRepo
        ) {
            Log.d("Dish", "produce UI")
            refreshChannel.send(AddingDishEvent.Init)
            Log.d("AddingDish", "send")
            launch {
                for (uri in uriChannel) {
                    value.data?.let {
                        value =
                            value.copy(data = it.copy(content = it.content.copy(imageUri = uri)))
                    }
                }
            }
            for (event in refreshChannel) {
                when (event) {
                    is AddingDishEvent.Init -> {
                        val result = categoryRepo.getCategory()
                        value = when (result) {
                            is Result.Success -> {
                                if (result.data.isEmpty()) {
                                    value.copy(loading = false, exception = NotCategoryException())
                                } else
                                    value.copy(
                                        loading = false,
                                        data = AddingDishState(
                                            result.data.map { it.toCategoryDTO() },
                                            AddingDishScreenContent()
                                        )
                                    )
                            }
                            is Result.Error -> value.copy(
                                loading = false,
                                exception = NotCategoryException()
                            )
                        }
                    }
                    is AddingDishEvent.PlaceDish -> {
                        Log.d("Dish", "place dish")
                        value = value.copy(
                            data = value.data!!.copy(
                                content =
                                value.data!!.content.copy(isError = false)
                            )
                        )
                        value =
                            value.copy(data = value.data!!.copy(content = value.data!!.content.computeInvalid()))
                        val validData = value.data!!.checkValid()
                        value = value.copy(
                            data = value.data!!.copy(
                                content =
                                value.data!!.content.copy(isError = !validData)
                            )
                        )
                        if (validData) {
                            Log.d("Dish", "hau")
                            dishRepo.createDish(value.data!!.content)
                        }
                    }
                    else -> {
                        value.data?.let {
                            Log.d("AddingDish", "hai")
                            value = value.copy(data = it.processEvent(event))
                        }
                    }
                }
                Log.d("AddingDish", "event")
            }
            Log.d("AddingDish", "end")

        }
    AddingDishScreen(
        state = screenState.value, {
            selectedImage.launch("image/*")
        },
        actions
    )
}

@Composable
fun AddingDishScreen(
    state: UiState<AddingDishState>,
    selectedImage: () -> Unit,
    actions: Action,
    scaffoldState: ScaffoldState = rememberScaffoldState()
) {
    state.data?.let {
        if (it.content.isError) {
            LaunchedEffect(scaffoldState.snackbarHostState) {
                scaffoldState.snackbarHostState.showSnackbar(
                    message = "Заполнены не все поля!"
                )
            }
        }
    }
    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(
                title = {
                    Text("Добавление Товара")
                },
                navigationIcon = { Icon(Icons.Filled.ArrowBack, null) }
            )
        }
    ) {
        if (state.loading) {
            Loading()
        } else if (state.hasError)
            ErrorState()
        else {
            AddingDishScreenContent(state.data!!, selectedImage, actions)
        }
    }
}

@Composable
fun AddingDishScreenContent(state: AddingDishState, selectedImage: () -> Unit, actions: Action) {
    Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            if (state.content.imageUri != null) {
                Image(
                    rememberGlidePainter(request = state.content.imageUri!!),
                    null,
                    modifier = Modifier
                        .width(240.dp)
                        .height(240.dp),
                    contentScale = ContentScale.Crop
                )
            } else {
                Surface(
                    modifier = Modifier
                        .width(240.dp)
                        .height(240.dp)
                        .border(
                            width = 5.dp,
                            color = Color.Gray,
                            shape = CutCornerShape(16.dp)
                        ),
                ) {
                    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                        Icon(Icons.Filled.Create, null)
                    }
                }
            }
        }
        Button(onClick = { selectedImage() }) {
            Text("Выбрать фото")
        }
        OutlinedTextField(
            value = state.content.name.text,
            onValueChange = {
                Log.d("AddingDish", "change name")
                actions.changeName(it)
            },
            modifier = Modifier
                .padding(
                    horizontal = 16.dp,
                    vertical = 8.dp
                )
                .fillMaxWidth(),
            maxLines = 1,
            singleLine = true,
            label = { Text("Название") },
            isError = state.content.name.invalid
        )
        OutlinedTextField(
            value = state.content.price.text,
            onValueChange = { actions.changePrice(it) },
            modifier = Modifier
                .padding(
                    horizontal = 16.dp,
                    vertical = 8.dp
                )
                .fillMaxWidth(),
            maxLines = 1,
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            ),
            label = { Text("Стоимость") },
            isError = state.content.price.invalid
        )
        Surface(
            modifier = Modifier
                .padding(
                    horizontal = 16.dp,
                    vertical = 8.dp
                )
                .height(64.dp)
                .fillMaxWidth()
        ) {
            OutlinedTextField(
                value = state.content.description.text,
                onValueChange = { actions.changeDescription(it) },
                maxLines = 1,
                label = { Text("Описание") },
                modifier = Modifier.fillMaxSize(),
                isError = state.content.description.invalid
            )
        }
        Text("Категория: ", modifier = Modifier.padding(16.dp))
        state.categories.forEach {
            Row(
                Modifier
                    .fillMaxWidth()
                    .selectable(
                        selected = (it.categoryId == state.content.categoryId),
                        onClick = {
                            actions.selectCategory(it.categoryId)
                        }
                    )
                    .padding(horizontal = 16.dp)
            ) {
                RadioButton(
                    selected = (it.categoryId == state.content.categoryId),
                    onClick = { actions.selectCategory(it.categoryId) }
                )
                Text(
                    text = it.name,
                    style = MaterialTheme.typography.body1.merge(),
                    modifier = Modifier.padding(start = 16.dp)
                )
            }
        }
        Button(onClick = { actions.placeDish() }) {
            Text("Опубликовать")
        }
    }
}

private fun AddingDishState.processEvent(event: AddingDishEvent): AddingDishState {
    return when (event) {
        is AddingDishEvent.ChangeName -> copy(
            content = content.copy(
                name = content.name.newTextField(event.value)
            )
        )
        is AddingDishEvent.ChangeDescription -> copy(
            content = content.copy(
                description = content.description.newTextField(event.value)
            )
        )
        is AddingDishEvent.ChangePrice -> {
            copy(
                content = content.copy(
                    price = content.price.copy(
                        event.value,
                        try {
                            event.value.toInt()
                            false
                        } catch (ex: NumberFormatException) {
                            true
                        }
                    )
                )
            )
        }
        is AddingDishEvent.SelectCategory -> {
            copy(content = content.copy(categoryId = event.categoryId))
        }
        is AddingDishEvent.GetUri -> {
            copy(content = content.copy(imageUri = event.uri))
        }
        else -> throw IllegalArgumentException()
    }
}

fun AddingDishState.checkValid(): Boolean {
    return this.content.categoryId != null && content.imageUri != null &&
            !(content.price.invalid || content.description.invalid || content.name.invalid)
}

fun AddingDishScreenContent.computeInvalid(): AddingDishScreenContent =
    copy(
        name = name.copy(invalid = name.text.trim() == ""),
        description = description.copy(invalid = description.text.trim() == ""),
        price = price.copy(
            invalid = try {
                price.text.toInt()
                false
            } catch (ex: NumberFormatException) {
                true
            }
        )
    )

fun TextField.newTextField(newValue: String): TextField =
    copy(
        text = newValue,
        invalid = newValue.trim() == ""
    )