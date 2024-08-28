package com.giraffe.quranpage.ui.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.giraffe.quranpage.local.model.VerseModel
import com.giraffe.quranpage.ui.screens.quran.PageUI

@Composable
fun Page(
    modifier: Modifier = Modifier,
    pageUI: PageUI,
    onVerseSelected: (VerseModel) -> Unit,
    onSurahNameClick: () -> Unit,
    onPageClick: () -> Unit
) {
    Column(
        modifier = modifier
            .statusBarsPadding(),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        PageHeader(pageUI = pageUI, onSurahNameClick = onSurahNameClick)
        PageContent(
            modifier = Modifier.weight(1f),
            pageUI = pageUI,
            onVerseSelected = onVerseSelected,
            onPageClick = onPageClick
        )
        PageFooter(pageUI = pageUI)
    }

}