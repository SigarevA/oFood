package ru.vsu.ofood.ui.detail

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.ButtonDefaults.buttonColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.coil.CoilImage
import ru.vsu.ofood.R
import ru.vsu.ofood.entities.Dish
import ru.vsu.ofood.ui.Interpret
import ru.vsu.ofood.ui.theme.BtnShape
import ru.vsu.ofood.ui.theme.DetailDishColor
import ru.vsu.ofood.ui.theme.dishShip
import ru.vsu.ofood.ui.theme.selectedTabColor

class ChangeCount(
    val increase: () -> Unit,
    val decrease: () -> Unit
)

@Composable
fun DetailDishScreen(
    dish: Dish,
    interpret: Interpret<DishScreenEvent>
) {
    val dishState = remember { mutableStateOf(DishState(dish = dish)) }
    DetailDishContent(
        dishState = dishState.value,
        changeCount = ChangeCount(
            { dishState.value = dishState.dishChangeCount(1) },
            {
                if (dishState.value.count == 1)
                    dishState.value = dishState.dishChangeCount(-1)
            }
        ),
        interpret = interpret
    )
}

@Composable
fun DetailDishContent(
    modifier: Modifier = Modifier,
    dishState: DishState,
    changeCount: ChangeCount,
    interpret: Interpret<DishScreenEvent>
) {
    Column {
        Column(modifier = Modifier.weight(1f)) {
            CoilImage(
                data = dishState.dish.photoPath,
                contentDescription = null
            )
            Text(
                text = dishState.dish.name,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
            )
            CountDish(
                Modifier.padding(16.dp, 8.dp),
                count = dishState.count,
                changeCount
            )
            Text(
                dishState.dish.description,
                modifier = Modifier.padding(8.dp)
            )
        }
        TotalPrice(
            price = dishState.dish.price,
             click = {
                interpret.complete(
                    DishScreenEvent.ClickAddingToBasket(
                        dish = dishState.dish,
                        dishState.count
                    )
                )
            }
        )
    }
}

@Composable
fun CountDish(
    modifier: Modifier = Modifier,
    count: Int,
    changeCount: ChangeCount
) {
    Surface(
        color = DetailDishColor,
        shape = RoundedCornerShape(8.dp),
        modifier = modifier
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Button(
                onClick = { changeCount.decrease() },
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color.Transparent,
                    contentColor = Color.White
                ),
                elevation = ButtonDefaults.elevation(
                    defaultElevation = 0.dp,
                    pressedElevation = 2.dp
                ),
                contentPadding = PaddingValues(4.dp, 2.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_remove),
                    contentDescription = "remove",
                    tint = Color.White.copy(alpha = 0.65f)
                )
            }
            Text(
                count.toString(),
                color = Color.White,
                fontSize = 24.sp,
                modifier = Modifier.padding(8.dp, 0.dp)
            )
            Button(
                onClick = { changeCount.increase() },
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color.Transparent,
                    contentColor = Color.White
                ),
                elevation = ButtonDefaults.elevation(
                    defaultElevation = 0.dp,
                    pressedElevation = 2.dp
                )
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_add),
                    contentDescription = "add",
                    tint = Color.White.copy(alpha = 0.65f)
                )
            }
        }
    }
}

@Composable
fun TotalPrice(
    modifier: Modifier = Modifier,
    price: Double,
    click: () -> Unit
) {
    Surface(
        color = DetailDishColor,
        shape = dishShip,
        modifier = modifier
    ) {
        Row(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = modifier
                    .weight(1f)
                    .alignByBaseline()
            ) {
                Text(
                    price.toString(),
                    color = Color.White,
                    fontSize = 16.sp
                )
                Spacer(modifier = Modifier.width(4.dp))
                Icon(
                    painter = painterResource(id = R.drawable.ic_ruble_currency_sign),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(0.dp, 2.dp)
                        .width(16.dp)
                        .height(16.dp),
                    tint = Color.White
                )
            }
            Button(
                onClick = {
                    Log.d("DishScreen", "click add to basket")
                    click()
                },
                modifier = Modifier.alignByBaseline(),
                shape = BtnShape,
                contentPadding = PaddingValues(
                    start = 32.dp,
                    top = 8.dp,
                    end = 32.dp,
                    bottom = 8.dp
                ),
                colors = buttonColors(
                    backgroundColor = DetailDishColor,
                    contentColor = Color.White
                ),
                border = BorderStroke(2.dp, selectedTabColor)
            ) {
                Text("В корзину")
            }
        }
    }
}

fun State<DishState>.dishChangeCount(action: Int) =
    this.value.copy(count = this.value.count + action * 1)