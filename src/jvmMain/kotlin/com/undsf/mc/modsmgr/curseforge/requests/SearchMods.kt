package com.undsf.mc.modsmgr.curseforge.requests

import okhttp3.FormBody

data class SearchMods(
    val gameId: Int = 432,
    var classId: Int? = null,
    var categoryId: Int? = null,
    var gameVersion: String? = null,
    var searchFilter: String? = null,
    var sortField: Int? = null,
    var sortOrder: String? = null,
    var modLoaderType: Int? = null,
    var gameVersionTypeId: Int? = null,
    var slug: String? = null,
    var index: Int? = null,
    var pageSize: Int? = null
) {
    fun toFormBody() : FormBody {
        val builder = FormBody.Builder()

        builder.add("gameId", gameId.toString())

        if (classId != null) {
            builder.add("classId", classId.toString())
        }

        if (categoryId != null) {
            builder.add("categoryId", categoryId.toString())
        }

        if (gameVersion != null) {
            builder.add("gameVersion", gameVersion!!)
        }

        if (searchFilter != null) {
            builder.add("searchFilter", searchFilter!!)
        }

        if (sortField != null) {
            builder.add("sortField", sortField.toString())
        }

        if (sortOrder != null) {
            builder.add("sortOrder", sortOrder!!)
        }

        if (modLoaderType != null) {
            builder.add("modLoaderType", modLoaderType.toString())
        }

        if (gameVersionTypeId != null) {
            builder.add("gameVersionTypeId", gameVersionTypeId.toString())
        }

        if (slug != null) {
            builder.add("slug", slug!!)
        }

        if (index != null) {
            builder.add("index", index.toString())
        }

        if (pageSize != null) {
            builder.add("pageSize", pageSize.toString())
        }

        return builder.build()
    }
}
