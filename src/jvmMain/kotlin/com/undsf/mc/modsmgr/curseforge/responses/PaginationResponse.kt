package com.undsf.mc.modsmgr.curseforge.responses

data class PaginationResponse<T>(
    var data: List<T>?,
    var pagination: Pagination?
) {
}