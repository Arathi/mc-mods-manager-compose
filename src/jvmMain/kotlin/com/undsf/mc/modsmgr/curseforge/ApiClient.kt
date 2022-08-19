package com.undsf.mc.modsmgr.curseforge

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.undsf.mc.modsmgr.curseforge.requests.SearchMods
import com.undsf.mc.modsmgr.curseforge.responses.Mod
import com.undsf.mc.modsmgr.curseforge.responses.ModFile
import com.undsf.mc.modsmgr.curseforge.responses.PaginationResponse
import okhttp3.OkHttpClient
import okhttp3.Request
import java.net.URL

class ApiClient(
    var apiKey: String = "",
    private val baseUrl: String = "https://api.curseforge.com",
    private val httpClient: OkHttpClient = OkHttpClient(),
    private val gson: Gson = Gson()
) {
    fun searchMods(conditions: SearchMods?) : List<Mod> {
        val mods = mutableListOf<Mod>()

        if (conditions == null) {
            return mods
        }

        val form = conditions.toFormBody()
        val query = StringBuilder()
        for (index in 0 until form.size) {
            if (index > 0) query.append("&")
            query.append(form.encodedName(index))
            query.append("=")
            query.append(form.encodedValue(index))
        }
        val url = URL("$baseUrl/v1/mods/search?${query}")

        val reqBuilder = Request.Builder()
        reqBuilder.url(url)
        reqBuilder.get()
        reqBuilder.addHeader("x-api-key", apiKey)
        val req = reqBuilder.build()

        val resp = httpClient.newCall(req).execute()
        val respBody = resp.body
        if (respBody != null) {
            val json = respBody.string()
            if ("" == json) {
                return mods
            }

            val type = object : TypeToken<PaginationResponse<Mod>>(){}.type
            val respMsg: PaginationResponse<Mod> = gson.fromJson(json, type)
            if (respMsg.data != null) {
                mods.addAll(respMsg.data!!)
            }
        }

        return mods
    }

    fun getModById(modId: Int) : Mod? {
        // TODO 暂未实现
        throw NotImplementedError()
    }

    fun getModFilesById(modId: Int) : List<ModFile> {
        val modFiles = mutableListOf<ModFile>()
        return modFiles.toList()
    }

    fun getModByClassIdAndSlug(classId: Int, slug: String) : Mod? {
        // TODO 暂未实现
        throw NotImplementedError()
    }
}