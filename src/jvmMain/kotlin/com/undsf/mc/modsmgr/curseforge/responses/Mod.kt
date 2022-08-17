package com.undsf.mc.modsmgr.curseforge.responses

data class Mod(
    var id: Int? = null,
    // gameId
    var name: String? = null,
    var slug: String? = null,
    // links
    var summary: String? = null,
    // status,
    var downloadCount: Long? = null,
    // isFeatured
    // primaryCategoryId
    // categories
    // classId
    var authors: List<ModAuthor>? = null,
    var logo: ModAsset? = null,
    // screenshots
    var mainFileId: Int? = null,
    var latestFiles: List<ModFile>? = null,
    // var latestFilesIndexes: List<FileIndex>?,
    var dateCreated: String? = null,
    var dateModified: String? = null,
    var dateReleased: String? = null,
    // allowModDistribution
    // gamePopularityRank
    // isAvailable
    // thumbsUpCount
) {

}