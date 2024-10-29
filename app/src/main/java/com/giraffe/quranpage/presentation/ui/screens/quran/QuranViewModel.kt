package com.giraffe.quranpage.presentation.ui.screens.quran


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.giraffe.quranpage.common.utils.addOrUpdate
import com.giraffe.quranpage.common.utils.domain.onError
import com.giraffe.quranpage.common.utils.domain.onSuccess
import com.giraffe.quranpage.common.utils.getJuzOfPageIndex
import com.giraffe.quranpage.common.utils.presentation.highlightVerse
import com.giraffe.quranpage.domain.entities.ReciterEntity
import com.giraffe.quranpage.domain.entities.SurahDataEntity
import com.giraffe.quranpage.domain.entities.VerseEntity
import com.giraffe.quranpage.domain.usecases.BookmarkVerseUseCase
import com.giraffe.quranpage.domain.usecases.GetAllPagesUseCase
import com.giraffe.quranpage.domain.usecases.GetBookmarkedVerseUseCase
import com.giraffe.quranpage.domain.usecases.GetLastPageUseCase
import com.giraffe.quranpage.domain.usecases.GetRecitersUseCase
import com.giraffe.quranpage.domain.usecases.GetSurahesDataUseCase
import com.giraffe.quranpage.domain.usecases.GetTafseerUseCase
import com.giraffe.quranpage.domain.usecases.RemoveBookmarkedVerseUseCase
import com.giraffe.quranpage.domain.usecases.SaveLastPageUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuranViewModel @Inject constructor(
    private val getRecitersUseCase: GetRecitersUseCase,
    private val getSurahesDataUseCase: GetSurahesDataUseCase,
    private val getAllPagesUseCase: GetAllPagesUseCase,
    private val bookmarkVerseUseCase: BookmarkVerseUseCase,
    private val getBookmarkedVerseUseCase: GetBookmarkedVerseUseCase,
    private val getTafseerUseCase: GetTafseerUseCase,
    private val saveLastPageUseCase: SaveLastPageUseCase,
    private val getLastPageUseCase: GetLastPageUseCase,
    private val removeBookmarkedVerseUseCase: RemoveBookmarkedVerseUseCase
) : ViewModel(), QuranEvents {
    private val _state = MutableStateFlow(QuranScreenState())
    val state = _state.asStateFlow()

    init {
        getAllVerses()
        getBookmarkedVerse()
    }

    private fun getAllVerses() {
        viewModelScope.launch(Dispatchers.IO) {
            getSurahesDataUseCase().let { surahesData ->
                getAllPagesUseCase(surahesData).let { pages ->
                    getLastPageUseCase().let { lastPageIndex ->
                        getRecitersUseCase().let { reciters ->
                            _state.update { state ->
                                pages.map { it.toUi() }.let { pagesUi ->
                                    val firstVerse =
                                        pagesUi[lastPageIndex - 1].contents[0].verses[0]
                                    val selectedReciter =
                                        reciters.firstOrNull { it.surahesAudioData.firstOrNull { s -> s.surahIndex == firstVerse.surahIndex } != null }
                                            ?: reciters[0]
                                    state.copy(
                                        allOriginalPages = pagesUi,
                                        allPages = pagesUi,
                                        lastPageIndex = lastPageIndex,
                                        firstVerse = firstVerse,
                                        surahesData = surahesData,
                                        surahesByJuz = surahesData.groupBy { surah ->
                                            getJuzOfPageIndex(
                                                surah.startPageIndex
                                            )
                                        },
                                        reciters = reciters,
                                        selectedReciter = selectedReciter
                                    )
                                }
                            }
                        }

                    }
                }
            }

        }
    }

    override fun bookmarkVerse(verse: VerseEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            _state.update {
                it.copy(bookmarkedVerse = verse)
            }
            bookmarkVerseUseCase(verse)
        }
    }

    override fun removeBookmarkedVerse() {
        viewModelScope.launch(Dispatchers.IO) {
            removeBookmarkedVerseUseCase()
            _state.update {
                it.copy(bookmarkedVerse = null)
            }
        }
    }

    override fun highlightVerse(verse: VerseEntity, isToRead: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            if (isToRead) {
                _state.update {
                    it.copy(
                        selectedVerseToRead = verse,
                        pageIndexToRead = verse.pageIndex
                    )
                }
            } else {
                _state.update { it.copy(selectedVerse = verse) }
            }
            highlightVerse(
                originalPages = _state.value.allOriginalPages,
                selectedVerse = _state.value.selectedVerse,
                selectedVerseToRead = _state.value.selectedVerseToRead,
            ).let { pages ->
                _state.update { it.copy(allPages = pages) }
            }
        }
    }

    override fun unhighlightVerse(isToRead: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            if (isToRead) {
                _state.update { it.copy(selectedVerseToRead = null, pageIndexToRead = null) }
            } else {
                _state.update { it.copy(selectedVerse = null) }
            }
            highlightVerse(
                originalPages = _state.value.allOriginalPages,
                selectedVerse = _state.value.selectedVerse,
                selectedVerseToRead = _state.value.selectedVerseToRead,
            ).let { pages ->
                _state.update { it.copy(allPages = pages) }
            }
        }
    }

    override fun saveLastPageIndex() {
        viewModelScope.launch(Dispatchers.IO) {
            saveLastPageUseCase(_state.value.firstVerse?.pageIndex ?: 0)
        }
    }

    override fun clearTafseer() {
        viewModelScope.launch(Dispatchers.IO) {
            _state.update {
                it.copy(
                    selectedVerseTafseer = null,
                    selectedVerseTafseerError = null
                )
            }
        }
    }

    private fun getBookmarkedVerse() {
        viewModelScope.launch(Dispatchers.IO) {
            getBookmarkedVerseUseCase().let { bookmarkedVerse ->
                _state.update {
                    it.copy(bookmarkedVerse = bookmarkedVerse)
                }
            }
        }
    }

    override fun getTafseer(verse: VerseEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            getTafseerUseCase(verse = verse).let { result ->
                result.onSuccess { tafseer ->
                    _state.update {
                        it.copy(
                            selectedVerseTafseer = tafseer,
                            selectedVerseTafseerError = null
                        )
                    }
                }.onError { error ->
                    _state.update { it.copy(selectedVerseTafseerError = error) }
                }
            }

        }
    }

    override fun selectVerseToRead(verse: VerseEntity?) {
        viewModelScope.launch(Dispatchers.IO) {
            _state.update {
                it.copy(
                    selectedVerseToRead = verse,
                    pageIndexToRead = verse?.pageIndex
                )
            }
        }
    }

    override fun selectVerse(verse: VerseEntity?) {
        viewModelScope.launch(Dispatchers.IO) {
            _state.update {
                it.copy(
                    selectedVerse = verse,
                    pageIndexToSelection = verse?.pageIndex
                )
            }
        }
    }

    override fun setFirstVerseOfPage(pageIndex: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            _state.value.allPages.getOrNull(pageIndex)?.contents?.getOrNull(
                0
            )?.verses?.getOrNull(0).let { firstVerse ->
                _state.update { it.copy(firstVerse = firstVerse) }
            }
        }
    }

    override fun updateReciter(reciter: ReciterEntity?) {
        viewModelScope.launch(Dispatchers.IO) {
            _state.update {
                it.copy(
                    reciters = it.reciters.toMutableList().addOrUpdate(reciter)
                )
            }
        }
    }

    override fun selectReciter(reciter: ReciterEntity?) {
        viewModelScope.launch(Dispatchers.IO) {
            _state.update {
                it.copy(
                    selectedReciter = reciter
                )
            }
        }
    }

    override fun clearRecentDownload() {
        viewModelScope.launch(Dispatchers.IO) {
            _state.update {
                it.copy(
                    recentUrl = null,
                    isRecentDownloaded = true,
                    recentSurahToDownload = null,
                )
            }
        }
    }

    override fun setRecentDownload(url: String, recentSurahToDownload: SurahDataEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            _state.update {
                it.copy(
                    recentUrl = url,
                    recentSurahToDownload = recentSurahToDownload,
                    isRecentDownloaded = false,
                )
            }
        }
    }
}