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
import androidx.compose.ui.unit.dp
import com.giraffe.quranpage.domain.entities.SurahDataEntity

@Composable
fun SurahDrawerItem(
    surah: SurahDataEntity,
    isSelected: Boolean = false,
    selectedColor: Color = MaterialTheme.colorScheme.secondaryContainer.copy(
        alpha = 0.5f
    ),
    unSelectedColor: Color = MaterialTheme.colorScheme.inverseOnSurface,
    scrollTo: (Int) -> Unit
) {
    val modifier = remember(isSelected) {
        Modifier
            .background(
                color = if (isSelected) selectedColor else unSelectedColor,
            )
            .padding(horizontal = 8.dp, vertical = 10.dp)
            .clickable { scrollTo(surah.startPageIndex) }
    }
    Row(
        modifier = modifier.background(
            color = if (isSelected) selectedColor else unSelectedColor,
        )
            .padding(horizontal = 8.dp, vertical = 10.dp)
            .clickable { scrollTo(surah.startPageIndex) },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(44.dp)
                .background(
                    color = if (isSelected) MaterialTheme.colorScheme.inverseOnSurface else MaterialTheme.colorScheme.secondaryContainer.copy(
                        alpha = 0.3f
                    ),
                    shape = CircleShape
                )
        ) {
            Text(text = surah.id.toString())
        }
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(text = surah.englishName)
                Text(
                    text = " (${surah.place})",
                    style = TextStyle(
                        color = MaterialTheme.colorScheme.primary.copy(
                            alpha = 0.4f
                        )
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
                        color = MaterialTheme.colorScheme.primary.copy(
                            alpha = 0.4f
                        )
                    )
                )
                Text(
                    text = " Pages: ${surah.startPageIndex} - ${surah.endPageIndex}",
                    style = TextStyle(
                        color = MaterialTheme.colorScheme.primary.copy(
                            alpha = 0.4f
                        )
                    )
                )
            }
        }

    }
}