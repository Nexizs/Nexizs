package eu.kanade.presentation.history

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLocale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import eu.kanade.domain.ui.UiPreferences
import eu.kanade.presentation.components.AppBar
import eu.kanade.presentation.components.AppBarActions
import eu.kanade.presentation.components.relativeDateText
import eu.kanade.presentation.history.components.HistoryItem
import eu.kanade.presentation.util.rememberIs24HourSystem
import eu.kanade.tachiyomi.ui.history.HistoryViewModel
import kotlinx.datetime.LocalDate
import mihon.app.di.appGraph
import mihon.icons.materialsymbols.MaterialSymbols
import mihon.icons.materialsymbols.rounded.DeleteSweep
import tachiyomi.domain.history.model.HistoryWithRelations
import tachiyomi.i18n.MR
import tachiyomi.presentation.core.components.FastScrollLazyColumn
import tachiyomi.presentation.core.components.ListGroupHeader
import tachiyomi.presentation.core.components.material.Scaffold
import tachiyomi.presentation.core.i18n.stringResource
import tachiyomi.presentation.core.screens.EmptyScreen
import tachiyomi.presentation.core.screens.LoadingScreen
import tachiyomi.presentation.core.util.collectAsState

@Composable
fun HistoryScreen(
    state: HistoryViewModel.State,
    snackbarHostState: SnackbarHostState,
    onSearchQueryChange: (String?) -> Unit,
    onClickCover: (mangaId: Long) -> Unit,
    onClickResume: (mangaId: Long, chapterId: Long) -> Unit,
    onClickFavorite: (mangaId: Long) -> Unit,
    onDialogChange: (HistoryViewModel.Dialog?) -> Unit,
) {
    Scaffold(
        topBar = { _ ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.background)
                    .statusBarsPadding()
                    .padding(horizontal = 16.dp, vertical = 2.dp),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                AppBarActions(
                    listOf(
                        AppBar.Action(
                            title = stringResource(MR.strings.pref_clear_history),
                            icon = MaterialSymbols.Rounded.DeleteSweep,
                            onClick = {
                                onDialogChange(HistoryViewModel.Dialog.DeleteAll)
                            },
                        ),
                    ),
                )
            }
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
    ) { contentPadding ->
        val listPadding = PaddingValues(bottom = contentPadding.calculateBottomPadding() + 96.dp)
        Column(modifier = Modifier.padding(top = contentPadding.calculateTopPadding())) {
            HistoryHeader(
                searchQuery = state.searchQuery,
                onSearchQueryChange = onSearchQueryChange,
            )
            Box(modifier = Modifier.weight(1f)) {
                state.list.let {
                    if (it == null) {
                        LoadingScreen()
                    } else if (it.isEmpty()) {
                        val msg = if (!state.searchQuery.isNullOrEmpty()) {
                            MR.strings.no_results_found
                        } else {
                            MR.strings.information_no_recent_manga
                        }
                        EmptyScreen(
                            stringRes = msg,
                        )
                    } else {
                        HistoryScreenContent(
                            history = it,
                            contentPadding = listPadding,
                            onClickCover = { history -> onClickCover(history.mangaId) },
                            onClickResume = { history -> onClickResume(history.mangaId, history.chapterId) },
                            onClickDelete = { item -> onDialogChange(HistoryViewModel.Dialog.Delete(item)) },
                            onClickFavorite = { history -> onClickFavorite(history.mangaId) },
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun HistoryHeader(
    searchQuery: String?,
    onSearchQueryChange: (String?) -> Unit,
) {
    val glassShape = RoundedCornerShape(28.dp)
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
    ) {
        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = "Recents",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(glassShape)
                .background(Color(0x33A78BFA))
                .border(width = 1.dp, color = Color(0x55FFFFFF), shape = glassShape)
                .padding(horizontal = 20.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                painter = painterResource(android.R.drawable.ic_menu_search),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                modifier = Modifier.size(20.dp),
            )
            Spacer(modifier = Modifier.width(12.dp))
            Box(
                modifier = Modifier.weight(1f),
                contentAlignment = Alignment.CenterStart,
            ) {
                BasicTextField(
                    value = searchQuery.orEmpty(),
                    onValueChange = { onSearchQueryChange(it.ifEmpty { null }) },
                    singleLine = true,
                    textStyle = TextStyle(
                        color = MaterialTheme.colorScheme.onBackground,
                        fontSize = 16.sp,
                    ),
                    cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                    modifier = Modifier.fillMaxWidth(),
                    decorationBox = { innerTextField ->
                        Box(contentAlignment = Alignment.CenterStart) {
                            if (searchQuery.isNullOrEmpty()) {
                                Text(
                                    text = "Search history",
                                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.55f),
                                    fontSize = 16.sp,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                )
                            }
                            innerTextField()
                        }
                    },
                )
            }
        }

        Spacer(modifier = Modifier.height(6.dp))
    }
}

@Composable
private fun HistoryScreenContent(
    history: List<HistoryUiModel>,
    contentPadding: PaddingValues,
    onClickCover: (HistoryWithRelations) -> Unit,
    onClickResume: (HistoryWithRelations) -> Unit,
    onClickDelete: (HistoryWithRelations) -> Unit,
    onClickFavorite: (HistoryWithRelations) -> Unit,
) {
    val context = LocalContext.current
    val locale = LocalLocale.current
    val is24HourSystem = rememberIs24HourSystem()
    val timeFormat by remember { context.appGraph.uiPreferences.timeFormat }.collectAsState()
    val timeFormatter = remember(timeFormat, locale, is24HourSystem) {
        UiPreferences.timeFormat(timeFormat, locale.platformLocale, is24HourSystem)
    }

    FastScrollLazyColumn(
        contentPadding = contentPadding,
    ) {
        items(
            items = history,
            key = { "history-${it.hashCode()}" },
            contentType = {
                when (it) {
                    is HistoryUiModel.Header -> "header"
                    is HistoryUiModel.Item -> "item"
                }
            },
        ) { item ->
            when (item) {
                is HistoryUiModel.Header -> {
                    ListGroupHeader(
                        modifier = Modifier.animateItem(),
                        text = relativeDateText(item.date),
                    )
                }
                is HistoryUiModel.Item -> {
                    val value = item.item
                    HistoryItem(
                        modifier = Modifier.animateItem(),
                        history = value,
                        timeFormatter = timeFormatter,
                        onClickCover = { onClickCover(value) },
                        onClickResume = { onClickResume(value) },
                        onClickDelete = { onClickDelete(value) },
                        onClickFavorite = { onClickFavorite(value) },
                    )
                }
            }
        }
    }
}

sealed interface HistoryUiModel {
    data class Header(val date: LocalDate) : HistoryUiModel
    data class Item(val item: HistoryWithRelations) : HistoryUiModel
}
