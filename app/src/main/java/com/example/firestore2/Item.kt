package com.example.firestore2

import com.google.firebase.firestore.ServerTimestamp
import java.util.*

class Item {
    var id: String = UUID.randomUUID().toString()
    var singer: String=""
    var editSinger: String=""
    var text2: String=""
    var deletedAt: Date? = null

    @ServerTimestamp
    var createdAt: Date? = null
}

