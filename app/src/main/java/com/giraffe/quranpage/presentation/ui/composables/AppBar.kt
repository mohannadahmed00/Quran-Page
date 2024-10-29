package com.giraffe.quranpage.presentation.ui.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Bookmark
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.unit.dp
import com.giraffe.quranpage.domain.entities.VerseEntity

@Composable
fun AppBar(
    bookmarkedVerse: VerseEntity? = null,
    onMenuClick: () -> Unit,
    onSearchClick: () -> Unit,
    onBookmarkClick: () -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }
    Row(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .fillMaxWidth()
            .clickable(
                interactionSource = interactionSource,
                indication = null
            ) {}
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            modifier = Modifier
                .size(30.dp)
                .clickable(onClick = onMenuClick),
            imageVector = Icons.Rounded.Menu, contentDescription = "Menu",
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)),

            )
        Image(
            modifier = Modifier
                .size(30.dp)
                .clickable(onClick = onBookmarkClick),
            imageVector = Icons.Rounded.Bookmark,
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = if (bookmarkedVerse != null) 0.7f else 0.2f)),
            contentDescription = "ic_bookmark"
        )
        Image(
            modifier = Modifier
                .size(30.dp)
                .clickable(onClick = onSearchClick),
            imageVector = Icons.Rounded.Search, contentDescription = "Search",
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)),
        )
    }
}