package ru.vsu.oFoodAdmin.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.vsu.oFoodAdmin.ui.MainActions

@Composable
fun HomeScreen(mainActions : MainActions) {
    Column(modifier = Modifier.padding(16.dp)) {
        ManageButton("Акции", Modifier.padding(vertical = 16.dp)
        ) {
            mainActions.navigateToPromotion() }
        ManageButton("Заказы", Modifier.padding(vertical = 16.dp)) {
            mainActions.navigateToOrders()
        }
        ManageButton("Товары", Modifier.padding(vertical = 16.dp) ) {
            mainActions.navigateToDish()
        }
    }
}

@Composable
fun ManageButton(
    name : String,
    modifier: Modifier = Modifier,
    onCLick : () -> Unit
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .height(100.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Button(
            onClick = { onCLick() },
            modifier = Modifier.fillMaxSize()
        ) {
            Text(name)
        }
    }
}