package com.example.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.other.Constants
import com.example.other.Event
import com.example.other.Resource
import com.example.shopping_repository.ShoppingRepository
import com.example.testingdemo.data.local.ShoppingItem
import com.example.testingdemo.data.remote.ImageResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShoppingViewModel @Inject constructor(val repository: ShoppingRepository) :
    ViewModel() {

    val shoppingItems = repository.getAllObservableItems()
    val price = repository.getTotalPriceObservable()

    private var _imagesLiveData = MutableLiveData<Event<Resource<ImageResponse>>>()
    val imagesLiveData: LiveData<Event<Resource<ImageResponse>>> = _imagesLiveData

    private var _currentImageUrlLiveData = MutableLiveData<String>()
    var currentImageUrlLiveData: LiveData<String> = _currentImageUrlLiveData

    private var _insertShoppingItemsStatus = MutableLiveData<Event<Resource<ShoppingItem>>>()
    var insertShoppingItemsStatus: LiveData<Event<Resource<ShoppingItem>>> =
        _insertShoppingItemsStatus

    fun setCurrentImageUrl(url: String) {
        _currentImageUrlLiveData.postValue(url)
    }

    fun deleteShoppingItem(shoppingItem: ShoppingItem) {
        viewModelScope.launch {
            repository.deleteItem(shoppingItem)
        }
    }

    fun insertShoppingItem(shoppingItem: ShoppingItem) {
        viewModelScope.launch {
            repository.insertItem(shoppingItem)
        }
    }

    fun insertShoppingItem(name: String, amountStr: String, priceStr: String) {
        if (name.isEmpty() || amountStr.isEmpty() || priceStr.isEmpty()) {
            _insertShoppingItemsStatus.postValue(Event(Resource.error("Empty value", null)))
            return
        }
        if (name.length > Constants.MAX_NAME_LENGTH) {
            _insertShoppingItemsStatus.postValue(
                Event(
                    Resource.error(
                        "Name must not exceeds max length: ${Constants.MAX_NAME_LENGTH}",
                        null
                    )
                )
            )
            return
        }
        if (priceStr.length > Constants.MAX_PRICE_LENGTH) {
            _insertShoppingItemsStatus.postValue(
                Event(
                    Resource.error(
                        "Price must not exceeds max length : ${Constants.MAX_PRICE_LENGTH}",
                        null
                    )
                )
            )
            return
        }
        val amount = try {
            amountStr.toInt()
        } catch (e: Exception) {
            _insertShoppingItemsStatus.postValue(
                Event(
                    Resource.error(
                        "Price must be valid amount",
                        null
                    )
                )
            )
            return

        }
        val shoppingItem =
            ShoppingItem(name, amount, priceStr.toInt(), _currentImageUrlLiveData.value ?: "")
        insertShoppingItem(shoppingItem)
        setCurrentImageUrl("")
        _insertShoppingItemsStatus.postValue(Event(Resource.success(shoppingItem)))
    }

    fun searchForImage(query: String) {
        if (query.isEmpty()) {
            return
        }
        _imagesLiveData.value = Event(Resource.loading(null))
        viewModelScope.launch {
            val response = repository.searchForImage(query)
            _imagesLiveData.value = Event(response)
        }
    }

}