package com.syntia.moviecatalogue.core.domain.usecase.interactor

import com.syntia.moviecatalogue.core.domain.repository.TrendingRepository
import com.syntia.moviecatalogue.core.domain.usecase.TrendingUseCase

class TrendingInteractor(private val trendingRepository: TrendingRepository) : TrendingUseCase {

  override suspend fun getTrendingItems() = trendingRepository.getTrendingItems()

  override suspend fun getPopularMovies() = trendingRepository.getPopularMovies()

  override suspend fun getPopularTvShows() = trendingRepository.getPopularTvShows()
}