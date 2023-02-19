package com.example.viewmodel


import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.MainCoroutineRule
import com.example.other.Constants
import com.example.other.Status
import com.example.shopping_repository.FakeShoppingRepository
import com.getOrAwaitValueTest
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test


@ExperimentalCoroutinesApi
class ShoppingViewModelTest {
    lateinit var viewModel: ShoppingViewModel

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @Before
    fun init() {
        viewModel = ShoppingViewModel(FakeShoppingRepository())
    }

    @Test
    fun `insert shopping item with empty field returns error`() {
        viewModel.insertShoppingItem("balu", "", "45")
        val result = viewModel.insertShoppingItemsStatus.getOrAwaitValueTest()
        assertThat(result.getContentIfNotHandled()?.status).isEqualTo(Status.ERROR)
    }

    @Test
    fun `insert shopping item with too long name return error`() {
        val name = buildString {
            (1..Constants.MAX_NAME_LENGTH + 1).forEach { _ ->
                append(1)
            }
        }
        viewModel.insertShoppingItem(name, "67", "99")
        val result = viewModel.insertShoppingItemsStatus.getOrAwaitValueTest { }
        assertThat(result.getContentIfNotHandled()?.status).isEqualTo(Status.ERROR)
    }

    @Test
    fun `insert shopping item with too long price return error`() {
        val price = buildString {
            (1..Constants.MAX_PRICE_LENGTH + 1).forEach { _ ->
                append(1)
            }
        }
        viewModel.insertShoppingItem("balu", price, "99")
        val result = viewModel.insertShoppingItemsStatus.getOrAwaitValueTest { }
        assertThat(result.getContentIfNotHandled()?.status).isEqualTo(Status.ERROR)
    }

    @Test
    fun `insert shopping item with too high amount return error`() {
        viewModel.insertShoppingItem("Grape", "43243243243243243299S67", "99")
        val result = viewModel.insertShoppingItemsStatus.getOrAwaitValueTest { }
        assertThat(result.getContentIfNotHandled()?.status).isEqualTo(Status.ERROR)
    }

    @Test
    fun `insert shopping item with valid input return success`() {
        viewModel.insertShoppingItem("Grape", "45", "99")
        val result = viewModel.insertShoppingItemsStatus.getOrAwaitValueTest { }
        assertThat(result.getContentIfNotHandled()?.status).isEqualTo(Status.SUCCESS)
    }
}