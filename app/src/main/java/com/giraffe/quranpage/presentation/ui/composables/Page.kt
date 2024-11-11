package com.giraffe.quranpage.presentation.ui.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.giraffe.quranpage.domain.entities.SurahDataEntity
import com.giraffe.quranpage.domain.entities.VerseEntity
import com.giraffe.quranpage.presentation.ui.screens.quran.PageUi

@Composable
fun Page(
    pageUI: PageUi,
    surahesData: List<SurahDataEntity>,
    onVerseSelected: (VerseEntity) -> Unit,
    onPageClick: () -> Unit
) {
    LazyColumn (
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            PageHeader(
                surahName = pageUI.namesOfSurahes,
                juz = pageUI.juz
            )
        }
        item{
            PageContent(
                modifier = Modifier.width(415.dp),
                pageUI = pageUI,
                surahesData = surahesData,
                onVerseSelected = onVerseSelected,
                onPageClick = onPageClick
            )
        }

        item {
            PageFooter(
                pageIndex = pageUI.pageIndex,
                hezb = pageUI.hezbStr,
                hasSajdah = pageUI.hasSajdah
            )
        }

    }

}