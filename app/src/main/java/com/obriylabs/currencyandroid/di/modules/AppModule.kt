package com.obriylabs.currencyandroid.di.modules

import android.app.Application
import android.arch.persistence.room.Room
import android.content.Context
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.obriylabs.currencyandroid.CurrencyExchangeApp
import com.obriylabs.currencyandroid.data.api.ExchangersService
import com.obriylabs.currencyandroid.data.room.ExchangersDb
import com.obriylabs.currencyandroid.data.room.ExchangersDao
import com.obriylabs.currencyandroid.data.repository.IExchangersRepository
import com.obriylabs.currencyandroid.data.repository.ExchangersRepositoryImpl
import com.obriylabs.currencyandroid.domain.SecretKeys
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module(includes = [ViewModelModule::class])
class AppModule(private val application: CurrencyExchangeApp) {

    @Provides
    @Singleton
    fun provideApplicationContext(): Context = application

    @Provides
    @Singleton
    fun provideExchangersService(interceptor: OkHttpClient): ExchangersService {
        return Retrofit.Builder()
                .baseUrl(SecretKeys.getUrl())
                .client(interceptor)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .build()
                .create(ExchangersService::class.java)
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        return OkHttpClient.Builder().addInterceptor(interceptor).build()
    }

    @Provides
    @Singleton
    fun provideDb(app: Application): ExchangersDb {
        return Room
                .databaseBuilder(app, ExchangersDb::class.java, "exchangers.db")
                .fallbackToDestructiveMigration()
                .build()
    }


    @Provides
    @Singleton
    fun provideExchangersDao(db: ExchangersDb): ExchangersDao = db.exchangersDao()

    @Provides
    @Singleton
    fun provideNetworkRepository(dataSource: ExchangersRepositoryImpl): IExchangersRepository = dataSource

}