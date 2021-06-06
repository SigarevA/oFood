package ru.vsu.ofood.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import ru.vsu.ofood.di.ComponentHolder
import ru.vsu.ofood.di.modules.BasketModule
import ru.vsu.ofood.di.modules.InfoModule
import ru.vsu.ofood.domain.CategoryRepo
import ru.vsu.ofood.domain.DishRepo
import ru.vsu.ofood.entities.Dish
import ru.vsu.ofood.ui.home.HomeScreenMiddleware

private const val TAG = "MainViewModel"

class MainViewModel : ViewModel() {

    init {
        ComponentHolder.infoComponent = ComponentHolder.appComponent.plusInfoComponent(
            InfoModule(::onEffects)
        )
    }

    private val mutableCurrentTab = MutableStateFlow<Tab>( Tab.Home )
    private val _events = MutableSharedFlow<Event>()
    val events = _events.asSharedFlow()

    private fun onEffects(event: Event) {
        viewModelScope.launch {
            _events.emit(event)
        }
    }

    var backListener: OnBackListener? = null

    val currentTab: StateFlow<Tab>
        get() = mutableCurrentTab.asStateFlow()

    fun navigate(tab: Tab) {
        if (mutableCurrentTab.value != tab)
            mutableCurrentTab.value = tab
    }

    fun back(): Boolean {
        if (backListener != null && !backListener!!.back())
            return false
        else if (mutableCurrentTab.value != Tab.Home) {
            mutableCurrentTab.value = Tab.Home
            return false
        } else
            return true

    }
}

sealed class Destination() {
    class DetailDish(val dish: Dish) : Destination()
    object Back : Destination()
}