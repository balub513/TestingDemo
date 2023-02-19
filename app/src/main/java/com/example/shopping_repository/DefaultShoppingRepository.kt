package com.example.shopping_repository

import androidx.lifecycle.LiveData
import com.example.other.Resource
import com.example.testingdemo.data.local.ShoppingDao
import com.example.testingdemo.data.local.ShoppingItem
import com.example.testingdemo.data.remote.ImageResponse
import com.example.testingdemo.data.remote.PixaBayAPI
import java.lang.reflect.Executable
import javax.inject.Inject

class DefaultShoppingRepository @Inject constructor(
    private val shoppingDao: ShoppingDao,
    private val pixaBayAPI: PixaBayAPI
) : ShoppingRepository {
    override suspend fun insertItem(item: ShoppingItem) {
        shoppingDao.insertItem(item)
    }

    override suspend fun deleteItem(item: ShoppingItem) {
        shoppingDao.deleteItem(item)
    }

    override fun getAllObservableItems(): LiveData<List<ShoppingItem>> {
        return shoppingDao.getAllObservableItems()
    }

    override fun getTotalPriceObservable(): LiveData<Float> {
        return shoppingDao.getItemsTotalPrice()
    }

    override suspend fun searchForImage(query: String): Resource<ImageResponse> {
        return try {
            var response = pixaBayAPI.searchForImage(query)
            if (response.isSuccessful) {
                response.body()?.let {
                    return@let Resource.success(it)
                } ?: Resource.error("image search error", null)
            } else {
                Resource.error("image search error", null)
            }
        } catch (e: Exception) {
            Resource.error("error in image search", null)
        }
    }

}