package com.giraffe.quranpage.presentation.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.giraffe.quranpage.common.utils.getPageIndexOfJuz

@Composable
fun DrawerPartHeader(
    parts: Set<Int>,
    juzIndex: Int,
    scrollTo: (Int) -> Unit
) {
    val previousPart by remember(juzIndex) {
        derivedStateOf {
            parts.elementAtOrNull(
                parts.indexOf(
                    juzIndex
                ) - 1
            )
        }
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f))
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        for (juz in (previousPart?.plus(1) ?: 1)..juzIndex) {
            val modifier = remember {
                Modifier.clickable { scrollTo(getPageIndexOfJuz(juz)) }
            }
            Text(
                modifier = modifier,
                text = "$juz juz'",
                style = TextStyle(
                    fontSize = 18.sp,
                    color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.3f)
                )
            )
        }
    }
}
