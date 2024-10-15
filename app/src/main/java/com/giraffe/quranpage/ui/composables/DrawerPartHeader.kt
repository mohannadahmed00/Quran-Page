package com.giraffe.quranpage.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.giraffe.quranpage.utils.getJuz
import com.giraffe.quranpage.utils.getPageIndexOfJuz

@Composable
fun DrawerPartHeader(
    startPageIndex: Int,
    oldJuz: Int,
    updateOldJuz: (Int) -> Unit,
    scrollTo: (Int) -> Unit
) {
    val newJuz = getJuz(startPageIndex)
    if (newJuz <= oldJuz) return

    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f))
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        for (juz in (oldJuz + 1)..newJuz) {
            item {
                Text(
                    modifier = Modifier.clickable { scrollTo(getPageIndexOfJuz(juz)) },
                    text = "$juz juz'",
                    style = TextStyle(
                        fontSize = 18.sp,
                        //fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.3f)
                    )
                )
            }
        }
    }


    /*if (newJuz-oldJuz>1){
        Row {
            for (juz in (oldJuz + 1)..newJuz) {
                Text(
                    modifier = Modifier.background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)).fillMaxWidth().padding(
                        top = if (juz==(oldJuz+1)) 8.dp else 0.dp,
                        bottom = if (juz==(newJuz)) 8.dp else 0.dp,
                        start = 8.dp,
                    ),
                    text = "$juz juz'",
                    style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.3f))
                )
            }
        }
    }else{
            Text(
                modifier = Modifier.background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)).fillMaxWidth().padding(
                    top = if (newJuz==(oldJuz+1)) 8.dp else 0.dp,
                    bottom = 8.dp,
                    start = 8.dp,
                ),
                text = "$newJuz juz'",
                style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.3f))
            )
    }*/
    /*for (juz in (oldJuz + 1)..newJuz) {
        Text(
            modifier = Modifier.background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)).fillMaxWidth().padding(
                top = if (juz==(oldJuz+1)) 8.dp else 0.dp,
                bottom = if (juz==(newJuz)) 8.dp else 0.dp,
                start = 8.dp,
            ),
            text = "$juz juz'",
            style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.3f))
        )
    }*/
    updateOldJuz(newJuz)
}
