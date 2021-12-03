package com.kei.githubappsecondsubmission.domain.data.local

import androidx.room.*
import com.kei.githubappsecondsubmission.domain.data.model.DetailUserResponse

//DAO = Data Access Object

@Dao
interface FavoriteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertDao (detailUserResponse: DetailUserResponse)

    @Query("SELECT * from detailUserResponse")
    fun getFavoritesDao() :List<DetailUserResponse>

    @Delete
    fun deleteDao(detailUserResponse: DetailUserResponse)
}