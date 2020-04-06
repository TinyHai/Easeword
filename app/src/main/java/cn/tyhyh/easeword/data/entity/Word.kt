package cn.tyhyh.easeword.data.entity

import org.litepal.crud.LitePalSupport

data class Word(
    var chapter: Chapter?,
    var text: String,
    val essays: ArrayList<Essay>
) : LitePalSupport() {
    var id: Long = 0
        private set

    constructor(): this(null, " ", ArrayList(0))
}