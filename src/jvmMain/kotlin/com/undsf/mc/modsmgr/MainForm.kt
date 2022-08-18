package com.undsf.mc.modsmgr

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.VerticalAlignmentLine
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogState
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupPositionProvider
import com.undsf.mc.modsmgr.curseforge.ApiClient
import com.undsf.mc.modsmgr.curseforge.requests.SearchMods
import com.undsf.mc.modsmgr.curseforge.responses.Mod
import com.undsf.mc.modsmgr.curseforge.responses.ModAuthor
import com.undsf.mc.modsmgr.curseforge.responses.ModFile
import com.undsf.mc.modsmgr.ui.ComboBox
import com.undsf.mc.modsmgr.ui.ComboBoxData
import org.w3c.dom.Text

class MainForm(
    val curseForgeApi: ApiClient
) {
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

    @Composable
    fun ModDownloadDialog(mod: Mod, showDialog: MutableState<Boolean>) {
        val dialogState = remember { mutableStateOf(DialogState()) }
        val downloadUrl = remember { mutableStateOf("") }

        fun onModVersionChanged(index: Int, text: String, value: Any) {
            if (value is ModFile) {
                val url = value.downloadUrl
                if (url != null) {
                    downloadUrl.value = url
                    println("更新下载地址为：${url}")
                }
            }
        }

        if (showDialog.value) {
            Dialog(
                onCloseRequest = {
                    println("关闭下载对话框")
                    showDialog.value = false
                },
                state = dialogState.value
            ) {
                val comboBoxModVersion = remember {
                    mutableStateOf(
                        ComboBoxData()
                    )
                }

                if (mod.latestFiles != null) {
                    for (file in mod.latestFiles!!) {
                        comboBoxModVersion.value.add(file.displayName!!, file)
                    }
                }

                Column {
                    Row {
                        Text("Mod版本")
                        ComboBox(comboBoxModVersion, ::onModVersionChanged)
                    }
                    Column(horizontalAlignment = Alignment.End, modifier = Modifier.fillMaxWidth()) {
                        Button(onClick = {
                            println("下载MOD：${downloadUrl.value}")
                        }) {
                            Text("下载")
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun ModInfo(mod: Mod) {
        val dialogVisable = remember { mutableStateOf(false) }

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

        Row (modifier = Modifier.border(1.dp, Color.Gray).fillMaxWidth().padding(5.dp).clickable(true, onClick = {
            dialogVisable.value = true
        })) {
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxHeight().padding(end = 10.dp)) {
                Icon(
                    Icons.Default.Add, "",
                    modifier = Modifier.background(Color.Black).size(48.dp)
                )
            }

            Column {
                Row {
                    Row {
                        Text(authorName)
                        Text(" / ")
                        Text(mod.name!!, modifier = Modifier.padding(end = 10.dp))

                        Text("id: ${mod.id.toString()}", color = Color.Gray, modifier = Modifier.padding(end = 10.dp))
                        Text("slug: ${mod.slug!!}", color = Color.Gray)
                    }
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    }
                }
                Row {
                    Text("下载量：${downloadCount}${downloadCountUnit}", modifier = Modifier.padding(end = 5.dp))
                    Text("创建时间：${createdAt}", modifier = Modifier.padding(end = 5.dp))
                    Text("更新时间：${updatedAt}")
                }
                Row {
                    Text(mod.summary!!)
                }
            }
        }

        ModDownloadDialog(mod, dialogVisable)
    }

    @Composable
    fun ModInfoList(mods: SnapshotStateList<Mod>) {
        LazyColumn() {
            items(mods) {
                Column(modifier = Modifier.padding(bottom = 3.dp)) {
                    ModInfo(it)
                }
            }
        }
    }

    @Composable
    fun app() {
        val searchModsConditions = remember { mutableStateOf(SearchMods(
            gameVersion = "1.19.2",
            sortField = 2,
            sortOrder = "desc",
            modLoaderType = 1
        )) }
        val searchBy = remember { mutableStateOf("关键字") }
        val searchResults = remember { mutableStateListOf<Mod>() }
        val textFieldkeyWord = remember { mutableStateOf("jei") }

        val comboBoxClassId = remember { mutableStateOf(
            ComboBoxData(
                mutableStateListOf("MOD", "材质", "地图", "音效"),
                mutableStateListOf(1, 2, 3, 4)
            )
        ) }

        val comboBoxGameVersion = remember { mutableStateOf(
            ComboBoxData(
                gameVersions,
                "请选择MC版本",
                128.dp
            )
        ) }

        val comboBoxSortField = remember {mutableStateOf(
            ComboBoxData(
                sortFields,
                "请选择排序字段",
                240.dp
            )
        ) }

        val comboBoxSortOrder = remember {mutableStateOf(
            ComboBoxData(
                sortOrders,
                "请选择排序方式",
                80.dp,
                1
            )
        ) }

        val comboBoxModLoaderType = remember {mutableStateOf(
            ComboBoxData(
                modLoaders,
                "请选择Mod加载器",
                100.dp,
                0
            )
        ) }

        fun onClassIdChanged(index: Int, text: String, value: Any) {
            println("classId选项改变：index=${index}, 显示内容=${text}, 对应值=${value}")
            searchModsConditions.value.categoryId = value as Int
        }

        fun onGameVersionChanged(index: Int, text: String, value: Any) {
            println("gameVersion选项改变：index=${index}, 显示内容=${text}, 对应值=${value}")
            searchModsConditions.value.gameVersion = value as String
        }

        fun onSortFieldChanged(index: Int, text: String, value: Any) {
            println("sortField选项改变：index=${index}, 显示内容=${text}, 对应值=${value}")
            searchModsConditions.value.sortField = value as Int
        }

        fun onSortOrderChanged(index: Int, text: String, value: Any) {
            println("sortOrder选项改变：index=${index}, 显示内容=${text}, 对应值=${value}")
            searchModsConditions.value.sortOrder = value as String
        }

        fun onModLoaderTypeChanged(index: Int, text: String, value: Any) {
            println("modLoaderType选项改变：index=${index}, 显示内容=${text}, 对应值=${value}")
            searchModsConditions.value.modLoaderType = value as Int
        }

        MaterialTheme {
            Row(modifier = Modifier.padding(10.dp)) {
                Column {
                    // Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(bottom = 10.dp)) {
                    //     Text("分类", modifier = Modifier.width(100.dp))
                    //     ComboBox(
                    //         bind = comboBoxClassId,
                    //         onSelectedIndexChanged = ::onClassIdChanged
                    //     )
                    // }

                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(bottom = 10.dp)) {
                        Text("游戏版本", modifier = Modifier.width(100.dp))
                        ComboBox(comboBoxGameVersion, ::onGameVersionChanged)
                        // Text("gameVersionTypeId")
                    }

                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(bottom = 10.dp)) {
                        Text(searchBy.value, modifier = Modifier.width(100.dp).clickable(true, onClick = {
                            searchBy.value = if (searchBy.value == "slug") "关键字" else "slug"
                        }))
                        TextField(textFieldkeyWord.value,
                            modifier = Modifier.width(100.dp).height(48.dp),
                            onValueChange = {
                                textFieldkeyWord.value = it
                            }
                        )
                    }

                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(bottom = 10.dp)) {
                        Text("排序规则", modifier = Modifier.width(100.dp))
                        ComboBox(comboBoxSortField, ::onSortFieldChanged)
                        ComboBox(comboBoxSortOrder, ::onSortOrderChanged)
                    }

                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(bottom = 10.dp)) {
                        Text("MOD加载器", modifier = Modifier.width(100.dp))
                        ComboBox(comboBoxModLoaderType, ::onModLoaderTypeChanged)
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                         Row {
                             Button(onClick = {
                                 if (searchBy.value == "slug") {
                                     searchModsConditions.value.slug = if (textFieldkeyWord.value != "") textFieldkeyWord.value else null
                                     searchModsConditions.value.searchFilter = null
                                 }
                                 else {
                                     searchModsConditions.value.slug = null
                                     searchModsConditions.value.searchFilter = if (textFieldkeyWord.value != "") textFieldkeyWord.value else null
                                 }
                                 val mods = curseForgeApi.searchMods(searchModsConditions.value)
                                 searchResults.clear()
                                 searchResults.addAll(mods)
                             }) {
                                 Text("搜索")
                             }
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

