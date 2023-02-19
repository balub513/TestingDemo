package com.example.testingdemo.data.local

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ShoppingDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItem(shoppingItem: ShoppingItem)

    @Delete
    suspend fun deleteItem(shoppingItem: ShoppingItem)

    @Query("SELECT * FROM ShoppingItems")
    fun getAllObservableItems(): LiveData<List<ShoppingItem>>

    @Query("SELECT SUM(price * amount)  FROM ShoppingItems")
    fun getItemsTotalPrice(): LiveData<Float>
}