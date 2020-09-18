package com.example.firestore2

import com.google.firebase.firestore.ServerTimestamp
import java.util.*

class Item {
    var singer: String=""
    var editSinger: String=""
    var text2: String=""

    @ServerTimestamp
    var createdAt: Date? = null
}

