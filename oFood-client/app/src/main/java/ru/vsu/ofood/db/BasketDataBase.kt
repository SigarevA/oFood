package ru.vsu.ofood.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ru.vsu.ofood.entities.DishInBasket

@Database(
    entities = [DishInBasket::class],
    version = 1
)
abstract class BasketDataBase : RoomDatabase() {
    abstract fun dishInBasketDao(): DishInBasketDao

    companion object {
        @Volatile
        private var INSTANCE: BasketDataBase? = null

        fun getInstance(applicationContext: Context): BasketDataBase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    applicationContext,
                    BasketDataBase::class.java,
                    "BasketDataBase"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }
}