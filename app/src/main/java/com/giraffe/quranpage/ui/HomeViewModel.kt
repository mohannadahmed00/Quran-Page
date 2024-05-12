package com.giraffe.quranpage.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.giraffe.quranpage.repo.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val repository: Repository) : ViewModel() {
    private val _state = MutableStateFlow(HomeState())
    val state = _state.asStateFlow()

    init {
        initStates()
    }

    private fun initStates() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getAyahsOfSurah(2) {
                repository.getPages { pages ->
                    _state.update {
                        it.copy(
                            pages = pages,
                            ayahs = repository.getAyahs(it.pageIndex)
                        )
                    }
                }
            }

        }
    }

    fun changePageIndex(pageIndex: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            _state.update { it.copy(ayahs = repository.getAyahs(pageIndex)) }
        }
    }

    private fun deleteAllPages() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAll {}
        }
    }

    companion object {
        private const val TAG = "HomeViewModel"
    }

}