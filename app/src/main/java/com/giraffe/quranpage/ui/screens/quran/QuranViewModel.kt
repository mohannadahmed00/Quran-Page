package com.giraffe.quranpage.ui.screens.quran

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class QuranViewModel @Inject constructor() : ViewModel() {
    private val _state = MutableStateFlow(QuranScreenState())
    val state = _state.asStateFlow()
}