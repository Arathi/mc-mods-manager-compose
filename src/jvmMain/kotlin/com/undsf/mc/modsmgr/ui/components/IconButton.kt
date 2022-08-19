package com.undsf.mc.modsmgr.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
fun IconButton(
    icon: ImageVector,
    onClick: () -> Unit,
    description: String = "",
    modifier: Modifier = Modifier.padding(3.dp)) {
    Icon(
        icon, description,
        modifier = modifier.clickable(true, onClick = onClick)
    )
}
