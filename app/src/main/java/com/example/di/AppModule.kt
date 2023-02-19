package com.example.di

import android.content.Context
import androidx.room.Room
import com.example.other.Constants.BASE_URL
import com.example.other.Constants.DATABASE_NAME
import com.example.shopping_repository.DefaultShoppingRepository
import com.example.shopping_repository.ShoppingRepository
import com.example.testingdemo.data.local.ShoppingDao
import com.example.testingdemo.data.local.ShoppingDataBase
import com.example.testingdemo.data.remote.PixaBayAPI
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun providesShoppingItemDatabase(@ApplicationContext applicationContext: Context): ShoppingDataBase {

        return Room.databaseBuilder(applicationContext, ShoppingDataBase::class.java, DATABASE_NAME)
            .build()
    }

    @Singleton
    @Provides
    fun providesShoppingDao(dataBase: ShoppingDataBase): ShoppingDao {
        return dataBase.shoppingDao()
    }

    @Singleton
    @Provides
    fun providesPixaBayApi(): PixaBayAPI {
        return Retrofit.Builder().addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
            .create(PixaBayAPI::class.java)
    }

    @Singleton
    @Provides
    fun provideDefaultShoppingRepository(dao: ShoppingDao, api: PixaBayAPI): ShoppingRepository {
        return DefaultShoppingRepository(dao, api)
    }
}