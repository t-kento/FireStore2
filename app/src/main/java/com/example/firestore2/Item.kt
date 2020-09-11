package com.example.firestore2

import com.google.firebase.Timestamp
import com.google.firebase.firestore.ServerTimestamp

class Item {
    var singer: String = ""
    var editSinger: String = ""
    var text2: String = ""
    @ServerTimestamp
    var createdAt: Timestamp? = null

    override fun toString(): String {
        return "singer:$singer editSinger:$editSinger text2:$text2"
    }
}
