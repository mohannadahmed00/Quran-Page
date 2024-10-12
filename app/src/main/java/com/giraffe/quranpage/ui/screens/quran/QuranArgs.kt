package com.giraffe.quranpage.ui.screens.quran

import androidx.lifecycle.SavedStateHandle
import com.giraffe.quranpage.local.model.VerseModel


class QuranArgs(savedStateHandle: SavedStateHandle) {
    val searchResult: VerseModel? = savedStateHandle[SEARCH_RESULT]

    companion object {
        const val SEARCH_RESULT = "SEARCH_RESULT"
    }
}