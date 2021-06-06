package ru.vsu.ofood.ui.home

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.coil.CoilImage
import ru.vsu.ofood.R
import ru.vsu.ofood.entities.Category
import ru.vsu.ofood.entities.Dish
import ru.vsu.ofood.ui.DishState
import ru.vsu.ofood.ui.HomeScreenEvent
import ru.vsu.ofood.ui.HomeScreenState
import ru.vsu.ofood.ui.theme.OFoodTheme
import ru.vsu.ofood.ui.theme.selectedTabColor

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

// TODO pass arguments DishCards
@Composable
fun HomeScreen(
    homeScreenMiddleware: HomeScreenMiddleware
) {
    val state = homeScreenMiddleware.state.collectAsState()
    HomeContent(homeScreenState = state.value, homeScreenMiddleware::complete)
}

@Composable
fun HomeContent(
    homeScreenState: HomeScreenState,
    interpret: (HomeScreenEvent) -> Unit
) {
    Column {
        when (homeScreenState) {
            is HomeScreenState.Empty -> Text("Nothing")
            is HomeScreenState.LoadingCategories -> LoadingCategoriesContent()
            is HomeScreenState.EmptyCategories -> Text("Don't fiil categories")
            is HomeScreenState.ContentCategories -> SuccessfulCategoriesLoading(
                homeScreenState = homeScreenState,
                interpret
            )
            //is HomeScreenState.ErrorCategories -> Text("Failure")
        }
    }
}

@Composable
fun LoadingCategoriesContent() {
    val colors = listOf(
        Color.Gray.copy(0.9f),
        Color.Gray.copy(0.3f),
        Color.Gray.copy(0.9f)
    )

    val infiniteTransition = rememberInfiniteTransition()

    BoxWithConstraints {
        val infiniteTransitionX = rememberInfiniteTransition()
        val startPadding by infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = 140f,
            animationSpec = infiniteRepeatable(
                animation = tween(1000, easing = LinearEasing),
                repeatMode = RepeatMode.Restart
            )
        )
        val size by infiniteTransitionX.animateFloat(
            initialValue = 0f,
            targetValue = 30f,
            animationSpec = infiniteRepeatable(
                animation = tween(1000, easing = LinearEasing),
                repeatMode = RepeatMode.Reverse
            )
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Surface(
                modifier = Modifier
                    .padding(startPadding.dp)
                    .size(size.dp),
                shape = RoundedCornerShape(8.dp),
                color = Color.Red
            ) {

            }
        }
    }
    /*  infiniteTransition.animateFloat(
          initialValue = ,
          targetValue = ,
          animationSpec =
      )
    val color by infiniteTransition.animateColor(
        initialValue = Color.Red,
        targetValue = Color.Green,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    Box(
        Modifier
            .fillMaxSize()
            .background(color)
    )
     */
}

@Composable
fun SuccessfulCategoriesLoading(
    homeScreenState: HomeScreenState.ContentCategories,
    interpret: (HomeScreenEvent) -> Unit
) {
    Search()
    TagCategories(homeScreenState.categories.map(Category::name))
    when (homeScreenState.dishState) {
        is DishState.LoadingDishes -> Text("Loading")
        is DishState.Empty -> Text("Nothing")
        is DishState.ContentDishes -> {
            LazyColumn() {
                homeScreenState.dishState.dishes.forEach { dish ->
                    item {
                        DishCard(
                            Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                            dish,
                            dish.photoPath
                        ) {
                            interpret(HomeScreenEvent.ClickDetailDishEvent(dish))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Search() {
    val text = remember { mutableStateOf("") }

    Row(verticalAlignment = Alignment.CenterVertically) {
        OutlinedTextField(
            value = text.value,
            onValueChange = { changeText -> text.value = changeText },
            modifier = Modifier
                .padding(8.dp)
                .weight(1f),
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_baseline_filter_list_24),
                    contentDescription = "search"
                )
            },
            singleLine = true
        )
        Button(onClick = { /*TODO*/ }, Modifier.padding(8.dp)) {
            Image(
                painterResource(id = R.drawable.ic_baseline_filter_list_24),
                contentDescription = null,
            )
        }

    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    OFoodTheme {
        Greeting("Android")
    }
}

@Composable
fun TagCategories(
    categories: List<String>
) {
    Row(modifier = Modifier.horizontalScroll(rememberScrollState())) {
        categories.forEachIndexed { index, s ->
            Tab(selected = index == 0, onClick = { /*TODO*/ }) {
                ChoiceChipCategory(
                    text = s,
                    isSelected = index == 0,
                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 16.dp)
                )
            }
        }
    }
}


@Composable
private fun ChoiceChipCategory(
    text: String,
    isSelected: Boolean,
    modifier: Modifier = Modifier
) {
    Surface(
        color = when {
            isSelected -> selectedTabColor
            else -> MaterialTheme.colors.onSurface.copy(alpha = 0.12f)
        },
        contentColor = if (isSelected) MaterialTheme.colors.surface else MaterialTheme.colors.onSurface,
        shape = RoundedCornerShape(16.dp),
        modifier = modifier
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.body2,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
    }
}

@Composable
fun DishCard(
    modifier: Modifier = Modifier,
    dish: Dish,
    path: String = "https://static-eu.insales.ru/files/1/5763/9737859/original/Efood_21-min.jpg",
    onClick: () -> Unit
) {
    Card(
        elevation = 4.dp,
        shape = RoundedCornerShape(size = 16.dp),
        modifier = modifier
            .height(160.dp)
            .fillMaxWidth()
    ) {
        Row {
            CoilImage(
                data = path,
                contentDescription = "dsa",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .width(160.dp)
                    .fillMaxHeight()
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    dish.name,
                    maxLines = 1
                )
                if (dish.discount == 0) {
                    Text(
                        dish.price.toString() + " ₽",
                        maxLines = 2
                    )
                }
                else {
                    Text(
                        dish.price.toString() + " ₽",
                        maxLines = 2,
                        style = MaterialTheme.typography.overline,
                        textDecoration = TextDecoration.LineThrough
                    )
                    Text(
                        ( dish.price - dish.price * dish.discount / 100 ) .toString() + " ₽",
                        maxLines = 2,
                    )
                }
            }
            Spacer(modifier = Modifier.width(8.dp))
            Box(
                modifier = Modifier.fillMaxHeight(),
                contentAlignment = Alignment.Center
            ) {
                ButtonNext(onClick)
            }
            Spacer(modifier = Modifier.width(8.dp))
        }
    }
}

@Preview
@Composable
fun ButtonNext(onClick: () -> Unit = {}) {
    Button(
        onClick = { onClick() },
        shape = RoundedCornerShape(16.dp),
        contentPadding = PaddingValues(8.dp, 8.dp),
        modifier = Modifier
            .widthIn(32.dp)
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_next),
            contentDescription = "next"
        )
    }
}