package com.undsf.mc.modsmgr.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

class ComboBoxData(
    var itemTextList: SnapshotStateList<String> = mutableStateListOf(),
    var itemValueList: SnapshotStateList<Any> = mutableStateListOf(),
    var defaultText: String = "请选择",
    var width: Dp? = null,
    var selectedIndex: Int = -1
) {
    constructor(items: Map<String, Any>, defaultText: String = "请选择", width: Dp = 100.dp, selectedIndex: Int = -1) : this(
        defaultText = defaultText,
        width = width,
        selectedIndex = selectedIndex
    ) {
        itemTextList.clear()
        itemValueList.clear()
        for (entry in items.entries) {
            val text = entry.key
            val value = entry.value
            itemTextList.add(text)
            itemValueList.add(value)
        }
    }

    fun selectedValue(): Any? {
        if (selectedIndex in 0 until itemValueList.size) {
            return itemValueList[selectedIndex]
        }
        return null
    }

    fun add(text: String, value: Any) {
        itemTextList.add(text)
        itemValueList.add(value)
    }
}

@Composable
fun ComboBox(bind: MutableState<ComboBoxData>,
             onSelectedIndexChanged: (Int, String, Any) -> Unit,
             modifier: Modifier = Modifier.padding(end = 10.dp)) {
    val expended = remember { mutableStateOf(false) }

    fun expend() {
        println("展开下拉框")
        expended.value = true
    }

    fun collapse() {
        println("收起下拉框")
        expended.value = false
    }

    Box(modifier = modifier) {
        Row(modifier = Modifier.border(1.dp, color = Color.Gray)) {
            val selectedIndex = bind.value.selectedIndex
            val itemAmount = bind.value.itemTextList.size
            val displayText = if (selectedIndex in 0 until itemAmount) {
                bind.value.itemTextList[selectedIndex]
            }
            else {
                bind.value.defaultText
            }

            val textModifier = Modifier.clickable(true, onClick = ::expend)
            if (bind.value.width != null) {
                textModifier.width(bind.value.width!!)
            }

            Text(
                displayText,
                modifier = textModifier
            )

            Icon(
                Icons.Default.ArrowDropDown, "",
                modifier = Modifier.clickable(true, onClick = ::expend)
            )

            DropdownMenu(expended.value, onDismissRequest = ::expend) {
                for (index in 0 until bind.value.itemTextList.size) {
                    DropdownMenuItem(onClick = {
                        onSelectedIndexChanged(
                            index,
                            bind.value.itemTextList[index],
                            bind.value.itemValueList[index]
                        )
                        bind.value.selectedIndex = index
                        collapse()
                    }) {
                        Text(bind.value.itemTextList[index])
                    }
                }
            }
        }
    }
}
