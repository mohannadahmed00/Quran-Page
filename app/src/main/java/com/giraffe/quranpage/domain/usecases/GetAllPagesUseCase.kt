package com.giraffe.quranpage.domain.usecases

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.withStyle
import com.giraffe.quranpage.common.utils.getJuzOfPageIndex
import com.giraffe.quranpage.domain.entities.SurahDataEntity
import com.giraffe.quranpage.domain.entities.VerseEntity
import com.giraffe.quranpage.presentation.ui.screens.quran.Content
import com.giraffe.quranpage.presentation.ui.screens.quran.PageUI
import com.giraffe.quranpage.presentation.ui.theme.onPrimaryContainerLight
import com.giraffe.quranpage.presentation.ui.theme.primaryLight
import javax.inject.Inject


class GetAllPagesUseCase @Inject constructor() {
    operator fun invoke(
        verses: List<VerseEntity>,
        surahesData: List<SurahDataEntity>
    ): List<PageUI> {
        var currentHezbNumber = 1
        return verses.groupBy { it.pageIndex }.toList().map {
            val pageHezbNumber = it.second.last().quarterHezbIndex
            val contents = it.second.groupBy { verses -> verses.surahIndex }
                .map { group ->
                    val surahArabicName =
                        surahesData.firstOrNull { surah -> surah.id == group.key }?.arabicName
                    Content(
                        surahArabicName ?: "",
                        group.value,
                        convertVerseToText(
                            group.value,
                            com.giraffe.quranpage.presentation.ui.theme.fontFamilies[it.first - 1],
                        ),
                        it.first
                    )
                }
            PageUI(
                contents = contents,
                orgContents = contents,
                pageIndex = it.first,
                fontFamily = com.giraffe.quranpage.presentation.ui.theme.fontFamilies[it.first - 1],
                surahName = getSurahesName(surahesData, it.first),
                juz = getJuzOfPageIndex(it.first),
                hezbStr = getHezb(
                    currentHezbNumber,
                    pageHezbNumber
                ) { hezb -> currentHezbNumber = hezb },
                hasSajdah = hasSajdah(contents)
            )
        }
    }

    fun highlightVerse(
        originalPages: List<PageUI>,
        selectedVerse: VerseEntity?,
        selectedVerseToRead: VerseEntity?
    ): MutableList<PageUI> {
        val pages = originalPages.toMutableList()//pure pages
        val pageUiToSelect = pages[selectedVerse?.pageIndex?.minus(1) ?: 0]
        val pageUiToRead = pages[selectedVerseToRead?.pageIndex?.minus(1) ?: 0]

        val contentIndexToSelect =
            pageUiToSelect.contents.indexOfFirst { c -> c.verses.contains(selectedVerse) }
        val contentIndexToRead =
            pageUiToRead.contents.indexOfFirst { c -> c.verses.contains(selectedVerseToRead) }


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
                        selectedVerse,
                        selectedVerseToRead
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
                            selectedVerse,
                            selectedVerseToRead
                        )
                    )
                    contentToRead?.let {
                        contentsToSelect[contentIndexToRead] = contentToRead.copy(
                            text = convertVerseToText(
                                contentsToRead[contentIndexToRead].verses,
                                pageUiToRead.fontFamily,
                                selectedVerse,
                                selectedVerseToRead
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
                            selectedVerse,
                            selectedVerseToRead
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
                            selectedVerse,
                            selectedVerseToRead
                        )
                    )
                    pages[pageUiToRead.pageIndex - 1] =
                        pageUiToRead.copy(contents = contentsToRead)
                }

            }

        }
        return pages
    }


    private fun hasSajdah(contents: List<Content>) = contents.firstOrNull { content ->
        content.verses.firstOrNull { verse -> verse.hasSajda } != null
    } != null

    private fun convertVerseToText(
        verses: List<VerseEntity>,
        fontFamily: FontFamily,
        verseToSelect: VerseEntity? = null,
        verseToRead: VerseEntity? = null,
    ): AnnotatedString {
        return buildAnnotatedString {
            verses.forEach { verse ->
                pushStringAnnotation(tag = verse.qcfContent, annotation = verse.qcfContent)
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

    private fun handleVerse(verse: VerseEntity): String {
        return when (verse.pageIndex) {
            1 -> {
                handelALFatiha(verse.qcfContent, verse.verseIndex)
            }

            2 -> {
                handelFirstPageOfAlBaqarah(verse.qcfContent, verse.verseIndex)
            }

            else -> {
                verse.qcfContent
            }
        }
    }

    private fun handelALFatiha(txt: String, verseIndex: Int): String {
        val str = StringBuilder()
        when (verseIndex) {
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

    private fun handelFirstPageOfAlBaqarah(txt: String, verseIndex: Int): String {
        val str = StringBuilder()
        val l = txt.length
        when (verseIndex) {
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

    private fun getHezb(
        currentHezbNumber: Int,
        pageHezbNumber: Int,
        action: (Int) -> Unit
    ): String? {
        return if (currentHezbNumber == pageHezbNumber) {
            null
        } else {
            action(pageHezbNumber)
            getHezbStr(currentHezbNumber)
        }
    }

    private fun getHezbStr(quarterIndex: Int): String {
        val integerPart = (quarterIndex / 4.0).toInt()//0
        val fractionalPart = (quarterIndex / 4.0) - integerPart//0.25
        val str = StringBuilder()
        if (fractionalPart != 0.0) str.append(if ((fractionalPart / .25).toInt() == 2) "1/2" else "${(fractionalPart / .25).toInt()}/4")
        str.append(" hezb ${integerPart + 1}")
        return str.toString()
    }

    private fun getSurahesName(surahesData: List<SurahDataEntity>, pageIndex: Int): String {
        val str = StringBuilder()
        var surahNumber = 0
        surahesData.filter { pageIndex in it.startPageIndex..it.endPageIndex }
            .forEach {
                if (it.id != surahNumber) {
                    surahNumber = it.id
                    str.append(
                        surahesData.firstOrNull { surah -> surah.id == surahNumber }?.englishName
                            ?: ""
                    )
                    str.append("    ")
                }
            }
        return str.toString().trim()
    }
}