package ru.vsu.oFoodAdmin.ui

import android.os.Bundle
import androidx.compose.material.ScaffoldState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ru.vsu.oFoodAdmin.di.ComponentHolder
import ru.vsu.oFoodAdmin.entities.Order
import ru.vsu.oFoodAdmin.ui.addingdish.AddingDishScreen
import ru.vsu.oFoodAdmin.ui.addingpromotion.AddingPromotionScreen
import ru.vsu.oFoodAdmin.ui.detailorder.DetailOrderScreen
import ru.vsu.oFoodAdmin.ui.home.HomeScreen
import ru.vsu.oFoodAdmin.ui.order.OrderScreen
import ru.vsu.oFoodAdmin.ui.promotions.PromotionScreen


object MainDestinations {
    const val HOME_ROUTE = "home"
    const val PROMOTION_ROUTE = "promotion"
    const val ADDING_PROMOTION = "addingPromotion"
    const val ORDERS_ROUTE = "orders"
    const val ORDER_ID = "detail_id"
    const val DISH_ROUTE = "dish_route"
}

@Composable
fun FoodAdminNavGraph(
    navController: NavHostController = rememberNavController(),
    scaffoldState: ScaffoldState = rememberScaffoldState(),
    startDestination: String = MainDestinations.HOME_ROUTE
) {
    val actions = remember(navController) { MainActions(navController) }
    val coroutineScope = rememberCoroutineScope()

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(MainDestinations.HOME_ROUTE) {
            HomeScreen(actions)
        }
        composable(MainDestinations.PROMOTION_ROUTE) {
            PromotionScreen(promotionRepo = ComponentHolder.appComponent.getPromotionRepo()) { actions.navigateToAddingPromotion() }
        }
        composable("${MainDestinations.PROMOTION_ROUTE}/${MainDestinations.ADDING_PROMOTION}") {
            AddingPromotionScreen(
                dishRepo = ComponentHolder.appComponent.getDishRepo(),
                promotionRepo = ComponentHolder.appComponent.getPromotionRepo()
            )
        }
        composable(MainDestinations.ORDERS_ROUTE) {
            OrderScreen(
                orderRepo = ComponentHolder.appComponent.getOrderRepo(),
                actions.navigateToDetailOrder
            )
        }
        composable("${MainDestinations.ORDERS_ROUTE}/{${MainDestinations.ORDER_ID}}") { navBackStackEntry ->
            DetailOrderScreen(
                orderId = navBackStackEntry.arguments!!.getString(MainDestinations.ORDER_ID),
                orderRepo = ComponentHolder.appComponent.getOrderRepo(),
                actions.upPress
            )
        }
        composable(MainDestinations.DISH_ROUTE) {
            AddingDishScreen(
                ComponentHolder.appComponent.getCategoryRepo(),
                ComponentHolder.appComponent.getDishRepo()
            )
        }
    }
}

class MainActions(navController: NavHostController) {
    val navigateToAddingPromotion: () -> Unit = {
        navController.navigate("${MainDestinations.PROMOTION_ROUTE}/${MainDestinations.ADDING_PROMOTION}")
    }
    val navigateToPromotion: () -> Unit = {
        navController.navigate(MainDestinations.PROMOTION_ROUTE)
    }
    val navigateToDetailOrder: (Order) -> Unit = { order ->
        navController.navigate("${MainDestinations.ORDERS_ROUTE}/${order.id}")
    }
    val navigateToOrders: () -> Unit = {
        navController.navigate(MainDestinations.ORDERS_ROUTE)
    }
    val upPress: () -> Unit = {
        navController.navigateUp()
    }
    val navigateToDish: () -> Unit = {
        navController.navigate(MainDestinations.DISH_ROUTE)
    }
}