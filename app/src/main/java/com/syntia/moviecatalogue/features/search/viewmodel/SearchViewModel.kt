package com.syntia.moviecatalogue.features.search.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagingData
import com.syntia.moviecatalogue.base.presentation.viewmodel.BaseViewModel
import com.syntia.moviecatalogue.core.domain.model.search.SearchResultUiModel
import com.syntia.moviecatalogue.core.domain.usecase.SearchUseCase
import kotlinx.coroutines.InternalCoroutinesApi

@InternalCoroutinesApi
class SearchViewModel(private val searchUseCase: SearchUseCase) : BaseViewModel() {

  private var _searchResults = MutableLiveData<PagingData<SearchResultUiModel>>()
  val searchResult: LiveData<PagingData<SearchResultUiModel>>
    get() = _searchResults

  fun searchQuery(query: String) {
    launchViewModelScope {
      searchUseCase.searchByQuery(query).runPagingFlow {
        _searchResults.value = it
      }
    }
  }
}