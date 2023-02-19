package com.example.shopping_repository

import androidx.lifecycle.LiveData
import com.example.other.Resource
import com.example.testingdemo.data.local.ShoppingItem
import com.example.testingdemo.data.remote.ImageResponse
import retrofit2.http.Query

interface ShoppingRepository {

    suspend fun insertItem(item: ShoppingItem)

    suspend fun deleteItem(item: ShoppingItem)

    fun getAllObservableItems(): LiveData<List<ShoppingItem>>

    fun getTotalPriceObservable(): LiveData<Float>

    suspend fun searchForImage(query: String): Resource<ImageResponse>

}