package ru.kovsh.tasku.di

import android.app.Application
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import ru.kovsh.tasku.models.mixedRepo.SharedRepository
import ru.kovsh.tasku.models.areas.AreasAPI
import ru.kovsh.tasku.models.areas.AreasRepository
import ru.kovsh.tasku.models.areas.AreasRepositoryImplementation
import ru.kovsh.tasku.models.auth.AuthAPI
import ru.kovsh.tasku.models.auth.AuthInterceptor
import ru.kovsh.tasku.models.auth.AuthRepository
import ru.kovsh.tasku.models.auth.AuthRepositoryImplementation
import ru.kovsh.tasku.models.local.AreaDao
import ru.kovsh.tasku.models.local.TasksDao
import ru.kovsh.tasku.models.mixedRepo.MenuCategoryRepository
import ru.kovsh.tasku.models.tasks.TaskAPI
import ru.kovsh.tasku.models.tasks.TaskRepository
import ru.kovsh.tasku.models.tasks.TaskRepositoryImplementation
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideSharedPref(app: Application): SharedPreferences {
        return app.getSharedPreferences("pref", MODE_PRIVATE)
    }

    @Provides
    @Singleton
    fun provideAuthApi(): AuthAPI {
        return Retrofit.Builder()
            .baseUrl("temp")
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create()
    }

    @Provides
    @Singleton
    fun provideAuthRepository(api: AuthAPI, prefs: SharedPreferences): AuthRepository {
        return AuthRepositoryImplementation(api, prefs)
    }

    @Provides
    @Singleton
    fun provideHttpClient(authInterceptor: AuthInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .build()
    }

    @Provides
    @Singleton
    fun provideAreaApi(httpClient: OkHttpClient): AreasAPI {
        return Retrofit.Builder()
            .client(httpClient)
            .baseUrl("temp")
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create()
    }

    @Provides
    @Singleton
    fun provideAreasRepository(api: AreasAPI): AreasRepository {
        return AreasRepositoryImplementation(api)
    }

    @Provides
    @Singleton
    fun provideTaskApi(httpClient: OkHttpClient): TaskAPI {
        return Retrofit.Builder()
            .client(httpClient)
            .baseUrl("temp")
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create()
    }

    @Provides
    @Singleton
    fun provideTaskRepository(api: TaskAPI): TaskRepository {
        return TaskRepositoryImplementation(api)
    }

    @Provides
    @Singleton
    fun provideSharedRepository(taskRepository: TaskRepository, tDao: TasksDao, aDao: AreaDao): SharedRepository {
        return SharedRepository(taskRepository, tDao, aDao)
    }

    @Provides
    @Singleton
    fun provideMenuCategoryRepository(taskRepository: TaskRepository, dao: TasksDao): MenuCategoryRepository {
        return MenuCategoryRepository(taskRepository, dao)
    }
}