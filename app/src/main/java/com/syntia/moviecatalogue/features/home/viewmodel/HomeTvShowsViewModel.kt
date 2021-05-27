package com.syntia.moviecatalogue.features.home.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagingData
import com.syntia.moviecatalogue.base.presentation.viewmodel.BaseViewModel
import com.syntia.moviecatalogue.core.domain.model.tvshow.TvShowsUiModel
import com.syntia.moviecatalogue.core.domain.usecase.TrendingUseCase
import kotlinx.coroutines.InternalCoroutinesApi

@InternalCoroutinesApi
class HomeTvShowsViewModel(private val trendingUseCase: TrendingUseCase) : BaseViewModel() {

  private var _tvShows = MutableLiveData<PagingData<TvShowsUiModel>>()
  val tvShows: LiveData<PagingData<TvShowsUiModel>>
    get() = _tvShows

  fun fetchTvShows() {
    launchViewModelScope {
      trendingUseCase.getPopularTvShows().runPagingFlow {
        _tvShows.value = it
      }
    }
  }
}