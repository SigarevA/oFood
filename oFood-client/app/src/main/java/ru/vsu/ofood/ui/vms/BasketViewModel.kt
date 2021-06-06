package ru.vsu.ofood.ui.vms

import android.util.Log
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import ru.vsu.ofood.di.ComponentHolder
import ru.vsu.ofood.di.modules.BasketModule
import ru.vsu.ofood.ui.BasketDestination
import ru.vsu.ofood.ui.NavigationOperation
import ru.vsu.ofood.ui.OnBackListener

private const val TAG = "BasketViewModel"

class BasketViewModel : ViewModel(), OnBackListener {

    init {
        ComponentHolder.basketTabComponent =
            ComponentHolder.appComponent.plusBasketModule(BasketModule())
    }

    private val mutableScreenState = MutableStateFlow<BasketDestination>(BasketDestination.Basket)
    val screenState: StateFlow<BasketDestination>
        get() = mutableScreenState.asStateFlow()

    private val backStack: MutableList<BasketDestination> = ArrayList()

    fun navigate(operation: NavigationOperation) {
        when (operation) {
            is NavigationOperation.Back -> {
                val prevState = backStack.removeLast()
                mutableScreenState.value = prevState
            }
            is NavigationOperation.Destination -> {
                when(operation.screen) {
                    is BasketDestination.OrderRegistration -> {
                        backStack.add(screenState.value)
                        mutableScreenState.value = BasketDestination.OrderRegistration
                    }
                }
            }
        }
    }

    override fun back(): Boolean {
        if (backStack.size == 0) {
            return true
        }
        navigate(NavigationOperation.Back)
        return false
    }

    override fun onCleared() {
        Log.d(TAG, "clear view Model")
        super.onCleared()
    }
}