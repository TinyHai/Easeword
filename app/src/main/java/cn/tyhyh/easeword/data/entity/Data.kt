package cn.tyhyh.easeword.data.entity

import org.litepal.annotation.Column
import org.litepal.crud.LitePalSupport

data class Data(
    @Column(unique = true) var sourceFile: String,
    var unlockedCount: Int,
    var chapters: ArrayList<Chapter>
) : LitePalSupport() {
    var id: Long = 0
        private set

    constructor(): this("", 1, ArrayList(0))
}