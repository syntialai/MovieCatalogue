package com.syntia.moviecatalogue.core.domain.usecase.interactor

import com.syntia.moviecatalogue.core.domain.model.detail.DetailUiModel
import com.syntia.moviecatalogue.core.domain.repository.FavoriteItemRepository
import com.syntia.moviecatalogue.core.domain.usecase.FavoriteItemUseCase
import kotlinx.coroutines.flow.Flow

class FavoriteItemInteractor(private val favoriteItemRepository: FavoriteItemRepository) :
    FavoriteItemUseCase {

  override suspend fun getFavoriteMovies() = favoriteItemRepository.getFavoriteMovies()

  override suspend fun getFavoriteTvShows() = favoriteItemRepository.getFavoriteTvShows()

  override suspend fun getIsMovieExist(
      id: Int): Flow<Boolean> = favoriteItemRepository.getIsMovieExist(id)

  override suspend fun getIsTvShowExist(
      id: Int): Flow<Boolean> = favoriteItemRepository.getIsTvShowExist(id)

  override suspend fun addMovie(detailUiModel: DetailUiModel) {
    favoriteItemRepository.addMovie(detailUiModel)
  }

  override suspend fun addTvShows(detailUiModel: DetailUiModel) {
    favoriteItemRepository.addTvShows(detailUiModel)
  }

  override suspend fun deleteMovieById(id: Int) {
    favoriteItemRepository.deleteMovieById(id)
  }

  override suspend fun deleteTvShowsById(id: Int) {
    favoriteItemRepository.deleteTvShowsById(id)
  }
}