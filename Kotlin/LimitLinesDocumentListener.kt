package kotlinConsole

import javax.swing.SwingUtilities
import javax.swing.event.DocumentEvent
import javax.swing.event.DocumentListener
import javax.swing.text.BadLocationException
import javax.swing.text.Document
import javax.swing.text.Element

/*
 *  Port http://www.camick.com/java/source/LimitLinesDocumentListener.java to the Kotlin version
 *
 *  A class to control the maximum number of lines to be stored in a Document
 *
 *  Excess lines can be removed from the start or end of the Document
 *  depending on your requirement.
 *
 *  a) if you append text to the Document, then you would want to remove lines
 *     from the start.
 *  b) if you insert text at the beginning of the Document, then you would
 *     want to remove lines from the end.
 */
class LimitLinesDocumentListener
	/*
	 *  Specify the number of lines to be stored in the Document.
	 *  Extra lines will be removed from the start or end of the Document,
	 *  depending on the boolean value specified.
	 */
@JvmOverloads constructor(maximumLines: Int, private val isRemoveFromStart: Boolean = true) : DocumentListener {
	/*
	 *  Set the maximum number of lines to be stored in the Document
	 */
	var limitLines: Int = 0
		set(maximumLines) {
			if (maximumLines < 1) {
				val message = "Maximum lines must be greater than 0"
				throw IllegalArgumentException(message)
			}

			field = maximumLines
		}

	init {
		limitLines = maximumLines
	}

	//  Handle insertion of new text into the Document

	override fun insertUpdate(e: DocumentEvent) {
		//  Changes to the Document can not be done within the listener
		//  so we need to add the processing to the end of the EDT

		SwingUtilities.invokeLater { removeLines(e) }
	}

	override fun removeUpdate(e: DocumentEvent) {}
	override fun changedUpdate(e: DocumentEvent) {}

	/*
	 *  Remove lines from the Document when necessary
	 */
	private fun removeLines(e: DocumentEvent) {
		//  The root Element of the Document will tell us the total number
		//  of line in the Document.

		val document = e.document
		val root = document.defaultRootElement

		while (root.elementCount > limitLines) {
			if (isRemoveFromStart) {
				removeFromStart(document, root)
			} else {
				removeFromEnd(document, root)
			}
		}
	}

	/*
	 *  Remove lines from the start of the Document
	 */
	private fun removeFromStart(document: Document, root: Element) {
		val line = root.getElement(0)
		val end = line.endOffset

		try {
			document.remove(0, end)
		} catch (ble: BadLocationException) {
			println(ble)
		}

	}

	/*
	 *  Remove lines from the end of the Document
	 */
	private fun removeFromEnd(document: Document, root: Element) {
		//  We use start minus 1 to make sure we remove the newline
		//  character of the previous line

		val line = root.getElement(root.elementCount - 1)
		val start = line.startOffset
		val end = line.endOffset

		try {
			document.remove(start - 1, end - start)
		} catch (ble: BadLocationException) {
			println(ble)
		}

	}
}