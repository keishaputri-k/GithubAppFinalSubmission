package com.kei.githubappsecondsubmission.domain.repository

import android.app.Application
import com.kei.githubappsecondsubmission.domain.data.local.DataBaseLocal
import com.kei.githubappsecondsubmission.domain.data.local.FavoriteDao
import com.kei.githubappsecondsubmission.domain.data.model.DetailUserResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FavoriteRepository @Inject constructor(application: Application) {
    private val favoriteDao: FavoriteDao
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    init {
        val dataBase = DataBaseLocal.dataBaseFavorite(application)
        favoriteDao = dataBase.favDao()
    }

    fun getListFavorite(): Flow<List<DetailUserResponse>> {
        return flow {
            val favoriteList = favoriteDao.getFavoritesDao()
            emit(favoriteList)
        }.flowOn(Dispatchers.IO)
    }

    fun insertFavoriteData(userResponse: DetailUserResponse) {
        executorService.execute { favoriteDao.insertDao(userResponse) }
    }

    fun deleteFavoritesData(userResponse: DetailUserResponse) {
        executorService.execute { favoriteDao.deleteDao(userResponse) }
    }
}