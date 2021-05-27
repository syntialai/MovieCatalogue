package com.syntia.moviecatalogue.core.data.repository

import androidx.paging.PagingData
import com.syntia.moviecatalogue.base.data.repository.BaseRepository
import com.syntia.moviecatalogue.base.domain.model.result.ResultWrapper
import com.syntia.moviecatalogue.core.data.source.remote.datasource.TrendingRemoteDataSource
import com.syntia.moviecatalogue.core.domain.model.movie.MovieUiModel
import com.syntia.moviecatalogue.core.domain.model.trending.TrendingItemUiModel
import com.syntia.moviecatalogue.core.domain.model.tvshow.TvShowsUiModel
import com.syntia.moviecatalogue.core.domain.repository.TrendingRepository
import com.syntia.moviecatalogue.core.utils.mapper.TrendingMapper
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow

class TrendingRepositoryImpl(private val trendingRemoteDataSource: TrendingRemoteDataSource,
    override val ioDispatcher: CoroutineDispatcher) : TrendingRepository, BaseRepository() {

  override suspend fun getTrendingItems(): Flow<ResultWrapper<List<TrendingItemUiModel>>> {
    return fetchRemote(trendingRemoteDataSource::getTrendingItems,
        TrendingMapper::toTrendingItemUiModels)
  }

  override suspend fun getPopularMovies(): Flow<PagingData<MovieUiModel>> {
    return getNetworkPagingFlow(trendingRemoteDataSource::getPopularMovies,
        TrendingMapper::toMovieUiModels)
  }

  override suspend fun getPopularTvShows(): Flow<PagingData<TvShowsUiModel>> {
    return getNetworkPagingFlow(trendingRemoteDataSource::getPopularTvShows,
        TrendingMapper::toTvShowsUiModels)
  }
}