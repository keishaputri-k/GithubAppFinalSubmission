package com.kei.githubappsecondsubmission.view.favorite

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kei.githubappsecondsubmission.domain.data.model.DetailUserResponse
import com.kei.githubappsecondsubmission.domain.repository.FavoriteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(private val favoriteRepository: FavoriteRepository) :ViewModel(){
    private val _favoriteLiveData : MutableLiveData<List<DetailUserResponse>?> = MutableLiveData()
    val favoriteLiveData get() = _favoriteLiveData

    private val _error: MutableLiveData<Throwable?> = MutableLiveData()
    val error get() = _error

    init {
        getDataListFavorite()
    }

   fun getDataListFavorite() {
        viewModelScope.launch {
            favoriteRepository.getListFavorite().collect {
                _favoriteLiveData.postValue(it)
            }
        }
   }
}