package com.syntia.moviecatalogue.features.detail.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.syntia.moviecatalogue.base.domain.model.result.ResultWrapper
import com.syntia.moviecatalogue.base.presentation.viewmodel.BaseViewModel
import com.syntia.moviecatalogue.core.domain.model.detail.CastUiModel
import com.syntia.moviecatalogue.core.domain.model.detail.DetailUiModel
import com.syntia.moviecatalogue.core.domain.usecase.DetailUseCase
import com.syntia.moviecatalogue.core.domain.usecase.FavoriteItemUseCase
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest

@InternalCoroutinesApi
class DetailViewModel(private val detailUseCase: DetailUseCase,
    private val favoriteItemUseCase: FavoriteItemUseCase) : BaseViewModel() {

  companion object {
    private const val MOVIE = "movie"
  }

  private var _casts = MutableLiveData<MutableList<CastUiModel>>()
  val casts: LiveData<MutableList<CastUiModel>>
    get() = _casts

  private var _details = MutableLiveData<DetailUiModel>()
  val details: LiveData<DetailUiModel>
    get() = _details

  private var _loadCasts = MutableLiveData<ResultWrapper<MutableList<CastUiModel>>>()
  val loadCasts: LiveData<ResultWrapper<MutableList<CastUiModel>>>
    get() = _loadCasts

  private var _isFavoriteItem = MutableLiveData<Boolean>()
  val isFavoriteItem: LiveData<Boolean>
    get() = _isFavoriteItem

  private var _typeAndId: Pair<String, Int>? = null

  fun setIdAndType(id: Int, type: String) {
    _typeAndId = Pair(type, id)
  }

  fun fetchDetails() {
    _typeAndId?.let { typeAndId ->
      launchViewModelScope {
        detailUseCase.getDetails(typeAndId.first, typeAndId.second).runFlow {
          _details.value = it
        }
      }
    }
  }

  fun fetchCasts() {
    _typeAndId?.let { typeAndId ->
      launchViewModelScope {
        detailUseCase.getDetailCasts(typeAndId.first, typeAndId.second).collectLatest {
          checkResponse(it, ::onSuccessFetchCasts)
        }
      }
    }
  }

  fun getIsFavoriteItem() {
    _typeAndId?.let { typeAndId ->
      launchViewModelScope {
        if (typeAndId.first == MOVIE) {
          favoriteItemUseCase.getIsMovieExist(typeAndId.second).collectLatest {
            _isFavoriteItem.value = it
          }
        } else {
          favoriteItemUseCase.getIsTvShowExist(typeAndId.second).collectLatest {
            _isFavoriteItem.value = it
          }
        }
      }
    }
  }

  fun updateFavoriteItem() {
    val isAdded = _isFavoriteItem.value ?: false
    _typeAndId?.let { typeAndId ->
      if (typeAndId.first == MOVIE) {
        updateMovieItem(isAdded)
      } else {
        updateTvItem(isAdded)
      }
      _isFavoriteItem.value = isAdded.not()
    }
  }

  private fun updateMovieItem(isAdded: Boolean) {
    _details.value?.let {
      launchViewModelScope {
        if (isAdded) {
          favoriteItemUseCase.deleteMovieById(it.id)
        } else {
          favoriteItemUseCase.addMovie(it)
        }
      }
    }
  }

  private fun updateTvItem(isAdded: Boolean) {
    _details.value?.let {
      launchViewModelScope {
        if (isAdded) {
          favoriteItemUseCase.deleteTvShowsById(it.id)
        } else {
          favoriteItemUseCase.addTvShows(it)
        }
      }
    }
  }

  private fun onSuccessFetchCasts(castsData: MutableList<CastUiModel>) {
    _casts.value = castsData
  }
}