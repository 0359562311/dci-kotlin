package com.tankiem.kotlin.dci.utils

import com.tankiem.kotlin.dci.app.network.responses.Session
import com.tankiem.kotlin.dci.app.network.responses.Student


object GlobalVariable {
    var session: Session? = null
    var currentUser: Student? = null

    fun reset() {
        session = null
        currentUser = null
    }
}