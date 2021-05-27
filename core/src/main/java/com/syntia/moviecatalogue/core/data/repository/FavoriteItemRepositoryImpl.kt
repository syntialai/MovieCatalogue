package com.syntia.moviecatalogue.core.data.repository

import androidx.paging.PagingData
import com.syntia.moviecatalogue.base.data.repository.BaseRepository
import com.syntia.moviecatalogue.core.data.source.local.datasource.FavoriteMoviesLocalDataSource
import com.syntia.moviecatalogue.core.data.source.local.datasource.FavoriteTvShowsLocalDataSource
import com.syntia.moviecatalogue.core.domain.model.detail.DetailUiModel
import com.syntia.moviecatalogue.core.domain.model.movie.MovieUiModel
import com.syntia.moviecatalogue.core.domain.model.tvshow.TvShowsUiModel
import com.syntia.moviecatalogue.core.domain.repository.FavoriteItemRepository
import com.syntia.moviecatalogue.core.utils.mapper.DetailMapper
import com.syntia.moviecatalogue.core.utils.mapper.FavoriteMapper
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn

class FavoriteItemRepositoryImpl(
    private val favoriteMoviesLocalDataSource: FavoriteMoviesLocalDataSource,
    private val favoriteTvShowsLocalDataSource: FavoriteTvShowsLocalDataSource,
    override val ioDispatcher: CoroutineDispatcher) : FavoriteItemRepository, BaseRepository() {

  override suspend fun getFavoriteMovies(): Flow<PagingData<MovieUiModel>> {
    return createLocalPagingFlow(favoriteMoviesLocalDataSource::getAllFavoriteMovies,
        FavoriteMapper::toMovieUiModels)
  }

  override suspend fun getFavoriteTvShows(): Flow<PagingData<TvShowsUiModel>> {
    return createLocalPagingFlow(favoriteTvShowsLocalDataSource::getAllFavoriteTvShows,
        FavoriteMapper::toTvShowUiModels)
  }

  override suspend fun getIsMovieExist(id: Int): Flow<Boolean> {
    return favoriteMoviesLocalDataSource.getIsMovieExists(id).flowOn(ioDispatcher)
  }

  override suspend fun getIsTvShowExist(id: Int): Flow<Boolean> {
    return favoriteTvShowsLocalDataSource.getIsTvShowExists(id).flowOn(ioDispatcher)
  }

  override suspend fun addMovie(detailUiModel: DetailUiModel) {
    val movie = DetailMapper.toMovieEntity(detailUiModel)
    favoriteMoviesLocalDataSource.addMovie(movie)
  }

  override suspend fun addTvShows(detailUiModel: DetailUiModel) {
    val tvShows = DetailMapper.toTvShowsEntity(detailUiModel)
    favoriteTvShowsLocalDataSource.addTvShow(tvShows)
  }

  override suspend fun deleteMovieById(id: Int) {
    favoriteMoviesLocalDataSource.deleteMovieById(id)
  }

  override suspend fun deleteTvShowsById(id: Int) {
    favoriteTvShowsLocalDataSource.deleteTvShowById(id)
  }
}