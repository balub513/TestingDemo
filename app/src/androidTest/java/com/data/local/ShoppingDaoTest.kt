package com.data.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.testingdemo.data.local.ShoppingDao
import com.example.testingdemo.data.local.ShoppingDataBase
import com.example.testingdemo.data.local.ShoppingItem
import com.getOrAwaitValue
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class ShoppingDaoTest {

    private lateinit var dao: ShoppingDao
    private lateinit var shoppingDataBase: ShoppingDataBase

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        shoppingDataBase = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            ShoppingDataBase::class.java
        ).allowMainThreadQueries().build()

        dao = shoppingDataBase.shoppingDao()
    }

    @After
    fun tearDown() {
        shoppingDataBase.close()
    }

    @Test
    fun testInsertShoppingItem() {
        runBlockingTest {
            val item = ShoppingItem("Banana", 10, 5, "imageUrl", 1)
            dao.insertItem(item)
            val itemList = dao.getAllObservableItems().getOrAwaitValue()
            assertThat(itemList).contains(item)
        }
    }

    @Test
    fun testDeleteShoppingItem() {
        runBlocking {
            val item1 = ShoppingItem("Banana", 10, 5, "imageUrl", 1)
            val item2 = ShoppingItem("Apple", 10, 5, "imageUrl", 2)
            val item3 = ShoppingItem("grape", 10, 5, "imageUrl", 3)
            dao.insertItem(item1)
            dao.insertItem(item2)
            dao.insertItem(item3)
            dao.deleteItem(item2)
            val itemList = dao.getAllObservableItems().getOrAwaitValue()
            assertThat(itemList).hasSize(2)
        }
    }

    @Test
    fun testTotalItemsPrice(){
        runBlocking {
            val item1 = ShoppingItem("Banana", 10, 5, "imageUrl", 1)
            val item2 = ShoppingItem("Apple", 10, 10, "imageUrl", 2)
            val item3 = ShoppingItem("grape", 10, 10, "imageUrl", 3)
            dao.insertItem(item1)
            dao.insertItem(item2)
            dao.insertItem(item3)

            val itemsTotalPrice = dao.getItemsTotalPrice().getOrAwaitValue {  }
            assertThat(itemsTotalPrice).isEqualTo(250)
        }
    }

}