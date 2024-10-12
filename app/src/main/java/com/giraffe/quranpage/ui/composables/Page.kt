package com.giraffe.quranpage.ui.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.giraffe.quranpage.local.model.VerseModel
import com.giraffe.quranpage.ui.screens.quran.PageUI

@Composable
fun Page(
    pageUI: PageUI,
    pageData: PageUI,
    onVerseSelected: (VerseModel) -> Unit,
    onPageClick: () -> Unit
) {
    Column(
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        PageHeader(
            surahName = pageData.surahName,
            juz = pageData.juz
        )
        PageContent(
            modifier = Modifier.weight(1f),
            pageUI = pageUI,
            onVerseSelected = onVerseSelected,
            onPageClick = onPageClick
        )
        PageFooter(
            pageIndex = pageData.pageIndex,
            hezb = pageData.hezbStr,
            hasSajdah = pageData.hasSajdah
        )
    }

}