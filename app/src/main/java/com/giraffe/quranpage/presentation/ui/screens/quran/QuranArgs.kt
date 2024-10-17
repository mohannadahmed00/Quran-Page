package com.giraffe.quranpage.presentation.ui.screens.quran

import androidx.lifecycle.SavedStateHandle
import com.giraffe.quranpage.domain.entities.VerseEntity


class QuranArgs(private val savedStateHandle: SavedStateHandle?) {
    val searchResult: VerseEntity? = savedStateHandle?.get(SEARCH_RESULT)
    fun clear() = savedStateHandle?.remove<VerseEntity?>(SEARCH_RESULT)

    companion object {
        const val SEARCH_RESULT = "SEARCH_RESULT"
    }
}