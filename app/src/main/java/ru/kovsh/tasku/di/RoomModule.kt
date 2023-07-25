package ru.kovsh.tasku.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ru.kovsh.tasku.models.local.AreaDao
import ru.kovsh.tasku.models.local.TasksDao
import ru.kovsh.tasku.models.tasks.TaskuRoomDatabase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {
    @Provides
    @Singleton
    fun provideRoomDatabase(@ApplicationContext context: Context): TaskuRoomDatabase {
        return Room.databaseBuilder(
            context,
            TaskuRoomDatabase::class.java,
            "database"
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    @Singleton
    fun provideTasksDao(database: TaskuRoomDatabase): TasksDao {
        return database.tasksDao()
    }

    @Provides
    @Singleton
    fun provideAreasDao(database: TaskuRoomDatabase): AreaDao {
        return database.areasDao()
    }

}