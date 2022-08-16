package com.undsf.mc.modsmgr.curseforge.responses

data class Pagination(
    var index: Int?,
    var pageSize: Int?,
    var resultCount: Int?,
    var totalCount: Long?
)
