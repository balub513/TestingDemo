package com.example.testingdemo.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [ShoppingItem::class],
    version = 1, exportSchema = true
)
abstract class ShoppingDataBase : RoomDatabase() {

    abstract fun shoppingDao(): ShoppingDao
}