package com.giraffe.quranpage.ui.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.giraffe.quranpage.R
import com.giraffe.quranpage.ui.screens.quran.PageUI
import ir.kaaveh.sdpcompose.ssp

@Composable
fun PageFooter(modifier: Modifier = Modifier, pageUI: PageUI) {
    Row(
        modifier = modifier.padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(modifier = Modifier.weight(1f)) {
            if (pageUI.hasSajdah) Image(
                modifier = Modifier.size(20.dp),
                painter = painterResource(id = R.drawable.sajdah),
                contentDescription = ""
            )
        }
        Text(text = (pageUI.pageIndex).toString(),
            style = TextStyle(
                fontSize = 10.ssp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimary
            )
        )
        Box(modifier = Modifier.weight(1f), Alignment.CenterEnd) {
            pageUI.hezb?.let {
                Text(
                    text = it,
                    style = TextStyle(
                        fontSize = 10.ssp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                )
            }
        }
    }
}