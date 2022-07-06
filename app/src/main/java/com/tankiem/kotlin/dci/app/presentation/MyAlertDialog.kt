package com.tankiem.kotlin.dci.app.presentation

import android.content.Context
import androidx.appcompat.app.AlertDialog
import com.tankiem.kotlin.dci.R

class MyAlertDialog {
    companion object {
        fun showAlertDialog(context: Context, title: String, message: String) {
            AlertDialog.Builder(context).apply {
                setTitle(title)
                setMessage(message)
                setPositiveButton(R.string.ok) { dialog, _ ->
                    dialog.cancel()
                }
                show()
            }
        }
    }
}