package com.syntia.moviecatalogue.di

import com.syntia.moviecatalogue.core.domain.usecase.DetailUseCase
import com.syntia.moviecatalogue.core.domain.usecase.FavoriteItemUseCase
import com.syntia.moviecatalogue.core.domain.usecase.SearchUseCase
import com.syntia.moviecatalogue.core.domain.usecase.TrendingUseCase
import com.syntia.moviecatalogue.core.domain.usecase.interactor.DetailInteractor
import com.syntia.moviecatalogue.core.domain.usecase.interactor.FavoriteItemInteractor
import com.syntia.moviecatalogue.core.domain.usecase.interactor.SearchInteractor
import com.syntia.moviecatalogue.core.domain.usecase.interactor.TrendingInteractor
import com.syntia.moviecatalogue.features.detail.viewmodel.DetailViewModel
import com.syntia.moviecatalogue.features.home.viewmodel.HomeMovieViewModel
import com.syntia.moviecatalogue.features.home.viewmodel.HomeTvShowsViewModel
import com.syntia.moviecatalogue.features.main.viewmodel.MainViewModel
import com.syntia.moviecatalogue.features.search.viewmodel.SearchViewModel
import kotlinx.coroutines.InternalCoroutinesApi
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.bind
import org.koin.dsl.module

@InternalCoroutinesApi
val trendingFeatureModule = module {
  factory { TrendingInteractor(get()) } bind TrendingUseCase::class
  viewModel { MainViewModel(get()) }
  viewModel { HomeMovieViewModel(get()) }
  viewModel { HomeTvShowsViewModel(get()) }
}

@InternalCoroutinesApi
val detailFeatureModule = module {
  factory { DetailInteractor(get()) } bind DetailUseCase::class
  factory { FavoriteItemInteractor(get()) } bind FavoriteItemUseCase::class
  viewModel { DetailViewModel(get(), get()) }
}

@InternalCoroutinesApi
val searchFeatureModule = module {
  factory { SearchInteractor(get()) } bind SearchUseCase::class
  viewModel { SearchViewModel(get()) }
}