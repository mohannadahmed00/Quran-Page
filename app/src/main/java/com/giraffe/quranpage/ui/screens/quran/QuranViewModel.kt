package com.giraffe.quranpage.ui.screens.quran


import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.withStyle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.giraffe.quranpage.local.model.ReciterModel
import com.giraffe.quranpage.local.model.SurahModel
import com.giraffe.quranpage.local.model.VerseModel
import com.giraffe.quranpage.repo.Repository
import com.giraffe.quranpage.ui.theme.brown
import com.giraffe.quranpage.ui.theme.fontFamilies
import com.giraffe.quranpage.utils.isNetworkAvailable
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuranViewModel @Inject constructor(private val repository: Repository) : ViewModel(),
    QuranEvents {
    private val _state = MutableStateFlow(QuranScreenState())
    val state = _state.asStateFlow()

    init {
        getReciters()
        getSurahesData()
        getAllVerses()
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
        selectedVerse: VerseModel? = null
    ): AnnotatedString {
        return buildAnnotatedString {
            verses.forEach { verse ->
                pushStringAnnotation(tag = verse.qcfData, annotation = verse.qcfData)
                val handledVerse = handleVerse(verse)


                withStyle(
                    style = SpanStyle(
                        fontFamily = fontFamily,
                        background = if (
                            verse == selectedVerse
                        ) brown.copy(
                            alpha = 0.2f
                        ) else Color.Transparent
                    )
                ) {
                    append(handledVerse.substring(0, handledVerse.length - 1))
                    withStyle(style = SpanStyle(color = brown)) {
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
                        val pageHezb = getHezbNumbre(it.second)//2
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
                                        state.selectedVerse,
                                    ),
                                    it.first
                                )
                            }
                        PageUI(
                            contents = contents,
                            orgContents = contents,
                            pageIndex = it.first,
                            fontFamily = fontFamilies[it.first - 1],
                            surahName = getSurahesOfPage(it.second, _state.value.surahesData),
                            juz = getJuzIndexFromPage(it.first),
                            hezb = if (currentHezbNumber == pageHezb) {
                                null
                            } else {
                                val prev = currentHezbNumber
                                currentHezbNumber = pageHezb
                                getHezbStr(prev)
                            },
                            hasSajdah = hasSajdah(it.second)
                        )
                    }
                    state.copy(
                        orgPages = pages,
                        pages = pages,
                    )
                }
            }
        }
    }

    private fun getJuzIndexFromPage(pageIndex: Int): Int {
        val result = pageIndex/ 20.0
        val truncatedResult = (result * 10).toInt() / 10.0
        val digitAfterDecimal = ((result - result.toInt()) * 10).toInt()
        val juz = if (digitAfterDecimal==0){
            truncatedResult.toInt()
        }else{
            truncatedResult.toInt()+1
        }
        return when (juz) {
            0 -> {
                1
            }
            31 -> {
                30
            }
            else -> {
                juz
            }
        }
    }

    private fun hasSajdah(verses: List<VerseModel>): Boolean {
        var hasSajdah = false
        for (verse in verses) {
            if (verse.sajda) {
                hasSajdah = true
                break
            }
        }
        return hasSajdah
    }

    private fun getHezbNumbre(verses: List<VerseModel>): Int {
        var hezbNumber = 0
        verses.forEach {
            hezbNumber = it.quarterHezbIndex
        }
        return hezbNumber
    }

    private fun getHezbStr(quarterIndex: Int): String {
        val integerPart = (quarterIndex / 4.0).toInt()//0
        val fractionalPart = (quarterIndex / 4.0) - integerPart//0.25
        val str = StringBuilder()
        if (fractionalPart != 0.0) str.append(decimalToFraction(fractionalPart))
        str.append(" hezb ${integerPart + 1}")
        return str.toString()
    }

    private fun decimalToFraction(decimal: Double) =
        if ((decimal / .25).toInt() == 2) "1/2" else "${(decimal / .25).toInt()}/4"

    private fun getSurahesOfPage(verses: List<VerseModel>, surahesData: List<SurahModel>): String {
        val str = StringBuilder()
        var surahNumber = 0
        verses.forEach {
            if (it.surahNumber != surahNumber) {
                surahNumber = it.surahNumber
                str.append(surahesData.firstOrNull { surah -> surah.id == surahNumber }?.name ?: "")
                str.append("    ")
            }
        }
        return str.toString().trim()

    }

    override fun onVerseSelected(verse: VerseModel?, isToRead: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            if (!isToRead) {
                _state.update { it.copy(selectedVerse = verse) }
            } else {
                _state.update { it.copy(selectedVerseToRead = verse) }
                val pageUI = _state.value.pages[verse?.pageIndex?.minus(1)?:0]




                val contents = pageUI.orgContents.toMutableList()//pure contents




                val contentIndex = pageUI.contents.indexOfFirst { it.verses.contains(verse) }
                contents[contentIndex] = contents[contentIndex].copy(
                    text = convertVerseToText(
                        contents[contentIndex].verses,
                        pageUI.fontFamily,
                        verse
                    )
                )
                val pages = state.value.orgPages.toMutableList()//pure pages
                pages[pageUI.pageIndex - 1] = pageUI.copy(contents = contents)
                _state.update { it.copy(pages = pages) }
            }


        }
    }

    override fun onPageChanged(pageIndex: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            _state.update { it.copy(currentPageIndex = pageIndex) }
        }
    }

    override fun getTafseer(surahIndex: Int, ayahIndex: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            _state.update {
                it.copy(
                    selectedVerse = state.value.selectedVerse?.copy(
                        tafseer = repository.getTafseer(
                            surahIndex,
                            ayahIndex
                        )
                    )
                )
            }
        }
    }


    override fun onReciterClick(reciter: ReciterModel) {
        viewModelScope.launch(Dispatchers.IO) {
            val listOfSurahesAudioData = reciter.surahesAudioData
            val selectedSurahesAudioData =
                listOfSurahesAudioData.firstOrNull { it.surahId == _state.value.selectedVerse?.surahNumber }
            _state.update {
                it.copy(
                    selectedReciter = reciter
                )
            }
            selectedSurahesAudioData?.let { surahesAudioData ->
                _state.update { it.copy(selectedAudioData = surahesAudioData) }
            }
        }
    }

    override fun downloadSurahForReciter(reciter: ReciterModel) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.downloadSurahAudio(
                reciter.id,
                reciter.folderUrl,
                state.value.selectedVerse?.surahNumber ?: 1
            ) { listOfReciters ->
                val selectedReciter = listOfReciters.firstOrNull { r -> r.id == reciter.id }
                val listOfSurahesAudioData = selectedReciter?.surahesAudioData
                val selectedSurahesAudioData =
                    listOfSurahesAudioData?.firstOrNull { it.surahId == _state.value.selectedVerse?.surahNumber }
                _state.update {
                    it.copy(
                        selectedReciter = selectedReciter,
                        reciters = listOfReciters,
                        selectedAudioData = selectedSurahesAudioData
                    )
                }
            }

        }
    }
}