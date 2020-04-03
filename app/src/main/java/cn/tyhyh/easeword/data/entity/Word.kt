package cn.tyhyh.easeword.data.entity

import org.litepal.crud.LitePalSupport

data class Word(
    var chapter: Chapter?,
    var text: String,
    val essays: ArrayList<Essay>
) : LitePalSupport() {
    val id: Long = 0

    constructor(): this(null, " ", ArrayList(0))
}