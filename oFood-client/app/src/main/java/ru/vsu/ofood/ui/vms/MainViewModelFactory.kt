package ru.vsu.ofood.ui.vms

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.vsu.ofood.domain.CategoryRepo
import ru.vsu.ofood.domain.DishRepo
import ru.vsu.ofood.ui.MainViewModel

class MainViewModelFactory(
    private val categoryRepo: CategoryRepo,
    private val dishRepo: DishRepo
    ) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}