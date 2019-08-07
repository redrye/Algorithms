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

import javax.swing.*
import javax.swing.text.BadLocationException
import javax.swing.text.MutableAttributeSet
import javax.swing.text.StyleConstants
import java.awt.*
import java.awt.datatransfer.Clipboard
import java.awt.datatransfer.StringSelection
import java.awt.event.*
import java.util.ArrayList
import java.util.Arrays

//import java.awt.Component;

class Console : JTextPane, KeyListener, MouseWheelListener, ComponentListener, MouseListener {

    var consoleDocument: ConsoleDocument? = null
        private set        // Holder of all text on the window

    private var maxLinesPerScreen = 17    // This is default based on default window size.

    private var prompt: String? = null            // Structured as such: [connection][subprompt][prompt]
    private var subPrompt = ""        // Subprompt: one character at the end of the prompt
    var connection = "Home>"    // Computer name, IP, or Domain name (for use with networking)
    /**
     * Returns the current path.
     *
     * @author Joey Patel
     * @author pateljo@northvilleschools.net (valid until 06/18)
     * @returns the console's current filepath.
     */
    /**
     * Sets the directory filepath of the console's prompting system, then updates the
     * entire prompt.
     *
     * @author Joey Patel
     * @author pateljo@northvilleschools.net (valid until 06/18)
     * @param  path to become the new path
     */
    var path = "C:\\"
        set(path) {
            field = path
            this.prompt = this.connection + this.path + this.subPrompt
        }        // Current Directory

    private val f: Font?            // Keeps track of the current font for line counting.

    private var wasInFocus = true    // Used to focus the screen when typing/scrolling.

    private val prompts = ArrayList<String>()                // List of previously run commands
    private var DOCUMENT_HARDCOPY = ArrayList<String>()      // List of all lines since last cls.
    private var currentCommand =
        ""                                         // The current command being written, constantly being updated
    private var currentPosition =
        0                                            // The line, as referenced in "DOCUMENT_HARDCOPY" that is at the top of the window
    private var currentCommandnum =
        0                                          // The current command number, as referenced in "prompts," that the user is
    //  accessing, based on arrow keys.

    var processor: InputProcessor = NoOpInputProcessor()            // Processor of input, as name implies.

    var completionSource: CompletionSource = NoOpCompletionSource()

    private val defaultStyle: MutableAttributeSet = new MutibleAttributeSet()

    internal var mostRecentSelectedText = ""

    /**
     * Sets the maximum number of lines that can fit on a window of the specified height.
     * Note that this is the height of the text area, not the entire window. (do not send
     * the raw window height, subtract the borders.)
     *
     * @author Joey Patel
     * @author pateljo@northvilleschools.net (valid until 06/18)
     * @param  heightInPixels   The height of the window, in pixels
     */
    fun setScreenHeight(heightInPixels: Int) { //in pixels
        val fm = this.getFontMetrics(f)
        var height = fm.maxDescent //haha, getMaxDecent works too, but was quickly depricated!
        height += fm.maxAscent
        maxLinesPerScreen = heightInPixels / height
    }

    /**
     * Performs the smallest necessary amount of scrolling to move the "true cursor," or
     * current line being edited, onto the user's screen. Should be called anytime user
     * attempts to append the document, so they can see what they're doing.
     *
     * @author Joey Patel
     * @author pateljo@northvilleschools.net (valid until 06/18)
     */
    fun focus() {
        //1. Is it already in focus?
        val MinimumFocusablePosition = DOCUMENT_HARDCOPY.size - maxLinesPerScreen
        val MaximumFocusablePosition = DOCUMENT_HARDCOPY.size - 1

        if (MinimumFocusablePosition <= currentPosition && currentPosition <= MaximumFocusablePosition)
            return

        //2. Otherwise, set current position to a focusable location
        var scrollDistance = 0
        if (MinimumFocusablePosition > currentPosition) {
            scrollDistance = MinimumFocusablePosition - currentPosition
        } else if (MaximumFocusablePosition < currentPosition) {
            scrollDistance = MaximumFocusablePosition - currentPosition
        }
        scroll(scrollDistance)
        wasInFocus = true
    }

    /**
     * Class used internally, no need to understand it.
     *
     * @author Joey Patel
     * @author pateljo@northvilleschools.net (valid until 06/18)
     * @param  lineNumber the line number to be checked
     * @returns boolean value of the comparison -> Is it in focus?
     */
    private fun isInFocus(lineNumber: Int): Boolean {
        val toReturn = wasInFocus
        wasInFocus = lineNumber < DOCUMENT_HARDCOPY.size
        return toReturn
    }

    /**
     * Scrolls the document by a specified number of lines, in a direction specified by
     * positive/negative integer value.
     *
     * @author Joey Patel
     * @author pateljo@northvilleschools.net (valid until 06/18)
     * @param  distance the number of lines to scroll
     */
    fun scroll(distance: Int) {
        currentPosition += distance
        if (isInFocus(currentPosition))
            currentCommand = consoleDocument!!.getUserInput()
        if (currentPosition < 0) currentPosition = 0
        while (DOCUMENT_HARDCOPY.contains(""))
            DOCUMENT_HARDCOPY.remove("")
        if (DOCUMENT_HARDCOPY.size < 1 || DOCUMENT_HARDCOPY.get(DOCUMENT_HARDCOPY.size - 1).endsWith("\n")) {
            DOCUMENT_HARDCOPY.add(currentCommand)
        } else {
            DOCUMENT_HARDCOPY.set(DOCUMENT_HARDCOPY.size - 1, currentCommand)
        }

        consoleDocument = ConsoleDocument()
        consoleDocument!!.setConsole(this)
        document = consoleDocument!!
        consoleDocument!!.setCaret(caret)

        consoleDocument!!.setFocusAfterAppend(false)
        var i = 0
        while (i + currentPosition < DOCUMENT_HARDCOPY.size) {
            if (DOCUMENT_HARDCOPY.get(currentPosition + i).endsWith("\n"))
                this.write(DOCUMENT_HARDCOPY.get(currentPosition + i))
            else {
                this.write(prompt)
                consoleDocument!!.writeUser(DOCUMENT_HARDCOPY.get(currentPosition + i), defaultStyle)
            }
            i++

        }
        consoleDocument!!.setFocusAfterAppend(true)
    }


    /**
     * Sets the one-character subprompt of the console's prompting system, then updates the
     * entire prompt.
     *
     * @author Joey Patel
     * @author pateljo@northvilleschools.net (valid until 06/18)
     * @param  prompt to become the new prompt
     */
    fun setPrompt(prompt: String) {
        this.subPrompt = prompt
        this.prompt = this.connection + this.path + this.subPrompt
    }

    constructor() {
        f = null
    } //for debugging only

    constructor(background: Color, text: Color, font: Font, prompt: String) : super() {
        consoleDocument = ConsoleDocument()
        consoleDocument!!.setConsole(this)
        document = consoleDocument!!


        DOCUMENT_HARDCOPY.add("")

        setBackground(background)

        caretColor = text
        addCaretListener(consoleDocument)
        consoleDocument!!.setCaret(caret)

        f = font
        val attrs = inputAttributes
        StyleConstants.setFontFamily(attrs, font.family)
        StyleConstants.setFontSize(attrs, font.size)
        StyleConstants.setItalic(attrs, font.style and Font.ITALIC != 0)
        StyleConstants.setBold(attrs, font.style and Font.BOLD != 0)
        StyleConstants.setForeground(attrs, text)
        styledDocument.setCharacterAttributes(0, consoleDocument!!.length + 1, attrs, false)
        defaultStyle = attrs


        this.prompt = this.connection + this.path + prompt
        consoleDocument!!.write(this.prompt, defaultStyle)

        addKeyListener(this) //catch tabs, enters, and up/down arrows for autocomplete and input processing
        addMouseWheelListener(this)
        addMouseListener(this)
    }

    /**
     * "Clears" the terminal window...
     * ...by replacing it with a new one - this was the easiest way to do it.
     *
     * @author Joey Patel
     * @author pateljo@northvilleschools.net (valid until 06/18)
     */
    fun cls() {
        consoleDocument = ConsoleDocument()
        consoleDocument!!.setConsole(this)
        document = consoleDocument!!
        consoleDocument!!.setCaret(caret)
        DOCUMENT_HARDCOPY = ArrayList<String>()
        DOCUMENT_HARDCOPY.add("")
        currentPosition = 0
    }

    fun write(text: String?) {
        consoleDocument!!.write(text, defaultStyle)
    }

    fun remove(offset: Int, length: Int) {
        try {
            styledDocument.remove(offset, length)
        } catch (e: BadLocationException) {
            // TODO Auto-generated catch block
            e.printStackTrace()
        }

    }

    override fun keyTyped(e: KeyEvent) {
        if (e.keyChar == '\t') {
            //don't append autocomplete tabs to the document
            e.consume()
        }
    }

    override fun keyPressed(e: KeyEvent) {
        // Is the cursor in a valid position?
        if (!consoleDocument!!.isCursorValid())
            consoleDocument!!.makeCursorValid()

        // Is the screen focused on the proper line?
        focus()

        //TAB -> AUTOCOMPLETE
        if (e.keyCode == KeyEvent.VK_TAB) {
            e.consume()
            val input = consoleDocument!!.getUserInput().trim()

            val completions = completionSource.complete(input)
            if (completions == null || completions.isEmpty()) {
                //no completions
                Toolkit.getDefaultToolkit().beep()
            } else if (completions.size == 1)
            //only one match - print it
            {
                var toInsert = completions.get(0)
                toInsert = toInsert.substring(input.length)
                consoleDocument!!.writeUser(toInsert, defaultStyle)
                //don't trigger processing because the user might not agree with the autocomplete
            } else {
                val help = StringBuilder()
                help.append('\n')
                for (str in completions) {
                    help.append(' ')
                    help.append(str)
                }
                help.append("\n" + prompt!!)
                consoleDocument!!.write(help.toString(), defaultStyle)
                consoleDocument!!.writeUser(input, defaultStyle)
            }
        }

        //UP ARROW -> FILL IN A PREV COMMAND
        if (e.keyCode == KeyEvent.VK_UP) {
            e.consume() //Don't actually go up a row

            //Get current input
            var currentInput = consoleDocument!!.getUserInput().trim()

            //If there's no previous commands, beep and return
            if (currentCommandnum <= 0) {
                currentCommandnum = 0 //It should never be less than zero, but you never know...
                Toolkit.getDefaultToolkit().beep()
                return
            }

            //remove the current input from console and, if it's null, initialize it to an empty string.
            if (currentInput != null && currentInput !== "") { //not sure which one it returns, but it doesn't really matter
                this.remove(consoleDocument!!.getLimit(), currentInput!!.length)
            } else {
                currentInput = ""                            //In case it's null... this may be unnecessary
            }

            //If it's something the user just typed, save it for later, just in case.
            if (currentCommandnum >= prompts.size) {
                currentCommandnum = prompts.size
                currentCommand = currentInput      //save the current command, for down arrow use.
            }

            //move on to actually processing the command, now that all extraneous cases are taken care of.

            //based on previous checks, currentCommandnum should be in the range of 1 to prompts.size() before change.
            //after change, it should be in the range of 0 to (prompts.size() - 1), valid for indexing prompts.
            currentCommandnum-- //update command number. (lower num = older command)

            //Index prompts and write the replacement.
            val replacementCommand = prompts.get(currentCommandnum)
            consoleDocument!!.writeUser(replacementCommand, defaultStyle)

            //Similar to tab, don't trigger processing because the user might not agree with the autocomplete
        }

        //DOWN ARROW -> FILL IN A NEWER COMMAND
        if (e.keyCode == KeyEvent.VK_DOWN) {
            e.consume() //pretty sure you can't go down, but if you can... don't.

            //If you've exhausted the list and replaced the line with the current command, beep and return
            if (currentCommandnum >= prompts.size) {
                Toolkit.getDefaultToolkit().beep()
                return
            }


            currentCommandnum++

            //Now, regardless of where you are in the list of commands, you're going to need to replace text.
            val currentInput = consoleDocument!!.getUserInput().trim()
            if (currentInput != null && currentInput !== "")
            //not sure which one it returns, but it doesn't really matter
                this.remove(consoleDocument!!.getLimit(), currentInput!!.length)


            //If you've exhausted the list but not yet replaced the line with the current command...
            if (currentCommandnum == prompts.size) {
                consoleDocument!!.writeUser(currentCommand, defaultStyle)
                return
            }

            //If, for some reason, the list is not in range (lower bound), make it in range.
            if (currentCommandnum < 0) {
                currentCommandnum = 0
            }

            //finally, write in the new command.
            consoleDocument!!.writeUser(prompts.get(currentCommandnum), defaultStyle)
        }
    }

    override fun keyReleased(e: KeyEvent) {
        if (e.keyCode == KeyEvent.VK_ENTER) {
            DOCUMENT_HARDCOPY.set(DOCUMENT_HARDCOPY.size - 1, prompt!! + consoleDocument!!.getUserInput())
            if (!DOCUMENT_HARDCOPY.get(DOCUMENT_HARDCOPY.size - 1).endsWith("\n"))
                DOCUMENT_HARDCOPY.set(
                    DOCUMENT_HARDCOPY.size - 1,
                    DOCUMENT_HARDCOPY.get(DOCUMENT_HARDCOPY.size - 1) + "\n"
                )
            DOCUMENT_HARDCOPY.add("")
            val line = consoleDocument!!.getUserInput().trim()
            val args = parseLine(line)
            prompts.add(line)
            currentCommandnum = prompts.size
            processor.process(args, this)
            consoleDocument!!.write(prompt, defaultStyle)
        }
    }

    override fun mouseWheelMoved(e: MouseWheelEvent) {
        this.scroll(e.wheelRotation * 3)
    }

    override fun componentHidden(e: ComponentEvent) {}
    override fun componentShown(e: ComponentEvent) {}
    override fun componentMoved(e: ComponentEvent) {}
    override fun componentResized(evt: ComponentEvent) {
        this.setScreenHeight((evt.source as JFrame).contentPane.size.getHeight().toInt())
    }

    override fun mouseExited(e: MouseEvent) {}
    override fun mouseEntered(e: MouseEvent) {}
    override fun mousePressed(e: MouseEvent) {}
    override fun mouseClicked(e: MouseEvent) {}

    override fun mouseReleased(e: MouseEvent) {
        if (this.selectedText != null)
        // See if they selected something
            mostRecentSelectedText = this.selectedText
        else
            mostRecentSelectedText = ""
        if (e.isPopupTrigger)
            doPop(e)
    }

    private fun doPop(e: MouseEvent) {
        val menu = PopUp()
        menu.show(e.component, e.x, e.y)
    }

    private inner class PopUp : JPopupMenu() {
        internal var copyButton: JMenuItem? = null

        init {
            copyButton = JMenuItem(object : AbstractAction("copy") {
                override fun actionPerformed(e: ActionEvent) {
                    if (mostRecentSelectedText != "") {
                        val selection = StringSelection(mostRecentSelectedText)
                        val clipboard = Toolkit.getDefaultToolkit().systemClipboard
                        clipboard.setContents(selection, selection)
                    }
                }
            })
            add(copyButton)
        }
    }

    private class NoOpInputProcessor : InputProcessor {
        fun process(text: Array<String>, console: Console) {}
    }

    private class NoOpCompletionSource : CompletionSource {
        fun complete(input: String): List<String>? {
            return null
        }
    }

    companion object {
        private val serialVersionUID = -5260432287332359321L

        private fun parseLine(line: String): Array<String> {
            val args = ArrayList<String>()
            val current = StringBuilder()
            val chars = line.toCharArray()
            var inQuotes = false
            for (c in chars) {
                if (c == '"') {
                    if (current.length > 0) {
                        args.add(current.toString())
                        current.setLength(0)
                    }
                    inQuotes = !inQuotes
                } else if (inQuotes) {
                    current.append(c)
                } else if (c == ' ') {
                    if (current.length > 0) {
                        args.add(current.toString())
                        current.setLength(0)
                    }
                } else {
                    current.append(c)
                }
            }

            args.add(current.toString().trim({ it <= ' ' }))

            return args.toTypedArray()
        }
    }
}
