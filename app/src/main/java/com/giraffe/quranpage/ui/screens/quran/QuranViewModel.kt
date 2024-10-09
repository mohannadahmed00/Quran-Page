package com.giraffe.quranpage.ui.screens.quran


import android.util.Log
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.withStyle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.giraffe.quranpage.local.model.ReciterModel
import com.giraffe.quranpage.local.model.SurahAudioModel
import com.giraffe.quranpage.local.model.VerseModel
import com.giraffe.quranpage.repo.Repository
import com.giraffe.quranpage.service.DownloadService
import com.giraffe.quranpage.ui.theme.fontFamilies
import com.giraffe.quranpage.ui.theme.onPrimaryContainerLight
import com.giraffe.quranpage.ui.theme.primaryLight
import com.giraffe.quranpage.usecases.BookmarkVerseUseCase
import com.giraffe.quranpage.usecases.GetBookmarkedVerseUseCase
import com.giraffe.quranpage.utils.addOrUpdate
import com.giraffe.quranpage.utils.getHezb
import com.giraffe.quranpage.utils.getJuz
import com.giraffe.quranpage.utils.getSurahesName
import com.giraffe.quranpage.utils.hasSajdah
import com.giraffe.quranpage.utils.isNetworkAvailable
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuranViewModel @Inject constructor(
    private val repository: Repository,
    private val bookmarkVerseUseCase: BookmarkVerseUseCase,
    private val getBookmarkedVerseUseCase: GetBookmarkedVerseUseCase,
) : ViewModel(),
    QuranEvents {
    private val _state = MutableStateFlow(QuranScreenState())
    val state = _state.asStateFlow()

    init {
        getReciters()
        getSurahesData()
        getAllVerses()
        getBookmarkedVerse()
    }

    private fun getReciters() {
        viewModelScope.launch(Dispatchers.IO) {
            val reciters = repository.getReciters(isNetworkAvailable())
            _state.update {
                it.copy(
                    reciters = reciters,
                    selectedReciter = if (reciters.isNotEmpty()) reciters[0] else null
                )
            }
        }
    }

    private fun getSurahesData() {
        viewModelScope.launch(Dispatchers.IO) {
            _state.update { it.copy(surahesData = repository.getSurahesData()) }
        }
    }

    private fun convertVerseToText(
        verses: List<VerseModel>,
        fontFamily: FontFamily,
        verseToSelect: VerseModel? = null,
        verseToRead: VerseModel? = null,
    ): AnnotatedString {
        return buildAnnotatedString {
            verses.forEach { verse ->
                pushStringAnnotation(tag = verse.qcfData, annotation = verse.qcfData)
                val handledVerse = handleVerse(verse)


                withStyle(
                    style = SpanStyle(
                        color = onPrimaryContainerLight,
                        fontFamily = fontFamily,
                        background = when (verse) {
                            verseToSelect -> {
                                primaryLight.copy(
                                    alpha = 0.2f
                                )
                            }

                            verseToRead -> {
                                primaryLight.copy(
                                    alpha = 0.1f
                                )
                            }

                            else -> Color.Transparent
                        }
                    )
                ) {
                    append(handledVerse.substring(0, handledVerse.length - 1))
                    withStyle(style = SpanStyle(color = primaryLight.copy(alpha = 0.5f))) {
                        append(handledVerse.substring(handledVerse.length - 1))
                    }

                }
                pop()

            }
        }
    }

    private fun handleVerse(verse: VerseModel): String {
        return when (verse.pageIndex) {
            1 -> {
                handelALFatiha(verse.qcfData, verse.verseNumber)
            }

            2 -> {
                handelFirstPageOfAlBaqarah(verse.qcfData, verse.verseNumber)
            }

            else -> {
                verse.qcfData
            }
        }
    }

    private fun handelALFatiha(txt: String, verseNumber: Int): String {
        val str = StringBuilder()
        when (verseNumber) {
            2, 3, 5 -> {
                str.append("\n")
                str.append(txt)
            }

            7 -> {
                txt.forEachIndexed { index, c ->
                    str.append(c)
                    if (index == txt.length - 4) str.append("\n")
                }
            }

            else -> {
                str.append(txt)
            }
        }
        return str.toString()

    }

    private fun handelFirstPageOfAlBaqarah(txt: String, verseNumber: Int): String {
        val str = StringBuilder()
        val l = txt.length
        when (verseNumber) {
            2 -> {
                txt.forEachIndexed { index, c ->
                    str.append(c)
                    if (index == l - 3) str.append("\n")
                }
            }

            5 -> {
                txt.forEachIndexed { index, c ->
                    str.append(c)
                    if (index == l - 4) str.append("\n")
                }
            }

            else -> {
                str.append(txt)
            }
        }
        return str.toString()

    }

    private fun getAllVerses() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getAllVerses().let { ayahs ->
                var currentHezbNumber = 1
                _state.update { state ->
                    val pages = ayahs.groupBy { it.pageIndex }.toList().map {
                        val pageHezbNumber = it.second.last().quarterHezbIndex
                        val contents = it.second.groupBy { verses -> verses.surahNumber }
                            .map { group ->
                                val surahArabicName =
                                    state.surahesData.firstOrNull { surah -> surah.id == group.key }?.arabic
                                Content(
                                    surahArabicName ?: "",
                                    group.value,
                                    convertVerseToText(
                                        group.value,
                                        fontFamilies[it.first - 1],
                                    ),
                                    it.first
                                )
                            }
                        PageUI(
                            contents = contents,
                            orgContents = contents,
                            pageIndex = it.first,
                            fontFamily = fontFamilies[it.first - 1],
                            surahName = getSurahesName(_state.value.surahesData, it.first),
                            juz = getJuz(it.first),
                            hezbStr = getHezb(
                                currentHezbNumber,
                                pageHezbNumber
                            ) { hezb -> currentHezbNumber = hezb },
                            hasSajdah = hasSajdah(_state.value.orgPages, it.first)
                        )
                    }
                    state.copy(
                        ayahs = ayahs,
                        orgPages = pages,
                        pages = pages,
                    )
                }
            }
        }
    }

    override fun highlightVerse() {
        viewModelScope.launch(Dispatchers.IO) {

            val pages = state.value.orgPages.toMutableList()//pure pages

            val verseToSelect = state.value.selectedVerse
            val verseToRead = state.value.selectedVerseToRead


            val pageUiToSelect = pages[verseToSelect?.pageIndex?.minus(1) ?: 0]
            val pageUiToRead = pages[verseToRead?.pageIndex?.minus(1) ?: 0]


            val contentIndexToSelect =
                pageUiToSelect.contents.indexOfFirst { c -> c.verses.contains(verseToSelect) }
            val contentIndexToRead =
                pageUiToRead.contents.indexOfFirst { c -> c.verses.contains(verseToRead) }


            val contentsToSelect = pageUiToSelect.orgContents.toMutableList()//pure contents
            val contentsToRead = pageUiToRead.orgContents.toMutableList()//pure contents

            val contentToSelect =
                if (contentIndexToSelect != -1) contentsToSelect[contentIndexToSelect] else null
            val contentToRead =
                if (contentIndexToRead != -1) contentsToRead[contentIndexToRead] else null

            if (contentToSelect == contentToRead) {
                contentToSelect?.let {
                    contentsToSelect[contentIndexToSelect] = contentToSelect.copy(
                        text = convertVerseToText(
                            contentsToSelect[contentIndexToSelect].verses,
                            pageUiToSelect.fontFamily,
                            state.value.selectedVerse,
                            state.value.selectedVerseToRead
                        )
                    )
                    pages[pageUiToSelect.pageIndex - 1] =
                        pageUiToSelect.copy(contents = contentsToSelect)
                }


            } else {
                if (contentToSelect?.pageIndex == contentToRead?.pageIndex) {
                    contentToSelect?.let {
                        contentsToSelect[contentIndexToSelect] = contentToSelect.copy(
                            text = convertVerseToText(
                                contentsToSelect[contentIndexToSelect].verses,
                                pageUiToSelect.fontFamily,
                                state.value.selectedVerse,
                                state.value.selectedVerseToRead
                            )
                        )
                        contentToRead?.let {
                            contentsToSelect[contentIndexToRead] = contentToRead.copy(
                                text = convertVerseToText(
                                    contentsToRead[contentIndexToRead].verses,
                                    pageUiToRead.fontFamily,
                                    state.value.selectedVerse,
                                    state.value.selectedVerseToRead
                                )
                            )
                        }
                        pages[pageUiToSelect.pageIndex - 1] =
                            pageUiToSelect.copy(contents = contentsToSelect)
                    }

                } else {
                    contentToSelect?.let {
                        contentsToSelect[contentIndexToSelect] = contentToSelect.copy(
                            text = convertVerseToText(
                                contentsToSelect[contentIndexToSelect].verses,
                                pageUiToSelect.fontFamily,
                                state.value.selectedVerse,
                                state.value.selectedVerseToRead
                            )
                        )
                        pages[pageUiToSelect.pageIndex - 1] =
                            pageUiToSelect.copy(contents = contentsToSelect)
                    }
                    contentToRead?.let {
                        contentsToRead[contentIndexToRead] = contentToRead.copy(
                            text = convertVerseToText(
                                contentsToRead[contentIndexToRead].verses,
                                pageUiToRead.fontFamily,
                                state.value.selectedVerse,
                                state.value.selectedVerseToRead
                            )
                        )
                        pages[pageUiToRead.pageIndex - 1] =
                            pageUiToRead.copy(contents = contentsToRead)
                    }

                }

            }







            _state.update { s -> s.copy(pages = pages) }


        }
    }

    override fun getTafseer(surahIndex: Int, ayahIndex: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getTafseer(
                surahIndex,
                ayahIndex
            ).let { tasfeer ->
                _state.update {
                    it.copy(
                        selectedVerseTafseer = tasfeer
                    )
                }
            }

        }
    }

    override fun updateReciter(reciter: ReciterModel?) {
        viewModelScope.launch(Dispatchers.IO) {
            _state.update {
                it.copy(
                    reciters = it.reciters.toMutableList().addOrUpdate(reciter)
                )
            }
        }
    }

    override fun clearRecentDownload() {
        viewModelScope.launch(Dispatchers.IO) {
            _state.update {
                it.copy(
                    recentUrl = null,
                    isRecentDownloaded = true
                )
            }
        }
    }

    override fun bookmarkVerse(verseModel: VerseModel?) {
        viewModelScope.launch (Dispatchers.IO){
            _state.update {
                it.copy(bookmarkedVerse = verseModel)
            }
            bookmarkVerseUseCase(verseModel)
        }
    }

    override fun getBookmarkedVerse() {
        viewModelScope.launch (Dispatchers.IO){
            _state.update {
                it.copy(bookmarkedVerse = getBookmarkedVerseUseCase())
            }
        }
    }

    override fun setRecentUrl(url: String?) {
        viewModelScope.launch(Dispatchers.IO) {
            Log.d("QuranViewModel", "setRecentUrl($url)")
            _state.update {
                it.copy(
                    recentUrl = url,
                    isRecentDownloaded = false,
                )
            }
            Log.d("QuranViewModel", "setRecentUrl: ${_state.value.recentUrl}")
        }
    }

    override fun saveLastPageIndex(pageIndex: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.saveLastPageIndex(pageIndex)
        }
    }

    private fun getLastPageIndex() {
        viewModelScope.launch(Dispatchers.IO) {
            Log.d("TAG", "getLastPageIndex: ${repository.getLastPageIndex()}")
            _state.update { it.copy(lastPageIndex = repository.getLastPageIndex()) }
        }
    }

    override fun selectVerseToRead(verse: VerseModel?) {
        viewModelScope.launch(Dispatchers.IO) {
            _state.update {
                it.copy(
                    selectedVerseToRead = verse,
                    pageIndexToRead = verse?.pageIndex
                )
            }
        }
    }

    override fun selectVerse(verse: VerseModel?) {
        viewModelScope.launch(Dispatchers.IO) {
            _state.update {
                it.copy(
                    selectedVerse = verse,
                    pageIndexToSelection = verse?.pageIndex
                )
            }
        }
    }

    override fun setFirstVerse(verse: VerseModel?) {
        viewModelScope.launch(Dispatchers.IO) {
            _state.update { it.copy(firstVerse = verse) }
        }
    }
}