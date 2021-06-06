package ru.vsu.ofood.ui.vms

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import ru.vsu.ofood.di.ComponentHolder
import ru.vsu.ofood.di.modules.HomeModule
import ru.vsu.ofood.ui.AppState
import ru.vsu.ofood.ui.Destination
import ru.vsu.ofood.ui.OnBackListener

class HomeTabVM() : ViewModel(), OnBackListener {

    init {
        ComponentHolder.homeTabComponent = ComponentHolder.appComponent.plusHomeModule(
            HomeModule(
                this::navigate
            )
        )
    }

    private val mutableScreenState = MutableStateFlow<AppState>(AppState.HomeScreen)
    val screenState: StateFlow<AppState>
        get() = mutableScreenState.asStateFlow()

    private val backStack: MutableList<AppState> = ArrayList()

    private fun navigate(destination: Destination) {
        when (destination) {
            is Destination.DetailDish -> {
                backStack.add(mutableScreenState.value)
                mutableScreenState.value = AppState.DishDetail(destination.dish)
            }
            is Destination.Back -> {
                val prevState = backStack.removeLast()
                mutableScreenState.value = prevState
            }
        }
    }

    override fun back(): Boolean {
        if (backStack.size == 0) {
            return true
        }
        navigate(Destination.Back)
        return false
    }
}