package com.undsf.mc.modsmgr.curseforge.responses

data class ModAsset(
    var id: Int?,
    var modId: Int?,
    var title: String?,
    var description: String?,
    var thumbnailUrl: String?,
    var url: String?
) {
}