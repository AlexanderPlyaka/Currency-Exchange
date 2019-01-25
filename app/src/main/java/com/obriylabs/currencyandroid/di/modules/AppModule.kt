package com.obriylabs.currencyandroid.di.modules

import android.app.Application
import android.arch.persistence.room.Room
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.obriylabs.currencyandroid.api.ExchangersService
import com.obriylabs.currencyandroid.db.ExchangersDb
import com.obriylabs.currencyandroid.db.ExchangersDao
import com.obriylabs.currencyandroid.utils.LiveDataCallAdapterFactory
import com.obriylabs.currencyandroid.utils.SecretKeys
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module(includes = [ViewModelModule::class])
class AppModule {

    @Singleton
    @Provides
    fun provideExchangersService(interceptor: OkHttpClient): ExchangersService {
        return Retrofit.Builder()
                .baseUrl(SecretKeys.getUrl())
                .client(interceptor)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(LiveDataCallAdapterFactory())
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .build()
                .create(ExchangersService::class.java)
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        return OkHttpClient.Builder()
                .addInterceptor(interceptor).build()
    }


    @Singleton
    @Provides
    fun provideDb(app: Application): ExchangersDb {
        return Room
                .databaseBuilder(app, ExchangersDb::class.java, "exchangers.db")
                .fallbackToDestructiveMigration()
                .build()
    }

    @Singleton
    @Provides
    fun provideExchangersDao(db: ExchangersDb): ExchangersDao {
        return db.exchangersDao()
    }
}