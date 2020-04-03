package cn.tyhyh.easeword.data.entity

import org.litepal.crud.LitePalSupport

/**
 * author: tiny
 * created on: 20-3-26 下午4:11
 */
data class Essay(val type: Int, var content: String) : LitePalSupport() {

    var id: Long = 0
        private set

    var word: Word? = null

    constructor() : this(null, TYPE_INVALID, "")

    constructor(word: Word?, type: Int, content: String): this(type, content) {
        this.word = word
    }



    companion object {
        const val TYPE_DRAWING = 0
        const val TYPE_NOTE = 1
        const val TYPE_INVALID = -1
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Essay

        if (id != other.id) return false
        if (type != other.type) return false
        if (content != other.content) return false

        return true
    }

    override fun hashCode(): Int {
        var result = type
        result = 31 * result + content.hashCode()
        result = 31 * result + id.hashCode()
        return result
    }
}