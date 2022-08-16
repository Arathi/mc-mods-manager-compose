package com.undsf.mc.modsmgr.curseforge

import com.undsf.mc.modsmgr.curseforge.requests.SearchMods
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertNotEquals
import kotlin.test.assertNotNull

class ApiClientTests {
    lateinit var apiClient: ApiClient

    @BeforeTest
    fun init() {
        apiClient = ApiClient("\$2a\$10\$xbPxK4A7YMD3ZffICW3iWOPJEyweDCMRGg/S01R2WsY9Zbf2Tw8W2")
        println("apiClient创建完成")
    }

    @Test
    fun testSearchMods() {
        val conditions: SearchMods = SearchMods(
            searchFilter = "jei"
        )
        val mods = apiClient.searchMods(conditions)
        assertNotNull(mods, "获取到的mods不能为空")
        assertNotEquals(0, mods.size, "获取到的mods数量不能为0")
        println("查询测试完成")
    }
}