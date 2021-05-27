package com.syntia.moviecatalogue.features.home.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagingData
import com.syntia.moviecatalogue.base.presentation.viewmodel.BaseViewModel
import com.syntia.moviecatalogue.core.domain.model.movie.MovieUiModel
import com.syntia.moviecatalogue.core.domain.usecase.TrendingUseCase
import kotlinx.coroutines.InternalCoroutinesApi

@InternalCoroutinesApi
class HomeMovieViewModel(private val trendingUseCase: TrendingUseCase) : BaseViewModel() {

  private var _movies = MutableLiveData<PagingData<MovieUiModel>>()
  val movies: LiveData<PagingData<MovieUiModel>>
    get() = _movies

  fun fetchMovies() {
    launchViewModelScope {
      trendingUseCase.getPopularMovies().runPagingFlow {
        _movies.value = it
      }
    }
  }
}