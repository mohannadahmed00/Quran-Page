package com.giraffe.quranpage.presentation.ui.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.giraffe.quranpage.domain.entities.SurahDataEntity
import com.giraffe.quranpage.domain.entities.VerseEntity
import com.giraffe.quranpage.presentation.ui.screens.quran.PageUi
import ir.kaaveh.sdpcompose.sdp

@Composable
fun Page(
    pageUI: PageUi,
    surahesData: List<SurahDataEntity>,
    onVerseSelected: (VerseEntity) -> Unit,
    onPageClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        PageHeader(
            surahName = pageUI.namesOfSurahes,
            juz = pageUI.juz
        )
        PageContent(
            modifier = Modifier.width(316.sdp),
            pageUI = pageUI,
            surahesData = surahesData,
            onVerseSelected = onVerseSelected,
            onPageClick = onPageClick
        )
        PageFooter(
            pageIndex = pageUI.pageIndex,
            hezb = pageUI.hezbStr,
            hasSajdah = pageUI.hasSajdah
        )
    }

}