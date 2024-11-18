package com.giraffe.quranpage.presentation.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import com.giraffe.quranpage.domain.entities.SurahDataEntity
import ir.kaaveh.sdpcompose.sdp
import ir.kaaveh.sdpcompose.ssp

@Composable
fun SurahDrawerItem(
    surah: SurahDataEntity,
    isSelected: Boolean = false,
    primary: Color = MaterialTheme.colorScheme.primary.copy(alpha = 0.4f),
    inverseOnSurface: Color = MaterialTheme.colorScheme.inverseOnSurface,
    scrollTo: (Int) -> Unit
) {

    val lightPrimary = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
    val selectionColor = remember(isSelected) {
        if (isSelected) inverseOnSurface else primary.copy(alpha = 0.4f)
    }
    Row(
        modifier = Modifier
            .background(color = if (isSelected) primary else inverseOnSurface)
            .clickable { scrollTo(surah.startPageIndex) }
            .padding(horizontal = 8.sdp, vertical = 10.sdp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(35.sdp)
                .background(
                    color = if (isSelected) inverseOnSurface else lightPrimary,
                    shape = CircleShape
                )
        ) {
            Text(text = surah.id.toString(), fontSize = 14.ssp)
        }
        Spacer(modifier = Modifier.width(8.sdp))
        Column {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(text = surah.englishName, fontSize = 14.ssp, fontWeight = FontWeight.Medium)
                Text(
                    text = " (${surah.place})",
                    style = TextStyle(
                        fontSize = 14.ssp,
                        color = selectionColor
                    )
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "Verses: ${surah.countOfVerses} /",
                    style = TextStyle(
                        fontSize = 12.ssp,
                        color = selectionColor
                    )
                )
                Text(
                    text = " Pages: ${surah.startPageIndex} - ${surah.endPageIndex}",
                    style = TextStyle(
                        fontSize = 12.ssp,
                        color = selectionColor
                    )
                )
            }
        }

    }
}