package com.syntia.moviecatalogue.favorite.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagingData
import com.syntia.moviecatalogue.base.presentation.viewmodel.BaseViewModel
import com.syntia.moviecatalogue.core.domain.model.movie.MovieUiModel
import com.syntia.moviecatalogue.core.domain.usecase.FavoriteItemUseCase
import kotlinx.coroutines.InternalCoroutinesApi

@InternalCoroutinesApi
class FavoriteMovieViewModel(private val favoriteItemUseCase: FavoriteItemUseCase) :
    BaseViewModel() {

  private var _movies = MutableLiveData<PagingData<MovieUiModel>>()
  val movies: LiveData<PagingData<MovieUiModel>>
    get() = _movies

  fun fetchFavoriteMovies() {
    launchViewModelScope {
      favoriteItemUseCase.getFavoriteMovies().runPagingFlow {
        _movies.value = it
      }
    }
  }
}