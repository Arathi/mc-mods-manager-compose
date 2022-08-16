package com.undsf.mc.modsmgr

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.undsf.mc.modsmgr.curseforge.ApiClient
import com.undsf.mc.modsmgr.curseforge.requests.SearchMods
import com.undsf.mc.modsmgr.curseforge.responses.Mod
import com.undsf.mc.modsmgr.curseforge.responses.ModAuthor

class MainForm(
    val curseForgeApi: ApiClient
) {
    var searchMods = SearchMods()

    val gameVersions = mapOf(
        "1.7.10" to "1.7.10",
        "1.12.2" to "1.12.2",
        "1.14.4" to "1.14.4",
        "1.16.5" to "1.16.5",
        "1.18.2" to "1.18.2",
        "1.19" to "1.19",
        "1.19.1" to "1.19.1",
        "1.19.2" to "1.19.2",
    )

    val sortFields = mapOf(
        "特性（Featured）" to 1,
        "流行度（Popularity）" to 2,
        "最后更新（LastUpdated）" to 3,
        "名称（Name）" to 4,
        "作者（Author）" to 5,
        "总下载量（TotalDownloads）" to 6,
        "分类（Category）" to 7,
        "游戏版本（GameVersion）" to 8
    )

    val sortOrders = mapOf(
        "升序" to "asc",
        "降序" to "desc"
    )

    val modLoaders = mapOf(
        "任意" to 0,
        "Forge" to 1,
        "Cauldron" to 2,
        "LiteLoader" to 3,
        "Fabric" to 4,
        "Quilt" to 5
    )

    private fun onDdmGameVersionChanged(version: String) {
        searchMods.gameVersion = version
    }

    private fun onBtnSearchModsClicked() {
        println("点击搜索按钮")
        curseForgeApi.searchMods(searchMods)
    }

    @Composable
    fun DropDownTextBox(bind: String, items: Map<String, Any?>, defaultText: String = "请选择") {
        val expended = remember { mutableStateOf(false) }
        val menuItems = remember { mutableStateOf(mutableMapOf<String, Any?>()) }
        val text = remember { mutableStateOf(defaultText) }

        if (items.isNotEmpty()) {
            menuItems.value.clear()
            for (entry in items.entries) {
                menuItems.value[entry.key] = entry.value
            }
        }

        Text(
            text = text.value,
            modifier = Modifier.clickable(true, onClick = {
                expended.value = true
            }).background(Color.Gray)
        )

        DropdownMenu(
            expended.value,
            onDismissRequest = {
                expended.value = false
            }
        ) {
            for (entry in menuItems.value.entries) {
                val display = entry.key
                var value = entry.value
                if (value == null) {
                    value = display
                }
                DropdownMenuItem(onClick = {
                    when (bind) {
                        "gameVersion" -> searchMods.gameVersion = value as String?
                        "sortField" -> searchMods.sortField = value as Int?
                        "sortOrder" -> searchMods.sortOrder = value as String?
                        "modLoaderType" -> searchMods.modLoaderType = value as Int?
                    }
                    expended.value = false
                    text.value = display
                }) {
                    Text(display)
                }
            }
        }
    }

    @Deprecated("用DropDownTextBox替代")
    @Composable
    fun DropDownButton(bind: String, items: Map<String, Any?>, defaultText: String = "请选择") {
        val expended = remember { mutableStateOf(false) }
        val menuItems = remember { mutableStateMapOf<String, Any?>() }
        val text = remember { mutableStateOf(defaultText) }

        if (items.isNotEmpty()) {
            menuItems.clear()
            for (entry in items.entries) {
                menuItems[entry.key] = entry.value
            }
        }

        Button(onClick = {
            expended.value = true
        }) {
            Text(text.value)
            DropdownMenu(
                expended.value,
                onDismissRequest = {
                    expended.value = false
                }
            ) {
                for (entry in menuItems.entries) {
                    val display = entry.key
                    var value = entry.value
                    if (value == null) {
                        value = display
                    }
                    DropdownMenuItem(onClick = {
                        when (bind) {
                            "gameVersion" -> searchMods.gameVersion = value as String?
                            "sortField" -> searchMods.sortField = value as Int?
                            "sortOrder" -> searchMods.sortOrder = value as String?
                            "modLoaderType" -> searchMods.modLoaderType = value as Int?
                        }
                        expended.value = false
                        text.value = display
                    }) {
                        Text(display)
                    }
                }
            }
        }
    }

    @Composable
    fun ModInfo(mod: Mod) {
        var authorName: String = "Unknown"
        if (mod.authors != null) {
            if ( mod.authors!!.isNotEmpty()) {
                authorName = mod.authors!![0].name!!
            }
        }

        var downloadCountUnit = ""
        var downloadCount = ""
        if (mod.downloadCount != null) {
            if (mod.downloadCount!! > 100000000) {
                downloadCountUnit = "亿"
                downloadCount = String.format("%.2f", mod.downloadCount!! / 100000000f)
            }
            else if (mod.downloadCount!! > 10000) {
                downloadCountUnit = "万"
                downloadCount = String.format("%.2f", mod.downloadCount!! / 10000f)
            }
        }

        var createdAt = "未知"
        if (mod.dateCreated != null && mod.dateCreated!!.isNotEmpty()) {
            createdAt = mod.dateCreated!!.substring(0, 10)
        }

        var updatedAt = "未知"
        if (mod.dateReleased != null && mod.dateReleased!!.isNotEmpty()) {
            updatedAt = mod.dateReleased!!.substring(0, 10)
        }

        Column {
            Row {
                Row {
                    Text(authorName)
                    Text(" / ")
                    Text(mod.name!!)
                }
                Row {
                    Text(mod.id.toString())
                    Text(" / ")
                    Text(mod.slug!!)
                }
            }
            Row {
                Text("下载量：${downloadCount}${downloadCountUnit}")
                Text("创建时间：${createdAt}")
                Text("更新时间：${updatedAt}")
            }
        }
    }

    @Composable
    fun ModInfoList(mods: SnapshotStateList<Mod>) {
        LazyColumn {
            items(mods) {
                ModInfo(it)
            }
        }
    }

    @Composable
    fun app() {
        val switchSearchBySlug = remember { mutableStateOf(false) }
        val searchBy = remember { mutableStateOf("关键字") }
        val searchResults = remember { mutableStateListOf<Mod>() }


        MaterialTheme {
            Row {
                Column {
                    Row {
                        // Text("分类")
                        // Text("classId")
                        // Text("categoryId")
                    }
                    Row {
                        Text("游戏版本")
                        DropDownTextBox("gameVersion", gameVersions, "请选择游戏版本")
                        // Text("gameVersionTypeId")
                    }
                    Row {
                        // Text("通过${searchBy.value}查找")
                        TextField("jei",
                            modifier = Modifier.width(100.dp).height(48.dp),
                            onValueChange = {}
                        )
                        Switch(switchSearchBySlug.value, onCheckedChange = {
                            switchSearchBySlug.value = it;
                            searchBy.value = if (it) "slug" else "关键字"
                        })
                        Text("通过${searchBy.value}查找")
                    }
                    Row {
                        Text("排序规则")
                        // Text("sortField")
                        // Text("sortOrder")
                        DropDownTextBox("sortField", sortFields, "请选择排序字段")
                        DropDownTextBox("sortOrder", sortOrders, "请选择顺序")
                    }
                    Row {
                        Text("MOD加载器")
                        DropDownTextBox("modLoaderType", modLoaders, "请选择MOD加载器")
                    }
                    Row {
                        Button(onClick = {
                            val mod = Mod(
                                id = 1,
                                name = "Just Enough Items (JEI)",
                                slug = "jei",
                                authors = listOf(ModAuthor(name = "mezz")),
                                downloadCount = 183_600_000,
                                dateCreated = "2012-08-16T17:20:00Z",
                                dateModified = "2022-08-16T17:20:00Z",
                                dateReleased = "2022-08-16T17:20:00Z"
                            )

                            searchResults.add(mod)
                            println("增加数据")
                        }) {
                            Text("搜索")
                        }
                    }

                    Divider()
                    Row {
                        ModInfoList(searchResults)
                    }
                }
            }
        }
    }
}

