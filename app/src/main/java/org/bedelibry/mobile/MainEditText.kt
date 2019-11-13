package org.bedelibry.mobile

import android.content.Context
import android.graphics.Color
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.AttributeSet
import android.view.KeyEvent
import android.view.KeyEvent.KEYCODE_DEL
import android.view.KeyEvent.KEYCODE_SLASH
import android.view.inputmethod.InputConnection
import android.view.inputmethod.InputConnectionWrapper
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import java.util.*


class MainEditText : EditText {

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {}

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}

    constructor(context: Context) : super(context) {}

    override fun onCreateInputConnection(outAttrs: EditorInfo): InputConnection {
        return MainInputConnection(
            super.onCreateInputConnection(outAttrs),
            true
        )
    }

    private inner class MainInputConnection(target: InputConnection, mutable: Boolean) :
        InputConnectionWrapper(target, mutable) {

        override fun sendKeyEvent(event: KeyEvent): Boolean {
            if (event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_DEL && this@MainEditText.getText().toString().filter { char -> char != ' ' } == "?-") {
                this@MainEditText.setText("")
                // Un-comment if you wish to cancel the backspace:
                return false
            }
            return super.sendKeyEvent(event)
        }

        override fun deleteSurroundingText(beforeLength: Int, afterLength: Int): Boolean {
            // magic: in latest Android, deleteSurroundingText(1, 0) will be called for backspace
            return sendKeyEvent(
                    KeyEvent(
                        KeyEvent.ACTION_DOWN,
                        KeyEvent.KEYCODE_DEL
                    )
                )
        }


    }

}