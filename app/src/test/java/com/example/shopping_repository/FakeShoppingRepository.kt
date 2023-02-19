package com.example.shopping_repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.other.Resource
import com.example.shopping_repository.ShoppingRepository
import com.example.testingdemo.data.local.ShoppingItem
import com.example.testingdemo.data.remote.ImageResponse

class FakeShoppingRepository : ShoppingRepository {

    private var shoppingItems = mutableListOf<ShoppingItem>()

    private var liveDataShoppingItemsList = MutableLiveData<List<ShoppingItem>>()

    private var liveDataTotalPrice = MutableLiveData<Float>()

    private var shouldReturnNetworkError = false

    fun setShouldReturnNetworkError(netWorkErr: Boolean) {
        shouldReturnNetworkError = netWorkErr
    }

    private fun getTotalPrice(): Float {
        return shoppingItems.sumOf { it.price * it.amount }.toFloat()
    }

    private fun refreshLiveData() {
        liveDataShoppingItemsList.postValue(shoppingItems)
        liveDataTotalPrice.postValue(getTotalPrice())
    }

    override suspend fun insertItem(item: ShoppingItem) {
        shoppingItems.add(item)
        refreshLiveData()
    }

    override suspend fun deleteItem(item: ShoppingItem) {
        shoppingItems.remove(item)
        refreshLiveData()
    }

    override fun getAllObservableItems(): LiveData<List<ShoppingItem>> {
        return liveDataShoppingItemsList
    }

    override fun getTotalPriceObservable(): LiveData<Float> {
        return liveDataTotalPrice
    }

    override suspend fun searchForImage(query: String): Resource<ImageResponse> {
        return if (!shouldReturnNetworkError) {
            Resource.success(ImageResponse(listOf(), 0, 0))
        } else {
            Resource.error("error", null)
        }
    }
}