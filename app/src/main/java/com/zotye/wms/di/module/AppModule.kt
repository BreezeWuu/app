package com.zotye.wms.di.module

import android.app.Application
import android.arch.persistence.room.Room
import android.content.Context
import com.zotye.wms.AppConstants
import com.zotye.wms.di.qualifier.ApplicationContext
import com.zotye.wms.di.qualifier.PreferenceInfo
import com.zotye.wms.data.AppDataManager
import com.zotye.wms.data.DataManager
import com.zotye.wms.data.api.ApiHelper
import com.zotye.wms.data.api.AppApiHelper
import com.zotye.wms.data.api.interceptor.HeaderInterceptor
import com.zotye.wms.data.api.interceptor.HttpLoggingInterceptor
import com.zotye.wms.data.api.service.ApiService
import com.zotye.wms.data.db.AppDatabase
import com.zotye.wms.data.db.AppDbHelper
import com.zotye.wms.data.db.DbHelper
import com.zotye.wms.data.prefs.AppPreferencesHelper
import com.zotye.wms.data.prefs.PreferencesHelper
import com.zotye.wms.BuildConfig
import com.zotye.wms.data.api.interceptor.ResponseInterceptor
import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

/**
 * Created by hechuangju on 2017/7/17.
 */
@Module
class AppModule {

    companion object {
        const val TIMEOUT_IN_SEC = 25
    }

    @Provides
    @ApplicationContext
    fun provideContext(application: Application): Context {
        return application
    }

    @Provides
    @Singleton
    @Named("headerInterceptor")
    fun provideHeaderInterceptor(headerInterceptor: HeaderInterceptor): Interceptor = headerInterceptor

    @Provides
    @Singleton
    @Named("responseInterceptor")
    fun provideResponseInterceptor(responseInterceptor: ResponseInterceptor): Interceptor = responseInterceptor

    @Provides
    @Singleton
    @Named("httpLoggingInterceptor")
    fun provideHttpLoggingInterceptor(): Interceptor = HttpLoggingInterceptor().setLevel(if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE)

    @Provides
    @Singleton
    fun provideHttpClient(@Named("headerInterceptor") headerInterceptor: Interceptor,
                          @Named("responseInterceptor") responseInterceptor: Interceptor,
                          @Named("httpLoggingInterceptor") httpLoggingInterceptor: Interceptor): OkHttpClient {
        return OkHttpClient.Builder()
                .readTimeout(TIMEOUT_IN_SEC.toLong(), TimeUnit.SECONDS)
                .writeTimeout(TIMEOUT_IN_SEC.toLong(), TimeUnit.SECONDS)
                .connectTimeout(TIMEOUT_IN_SEC.toLong(), TimeUnit.SECONDS)
                .addInterceptor(headerInterceptor)
                .addInterceptor(responseInterceptor)
                .addNetworkInterceptor(httpLoggingInterceptor)
                .build()
    }

    @Provides
    @Singleton
    fun provideService(okHttpClient: OkHttpClient): ApiService {
        val retrofit: Retrofit = Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(BuildConfig.API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        return retrofit.create(ApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideApiService(apiHelper: AppApiHelper): ApiHelper = apiHelper

    @Singleton
    @Provides
    fun provideDatabase(app: Application): AppDatabase {
        return Room.databaseBuilder(app, AppDatabase::class.java, AppConstants.DB_NAME).build()
    }

    @Singleton
    @Provides
    fun provideDbHelper(dbHelper: AppDbHelper): DbHelper = dbHelper

    @Provides
    @PreferenceInfo
    internal fun providePreferenceName(): String = AppConstants.PREF_NAME

    @Singleton
    @Provides
    fun providePreferencesHelper(appPreferencesHelper: AppPreferencesHelper): PreferencesHelper = appPreferencesHelper

    @Singleton
    @Provides
    fun provideDataManager(appDataManager: AppDataManager): DataManager = appDataManager

}