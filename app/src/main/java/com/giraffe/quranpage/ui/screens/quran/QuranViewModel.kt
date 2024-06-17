package com.giraffe.quranpage.ui.screens.quran

import androidx.lifecycle.ViewModel
import com.giraffe.quranpage.repo.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class QuranViewModel @Inject constructor(private val repository: Repository) : ViewModel() {
    private val _state = MutableStateFlow(QuranScreenState())
    val state = _state.asStateFlow()




    companion object{
        private const val TAG = "QuranViewModel"
    }
}