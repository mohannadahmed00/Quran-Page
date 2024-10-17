package com.giraffe.quranpage.presentation.ui.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.giraffe.quranpage.domain.entities.SurahDataEntity
import com.giraffe.quranpage.domain.entities.VerseEntity
import com.giraffe.quranpage.presentation.ui.screens.quran.PageUI

@Composable
fun Page(
    pageUI: PageUI,
    surahesData: List<SurahDataEntity>,
    onVerseSelected: (VerseEntity) -> Unit,
    onPageClick: () -> Unit
) {
    Column(
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        PageHeader(
            surahName = pageUI.surahName,
            juz = pageUI.juz
        )
        PageContent(
            modifier = Modifier.weight(1f),
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