package ru.vsu.ofood.ui.vms

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import ru.vsu.ofood.ui.BasketDestination
import ru.vsu.ofood.ui.InfoDestination
import ru.vsu.ofood.ui.NavigationOperation
import ru.vsu.ofood.ui.OnBackListener
import java.lang.IllegalArgumentException

class InfoTabVm : ViewModel(), OnBackListener {
    private val mutableScreenState = MutableStateFlow<InfoDestination>(InfoDestination.Contacts)
    val screenState: StateFlow<InfoDestination>
        get() = mutableScreenState.asStateFlow()

    private val backStack: MutableList<InfoDestination> = ArrayList()

    fun navigate(operation: NavigationOperation) {
        when (operation) {
            is NavigationOperation.Back -> {
                val prevState = backStack.removeLast()
                mutableScreenState.value = prevState
            }
            is NavigationOperation.Destination -> {
                when(operation.screen) {
                    is InfoDestination -> {
                        backStack.add(screenState.value)
                        mutableScreenState.value = operation.screen
                    }
                    else -> throw IllegalArgumentException()
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
}