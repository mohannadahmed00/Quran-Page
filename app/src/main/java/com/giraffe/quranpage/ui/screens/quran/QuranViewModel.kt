package com.giraffe.quranpage.ui.screens.quran


import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.withStyle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.giraffe.quranpage.local.model.SurahModel
import com.giraffe.quranpage.local.model.VerseModel
import com.giraffe.quranpage.repo.Repository
import com.giraffe.quranpage.ui.theme.brown
import com.giraffe.quranpage.ui.theme.fontFamilies
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.ceil

@HiltViewModel
class QuranViewModel @Inject constructor(private val repository: Repository) : ViewModel(),
    QuranEvents {
    private val _state = MutableStateFlow(QuranScreenState())
    val state = _state.asStateFlow()

    init {
        getSurahesData()
        getAllVerses()
    }

    private fun getSurahesData() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getSurahesData().let { surahes ->
                _state.update { it.copy(surahesData = surahes) }
            }
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
                    append(handledVerse.substring(0,handledVerse.length-1))
                    withStyle(style = SpanStyle(color = brown)){
                        append(handledVerse.substring(handledVerse.length-1))
                    }

                }
                pop()

            }
        }
    }
    private fun handleVerse(verse: VerseModel):String{
        return when (verse.pageIndex) {
            1 -> {
                handelALFatiha(verse.qcfData,verse.verseNumber)
            }
            2 -> {
                handelFirstPageOfAlBaqarah(verse.qcfData,verse.verseNumber)
            }
            else -> {
                verse.qcfData
            }
        }
    }
    private fun handelALFatiha(txt: String, verseNumber: Int): String {
        val str = StringBuilder()
        when (verseNumber) {
            1, 2, 4 -> {
                str.append(txt)
                str.append("\n")
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
                                        state.selectedVerse
                                    )
                                )
                            }
                        PageUI(
                            contents = contents,
                            orgContents = contents,
                            pageIndex = it.first,
                            fontFamily = fontFamilies[it.first - 1],
                            surahName = getSurahesOfPage(it.second, _state.value.surahesData),
                            juz = ceil(it.first / 20.0).toInt(),
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

    override fun onVerseSelected(pageUI: PageUI, content: Content, verse: VerseModel) {
        viewModelScope.launch(Dispatchers.IO) {
            val contentIndex = pageUI.contents.indexOfFirst { it == content }//0
            val contents = pageUI.orgContents.toMutableList()//pure
            contents[contentIndex] = pageUI.contents.first { it == content }
                .copy(text = convertVerseToText(content.verses, pageUI.fontFamily, verse))
            val pages = state.value.orgPages.toMutableList()
            pages[pageUI.pageIndex - 1] = pageUI.copy(contents = contents)
            _state.update { it.copy(selectedVerse = verse, pages = pages) }
        }
    }

    override fun onPageChanged(pageIndex: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            _state.update { it.copy(currentPageIndex = pageIndex) }
        }
    }
}