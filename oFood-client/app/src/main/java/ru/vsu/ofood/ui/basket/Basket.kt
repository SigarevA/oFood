package ru.vsu.ofood.ui.basket

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.ButtonDefaults.buttonColors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.coil.CoilImage
import kotlinx.coroutines.launch
import ru.vsu.ofood.R
import ru.vsu.ofood.di.ComponentHolder
import ru.vsu.ofood.dto.DishToBasketDTO
import ru.vsu.ofood.entities.Dish
import ru.vsu.ofood.ui.BasketDestination
import ru.vsu.ofood.ui.NavigationOperation
import ru.vsu.ofood.ui.theme.selectedTabColor

@Composable
fun BasketScreen(basketNavigate: (NavigationOperation) -> Unit) {
    val basketScreenMiddleware = ComponentHolder.basketTabComponent!!.getBasketScreenMiddleware()
    LaunchedEffect("dsa") {
        suspend {
            basketScreenMiddleware.complete(BasketAction.LoadDishes)
        } ()
    }
    val basketState = basketScreenMiddleware.basketState.collectAsState()
    BasketContent(basketState = basketState.value, reduceAction = basketScreenMiddleware::complete, basketNavigate)
}

@Composable
fun BasketContent(
    basketState: BasketState,
    reduceAction: (BasketAction) -> Unit,
    basketNavigate: (NavigationOperation) -> Unit
) {
    when (basketState) {
        is BasketState.EmptyState -> Text("Empty")
        is BasketState.EmptyDishesState -> EmptyBasket()
        is BasketState.ContentDishesState -> {
            BasketStateContent(basketState.dishes, reduceAction, basketNavigate)
        }
        is BasketState.ErrorState -> {
        }
    }
}

@Composable
fun BasketStateContent(
    dishes: List<DishToBasketDTO>,
    reduceAction: (BasketAction) -> Unit,
    basketNavigate: (NavigationOperation) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Корзина") }
            )
        }
    ) {
        Column {
            LazyColumn(modifier = Modifier.weight(1f)) {
                dishes.forEach {
                    item {
                        ItemDishInBasket(
                            Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                            it,
                            reduceAction
                        )
                    }
                }
            }
            Button(
                onClick = { basketNavigate(NavigationOperation.Destination(BasketDestination.OrderRegistration)) },
                modifier = Modifier
                    .padding(
                        horizontal = 16.dp,
                        vertical = 8.dp
                    )
                    .fillMaxWidth(),
                shape = RoundedCornerShape(16.dp)
    //            colors = buttonColors(
     //               backgroundColor = selectedTabColor,
      //              contentColor = Color.White
                //)
            ) {
                Text("Оформить")
            }
        }
    }
}

@Composable
fun ItemDishInBasket(
    modifier: Modifier = Modifier,
    dishToBasketDTO: DishToBasketDTO,
    reduceAction: (BasketAction) -> Unit
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .height(120.dp),
        elevation = 4.dp,
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
        ) {
            CoilImage(
                data = dishToBasketDTO.dish.photoPath,
                contentDescription = "Dish image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .width(120.dp)
                    .fillMaxHeight()
            )
            Box(
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .fillMaxHeight(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "${dishToBasketDTO.count}x",
                    modifier = Modifier
                )
            }
            Column(modifier = Modifier) {
                Row(
                    modifier = Modifier
                        .weight(1f)
                        .padding(all = 16.dp)
                ) {
                    Text(
                        dishToBasketDTO.dish.name,
                        modifier = Modifier
                            .weight(1f)
                    )
                    DeleteDishFromBasketIcon() {
                        reduceAction(BasketAction.DeleteDishFromBasketAction(dishToBasketDTO.dish))
                    }
                }
                Row(modifier = Modifier.padding(all = 16.dp)) {
                    ActionCountDish(
                        Modifier.weight(1f),
                        {
                            reduceAction(BasketAction.IncreaseCountEvent(dish = dishToBasketDTO.dish))
                        }, {
                            reduceAction(BasketAction.DecreaseCountEvent(dish = dishToBasketDTO.dish))
                        }
                    )
                    Text(dishToBasketDTO.dish.price.toString())
                }
            }
        }
    }
}

@Composable
fun ActionCountDish(
    modifier: Modifier = Modifier,
    increaseQuantity: () -> Unit,
    decreaseQuantity: () -> Unit
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(
            modifier = Modifier.size(24.dp),
            shape = RoundedCornerShape(12.dp),
            color = Color(0xFFF6F6F6)
        ) {
            IconButton(onClick = { decreaseQuantity() }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_remove),
                    contentDescription = null,
                    modifier = Modifier.padding(4.dp)
                )
            }
        }
        Text(
            "01",
            modifier = Modifier
                .padding(horizontal = 8.dp)
        )
        Surface(
            modifier = Modifier.size(24.dp),
            shape = RoundedCornerShape(12.dp),
            color = Color(0xFFF6F6F6)
        ) {
            IconButton(onClick = { increaseQuantity() }) {
                Icon(
                    Icons.Filled.Add,
                    contentDescription = null,
                    modifier = Modifier.padding(4.dp)
                )
            }
        }
    }
}

@Composable
fun DeleteDishFromBasketIcon(
    deleteDishFromBasket: () -> Unit
) {
    Surface(
        modifier = Modifier.size(24.dp),
        shape = RoundedCornerShape(12.dp),
        color = selectedTabColor.copy(alpha = 0.45f)
    ) {
        IconButton(onClick = { deleteDishFromBasket() }) {
            Icon(
                Icons.Filled.Close,
                contentDescription = null,
                modifier = Modifier.padding(4.dp)
            )
        }
    }
}

@Composable
fun BasketEmptyStat() {

}

@Composable
fun EmptyBasket() {
    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_circle_basket),
                contentDescription = "null"
            )
            Text(
                "Корзина пуста",
                color = Color.Black,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}