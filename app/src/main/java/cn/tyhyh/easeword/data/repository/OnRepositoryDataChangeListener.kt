package cn.tyhyh.easeword.data.repository

import org.litepal.crud.LitePalSupport

/**
 * author: tiny
 * created on: 20-4-1 下午2:21
 */
interface OnRepositoryDataChangeListener<T : LitePalSupport> {
    fun onUpdate(date: T?)

    fun onDelete(data: T?)

    fun onInsert(data: T?)

    fun onDeleteCollection(dataCollection: Collection<T>)
}

open class SimpleRepositoryDataChangeListener<T : LitePalSupport> : OnRepositoryDataChangeListener<T> {
    override fun onUpdate(date: T?) {}

    override fun onDelete(data: T?) {}

    override fun onInsert(data: T?) {}

    override fun onDeleteCollection(dataCollection: Collection<T>) {}
}