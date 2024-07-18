package com.giraffe.quranpage.ui.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.DrawerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.giraffe.quranpage.local.model.VerseModel
import com.giraffe.quranpage.ui.screens.quran.Content
import com.giraffe.quranpage.ui.screens.quran.PageUI

@Composable
fun Page(
    modifier: Modifier = Modifier,
    pageUI: PageUI,
    onVerseSelected: (PageUI, Content, VerseModel) -> Unit,
    onDrawerClicked: () -> Unit
) {
    Column(
        modifier = modifier
            .statusBarsPadding(),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        PageHeader(pageUI = pageUI, onDrawerClicked = onDrawerClicked)
        PageContent(
            modifier = Modifier.weight(1f),
            pageUI = pageUI,
            onVerseSelected = onVerseSelected
        )
        PageFooter(pageUI = pageUI)
    }

}