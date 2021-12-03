package com.kei.githubappsecondsubmission.view.detail.dashboard

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kei.githubappsecondsubmission.domain.data.model.DetailUserResponse
import com.kei.githubappsecondsubmission.domain.data.network.ApiResult
import com.kei.githubappsecondsubmission.domain.repository.FavoriteRepository
import com.kei.githubappsecondsubmission.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val favoriteRepository: FavoriteRepository
) :
    ViewModel() {
    private val _detailUser: MutableLiveData<DetailUserResponse?> = MutableLiveData()
    val detailUser get() = _detailUser

    private val _loading: MutableLiveData<Boolean> = MutableLiveData()
    val loading get() = _loading

    private val _error: MutableLiveData<Throwable?> = MutableLiveData()
    val error get() = _error

    private var strUsername: String = " "

    private val _isFavorite: MutableLiveData<Boolean> = MutableLiveData()
    val isFavorite get() = _isFavorite

    private val _listFavorite :MutableLiveData<List<DetailUserResponse>?> = MutableLiveData()
    init {
        getFavoriteUser()
        _isFavorite.postValue(false)
    }


    fun getDetailUser(username: String) {
        if (strUsername != username) {
            viewModelScope.launch {
                strUsername = username
                userRepository.getDetailUser(username).onStart {
                    _loading.value = true
                }.onCompletion {
                    _loading.value = false
                }.collect {
                    when (it) {
                        is ApiResult.Success -> {
                            _error.postValue(null)
                            _detailUser.postValue(it.data)
                        }
                        is ApiResult.Error -> {
                            _error.postValue(it.throwable)
                        }
                    }
                }
            }
        }
    }

    private fun insert(favoriteUser: DetailUserResponse?) {
        viewModelScope.launch {
            if (favoriteUser != null){
                favoriteRepository.insertFavoriteData(favoriteUser)
                getFavoriteUser()
                _isFavorite.postValue(true)
            }
        }
    }

    private fun getFavoriteUser() {
        viewModelScope.launch {
            favoriteRepository.getListFavorite().collect {
                _listFavorite.postValue(it)
            }
        }
    }

    fun showFavorite(favoriteUser: DetailUserResponse?){
        viewModelScope.launch {
            for (it in _listFavorite.value?: mutableListOf()){
                if (favoriteUser?.login == it.login){
                    _isFavorite.postValue(true)
                    break
                }else{
                    _isFavorite.postValue(false)
                }
            }
        }
    }

    private fun delete(favoriteUser: DetailUserResponse?){
        viewModelScope.launch {
            if (favoriteUser != null){
                favoriteRepository.deleteFavoritesData(favoriteUser)
                getFavoriteUser()
                _isFavorite.postValue(false)
            }
        }
    }

    fun isFavoriteUser(favoriteUser: DetailUserResponse?){
        viewModelScope.launch {
            if (_isFavorite.value == true){
                delete(favoriteUser)
            }else{
                insert(favoriteUser)
            }
        }
    }
}