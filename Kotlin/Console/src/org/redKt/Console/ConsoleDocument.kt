/**
 * Copyright (C) 2012 Ben Navetta <ben.navetta></ben.navetta>@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.redkt.Console


import javax.swing.event.CaretEvent
import javax.swing.event.CaretListener

import javax.swing.text.BadLocationException
import javax.swing.text.Caret
import javax.swing.text.DefaultStyledDocument
import javax.swing.text.MutableAttributeSet

import java.util.ArrayList

import java.awt.font.LineBreakMeasurer

class ConsoleDocument : DefaultStyledDocument(), CaretListener {
    private var caret: Caret? = null

    private var console: Console? = null

    var limit: Int = 0
        private set

    private var doFocus = true

    val userInput: String
        get() {
            try {
                return getText(limit, getLength() - limit)
            } catch (e: BadLocationException) {
                e.printStackTrace()
                return " "
            }

        }

    val isCursorValid: Boolean
        get() = caret!!.getDot() >= limit

    fun setConsole(console: Console) {
        this.console = console
    }

    fun setFocusAfterAppend(`var`: Boolean) {
        doFocus = `var`
    }

    fun write(text: String, attrs: MutableAttributeSet) {
        try {
            insertString(getLength(), text, attrs)
            limit = getLength()
            caret!!.setDot(limit)
        } catch (e: BadLocationException) {
            e.printStackTrace()
        }

        if (doFocus) {
            console!!.focus()
        }
    }

    fun writeUser(text: String, attrs: MutableAttributeSet) {
        try {
            insertString(getLength(), text, attrs)
            caret!!.setDot(getLength())
        } catch (e: BadLocationException) {
            e.printStackTrace()
        }

        if (doFocus) console!!.focus()
    }

    @Throws(BadLocationException::class)
    override fun remove(offs: Int, len: Int) {
        if (offs < limit) {
            return
        }
        super.remove(offs, len)
    }

    fun setCaret(caret: Caret) {
        this.caret = caret
    }

    fun makeCursorValid() {
        if (caret!!.getDot() < limit) {
            caret!!.setDot(limit)
        }
    }

    override fun caretUpdate(e: CaretEvent) {} // Moved to "MakeCursorValid" so that the user can still copy text

    companion object {

        private val serialVersionUID = -1270788544217141905L
    }

}
