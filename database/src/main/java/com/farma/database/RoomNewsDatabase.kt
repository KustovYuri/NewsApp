package com.farma.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.farma.database.dao.ArticleDao
import com.farma.database.models.ArticleDBO
import com.farma.database.utils.Converters

class NewsDatabase internal constructor(private val database: RoomNewsDatabase){
    val articlesDao: ArticleDao
        get() = database.articlesDao()
}

@Database(entities = [ArticleDBO::class], version = 1)
@TypeConverters(Converters::class)
internal abstract class RoomNewsDatabase: RoomDatabase(){
    abstract fun articlesDao():ArticleDao
}

fun NewsDatabase(applicationContext: Context):NewsDatabase{
    val newsRoomDatabase = Room.databaseBuilder(
        checkNotNull(applicationContext.applicationContext),
        RoomNewsDatabase::class.java,
        "news"
    ).build()

    return NewsDatabase(newsRoomDatabase)
}