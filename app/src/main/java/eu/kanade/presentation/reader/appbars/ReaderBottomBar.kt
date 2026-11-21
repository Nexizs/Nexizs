package eu.kanade.presentation.reader.appbars

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import eu.kanade.tachiyomi.ui.reader.setting.ReaderOrientation
import eu.kanade.tachiyomi.ui.reader.setting.ReadingMode
import mihon.icons.custommaterialsymbols.CustomMaterialSymbols
import mihon.icons.custommaterialsymbols.rounded.CropOff
import mihon.icons.materialsymbols.MaterialSymbols
import mihon.icons.materialsymbols.rounded.Crop
import mihon.icons.materialsymbols.rounded.Settings
import tachiyomi.i18n.MR
import tachiyomi.presentation.core.i18n.stringResource

@Composable
fun ReaderBottomBar(
    readingMode: ReadingMode,
    onClickReadingMode: () -> Unit,
    orientation: ReaderOrientation,
    onClickOrientation: () -> Unit,
    cropEnabled: Boolean,
    onClickCropBorder: () -> Unit,
    onClickSettings: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val panelShape = RoundedCornerShape(32.dp)
    Row(
        modifier = modifier
            .pointerInput(Unit) {}
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clip(panelShape)
            .background(Color(0xCC1B1840))
            .border(width = 1.dp, color = Color(0x55FFFFFF), shape = panelShape)
            .padding(horizontal = 8.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        ReaderGlassIconButton(
            onClick = onClickReadingMode,
            icon = readingMode.icon,
            description = stringResource(MR.strings.viewer),
        )

        ReaderGlassIconButton(
            onClick = onClickOrientation,
            icon = orientation.icon,
            description = stringResource(MR.strings.rotation_type),
        )

        ReaderGlassIconButton(
            onClick = onClickCropBorder,
            icon = if (cropEnabled) MaterialSymbols.Rounded.Crop else CustomMaterialSymbols.Rounded.CropOff,
            description = stringResource(MR.strings.pref_crop_borders),
        )

        ReaderGlassIconButton(
            onClick = onClickSettings,
            icon = MaterialSymbols.Rounded.Settings,
            description = stringResource(MR.strings.action_settings),
        )
    }
}

@Composable
private fun ReaderGlassIconButton(
    onClick: () -> Unit,
    icon: ImageVector,
    description: String,
) {
    IconButton(
        onClick = onClick,
        modifier = Modifier
            .size(48.dp)
            .clip(CircleShape)
            .background(Color(0x33A78BFA))
            .border(width = 1.dp, color = Color(0x55FFFFFF), shape = CircleShape),
    ) {
        Icon(
            imageVector = icon,
            contentDescription = description,
            tint = Color(0xFFEDEBFF),
        )
    }
}
