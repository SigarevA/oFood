package ru.vsu.ofood.ui

import android.util.Log
import android.widget.CalendarView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import ru.vsu.ofood.R
import ru.vsu.ofood.di.ComponentHolder
import ru.vsu.ofood.ui.basket.BasketScreen
import ru.vsu.ofood.ui.detail.DetailDishScreen
import ru.vsu.ofood.ui.home.HomeScreen
import ru.vsu.ofood.ui.info.CityMapView
import ru.vsu.ofood.ui.info.InfoScreen
import ru.vsu.ofood.ui.order_registration.OrderRegistrationScreen
import ru.vsu.ofood.ui.theme.OFoodTheme
import ru.vsu.ofood.ui.vms.BasketViewModel
import ru.vsu.ofood.ui.vms.HomeTabVM
import ru.vsu.ofood.ui.vms.InfoTabVm
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

@Composable
fun AppContent(content: @Composable () -> Unit) {
    OFoodTheme {
        Surface(color = MaterialTheme.colors.background) {
            content()
        }
    }
}

@Composable
fun MainScreen(vm: MainViewModel) {
    val states = vm.currentTab.collectAsState()
  /*  DatePicker({
        Log.d("MainScreen", "$it")
    }, { }) */
    MainContent(selectedTab = states.value, vm)
}

@Composable
fun MainContent(selectedTab: Tab, vm: MainViewModel) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Surface(
            modifier = Modifier
                .weight(1f)
                .background(Color.Cyan)
        ) {
            when (selectedTab) {
                is Tab.Home -> TabHome(vm)
                is Tab.Info -> TabInfo(vm)
                is Tab.Basket -> TabBasket(vm)
            }
        }
        AppContainer(selectedTab, vm::navigate)
    }
}

@Composable
fun AppContainer(selectedTab: Tab, navigate: (Tab) -> Unit) {
    BottomNavigation(backgroundColor = Color.White) {
        tabs.forEach { tab ->
            BottomNavigationItem(
                selected = selectedTab == tab,
                onClick = { navigate(tab) },
                icon = {
                    Icon(
                        painter = painterResource(id = tab.icon),
                        contentDescription = null
                    )
                },
                label = {
                    Text(stringResource(id = tab.label))
                }
            )
        }
    }
}

@Composable
fun TabHome(mainViewModel: MainViewModel) {
    val tabHomeViewModel: HomeTabVM = viewModel()
    mainViewModel.backListener = tabHomeViewModel
    val screenState = tabHomeViewModel.screenState.collectAsState()
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        TabHomeContent(currentState = screenState.value)
    }
}

@Composable
fun TabHomeContent(
    currentState: AppState
) {

    when (currentState) {
        is AppState.HomeScreen -> {
            HomeScreen(homeScreenMiddleware = ComponentHolder.homeTabComponent!!.getHomeScreenMiddleware())
        }
        is AppState.DishDetail -> {
            DetailDishScreen(
                currentState.dish,
                ComponentHolder.homeTabComponent!!.getDishScreenMiddleware()
            )
        }
    }
}

@Composable
fun TabInfoContent(
    infoDestination: InfoDestination,
    navigate: (NavigationOperation) -> Unit
) {
    Log.d("TabInfoContent", "${infoDestination}")
    when (infoDestination) {
        is InfoDestination.Contacts -> {
            Log.d("TabInfoContent", "is Contacts")
            InfoScreen(navigate)
        }
        is InfoDestination.Localization -> {
            Log.d("TabInfoContent", "is Localization")
            CityMapView(infoDestination.coordinate, navigate)
        }
    }
}

@Composable
fun TabInfo(mainViewModel: MainViewModel) {
    val infoTabVM: InfoTabVm = viewModel()
    val screenState = infoTabVM.screenState.collectAsState()
    mainViewModel.backListener = infoTabVM
    TabInfoContent(infoDestination = screenState.value, infoTabVM::navigate)
}

@Composable
fun TabBasketContent(stateTab: BasketDestination, basketNavigate: (NavigationOperation) -> Unit) {
    when (stateTab) {
        is BasketDestination.Basket -> BasketScreen(basketNavigate)
        is BasketDestination.OrderRegistration -> OrderRegistrationScreen(basketNavigate)
    }
}

@Composable
fun TabBasket(mainViewModel: MainViewModel) {
    val basketViewModel: BasketViewModel = viewModel()
    mainViewModel.backListener = basketViewModel
    val stateTab = basketViewModel.screenState.collectAsState()
    TabBasketContent(stateTab = stateTab.value, basketViewModel::navigate)
}


@Composable
fun DatePicker(onDateSelected: (Calendar) -> Unit, onDismissRequest: () -> Unit) {
    val selDate = remember { mutableStateOf(Calendar.getInstance()) }

    Dialog(onDismissRequest = { onDismissRequest() }, properties = DialogProperties()) {
        Column(
            modifier = Modifier
                .wrapContentSize()
                .background(
                    color = MaterialTheme.colors.surface,
                    shape = RoundedCornerShape(size = 16.dp)
                )
        ) {
            Column(
                Modifier
                    .defaultMinSize(minHeight = 72.dp)
                    .fillMaxWidth()
                    .background(
                        color = MaterialTheme.colors.primary,
                        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
                    )
                    .padding(16.dp)
            ) {
                Text(
                    text = stringResource(R.string.choose_date).toUpperCase(Locale("ru")),
                    style = MaterialTheme.typography.caption,
                    color = MaterialTheme.colors.onPrimary
                )
                Spacer(modifier = Modifier.size(24.dp))
            }

            CustomCalendarView(onDateSelected = {
                selDate.value = it
            })

            Spacer(modifier = Modifier.size(8.dp))
            Text(
                text = "Cancel",
                style = MaterialTheme.typography.button,
                color = MaterialTheme.colors.onPrimary
            )
            Row(
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(bottom = 16.dp, end = 16.dp)
            ) {
                TextButton(
                    onClick = onDismissRequest
                ) {
                    Text(
                        text = stringResource(id = R.string.cancel)
                    )
                }

                TextButton(
                    onClick = {
                        onDateSelected(selDate.value)
                        onDismissRequest()
                    }
                ) {
                    Text(
                        text = stringResource(id = R.string.confirmation)
                    )
                }

            }
        }
    }
}

@Composable
fun CustomCalendarView(onDateSelected: (Calendar) -> Unit) {
    AndroidView(
        modifier = Modifier.wrapContentSize(),
        factory = { context ->
            CalendarView(context)
        },
        update = { view ->
            view.minDate = Calendar.getInstance().timeInMillis

            view.setOnDateChangeListener { _, year, month, dayOfMonth ->
                Log.d("AndroidView", "year : $year, month : $month, dayOfMonth : $dayOfMonth")
                val calendar = Calendar.getInstance()
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, month)
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                onDateSelected(calendar)
            }
        }
    )
}