package com.undsf.mc.modsmgr.curseforge.responses

data class ModFile(
    var id: Int? = null,
    // var gameId: Int,
    var modId: Int? = null,
    // isAvailable
    var displayName: String? = null,
    var fileName: String? = null,
    // var releaseType
    // fileStatus
    // hashes
    // fileDate
    // fileLength
    // downloadCount
    var downloadUrl: String? = null,
    var gameVersions: List<String>? = null,
    // sortableGameVersions
    // dependencies,
    // var exposeAsAlternative: Boolean?,
    // parentProjectFileId
    // alternateFileId
    // isServerPack
    // serverPackFileId
    // fileFingerprint
    // modules
) {
}