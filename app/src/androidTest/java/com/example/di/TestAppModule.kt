package com.example.di

import android.content.Context
import androidx.room.Room
import com.example.testingdemo.data.local.ShoppingDataBase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Named

@Module
@InstallIn(SingletonComponent::class)
object TestAppModule {

    @Provides
    @Named("TestDB")
    fun providesInMemoryDatabase(@ApplicationContext context: Context): ShoppingDataBase {
        return Room.inMemoryDatabaseBuilder(context, ShoppingDataBase::class.java).allowMainThreadQueries()
            .build()
    }
}