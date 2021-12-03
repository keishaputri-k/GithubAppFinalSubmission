package com.kei.githubappsecondsubmission.domain.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.kei.githubappsecondsubmission.domain.data.model.DetailUserResponse

@Database(entities = [DetailUserResponse::class], version = 1)
abstract class DataBaseLocal : RoomDatabase(){
    abstract fun favDao() : FavoriteDao
    companion object{
        @Volatile
        var instance : DataBaseLocal? = null

        fun dataBaseFavorite(context: Context) : DataBaseLocal{
            if (instance == null){
                synchronized(DataBaseLocal::class.java){
                    instance = Room.databaseBuilder(
                        context.applicationContext, DataBaseLocal::class.java, "Favorite"
                    ).build()
                }
            }
            return instance as DataBaseLocal
        }
    }

}