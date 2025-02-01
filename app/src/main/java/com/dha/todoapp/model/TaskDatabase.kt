package com.dha.todoapp.model

import android.content.Context
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import androidx.room.Update
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.internal.synchronized

@Database(entities = [Task::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class TaskDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao

    companion object {
        @Volatile
        private var INSTANCE: TaskDatabase? = null

        @OptIn(InternalCoroutinesApi::class)
        fun getInstance(context: Context): TaskDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TaskDatabase::class.java,
                    "task_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}

@Dao
abstract interface TaskDao {
    @Query("SELECT * FROM tasks")
    suspend fun getAllTask(): List<Task>
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addTodo(task: Task)
    @Update
    suspend fun editTask(task: Task)
    @Query("DELETE FROM tasks WHERE id = :id")
    suspend fun removeTask(id: String)
    @Query("UPDATE tasks SET isCompleted = 1 WHERE id = :id")
    suspend fun markAsCompleted(id: String)
    @Query("UPDATE tasks SET isCompleted = 0 WHERE id = :id")
    suspend fun markAsUncompleted(id: String)
}