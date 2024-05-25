package com.example.storygram.utils

import com.example.storygram.data.remote.response.ListStoryItem

object DataDummy {
    fun generateDummyListStoryResponse(): List<ListStoryItem> {
        val items: MutableList<ListStoryItem> = arrayListOf()
        for (i in 0..100) {
            val story = ListStoryItem(
                "id$i",
                "author $i",
                "description $i",
                i + 1.toFloat(),
                "id$i",
                i + 1.toFloat(),
            )
            items.add(story)
        }
        return items
    }
}