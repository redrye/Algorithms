package kotlinConsole
import java.awt.Color
import java.io.ByteArrayOutputStream
import java.io.PrintStream
import javax.swing.event.DocumentListener
import javax.swing.text.*

/*  Port http://www.camick.com/java/source/MessageConsole.java to the Kotlin version
 *
 *  Create a simple console to display text messages.
 *
 *  Messages can be directed here from different sources. Each source can
 *  have its messages displayed in a different color.
 *
 *  Messages can either be appended to the console or inserted as the first
 *  line of the console
 *
 *  You can limit the number of lines to hold in the Document.
 */
class MessageConsole
	/*
	 *	Use the text component specified as a simply console to display
	 *  text messages.
	 *
	 *  The messages can either be appended to the end of the console or
	 *  inserted as the first line of the console.
	 */
@JvmOverloads constructor(private val textComponent: JTextComponent, private val isAppend: Boolean = true) {
	private val document: Document = textComponent.document
	private var limitLinesListener: DocumentListener? = null

	init {
		textComponent.isEditable = false
	}

	/*
	 *  Redirect the output from the standard output to the console
	 *  using the specified color and PrintStream. When a PrintStream
	 *  is specified the message will be added to the Document before
	 *  it is also written to the PrintStream.
	 */
	@JvmOverloads
	fun redirectOut(textColor: Color? = null, printStream: PrintStream? = null) {
		val cos = ConsoleOutputStream(textColor, printStream)
		System.setOut(PrintStream(cos, true))
	}

	/*
	 *  Redirect the output from the standard error to the console
	 *  using the specified color and PrintStream. When a PrintStream
	 *  is specified the message will be added to the Document before
	 *  it is also written to the PrintStream.
	 */
	@JvmOverloads
	fun redirectErr(textColor: Color? = null, printStream: PrintStream? = null) {
		val cos = ConsoleOutputStream(textColor, printStream)
		System.setErr(PrintStream(cos, true))
	}

	/*
	 *  To prevent memory from being used up you can control the number of
	 *  lines to display in the console
	 *
	 *  This number can be dynamically changed, but the console will only
	 *  be updated the next time the Document is updated.
	 */
	fun setMessageLines(lines: Int) {
		if (limitLinesListener != null)
			document.removeDocumentListener(limitLinesListener)

		limitLinesListener = LimitLinesDocumentListener(lines, isAppend)
		document.addDocumentListener(limitLinesListener)
	}

	/*
	 *	Class to intercept output from a PrintStream and add it to a Document.
	 *  The output can optionally be redirected to a different PrintStream.
	 *  The text displayed in the Document can be color coded to indicate
	 *  the output source.
	 */
	internal inner class ConsoleOutputStream
		/*
		 *  Specify the option text color and PrintStream
		 */
	(textColor: Color?, private val printStream: PrintStream?) : ByteArrayOutputStream() {
		private val EOL = System.getProperty("line.separator")
		private var attributes: SimpleAttributeSet? = null
		private val buffer = StringBuffer(80)
		private var isFirstLine: Boolean = false

		init {
			if (textColor != null) {
				attributes = SimpleAttributeSet()
				StyleConstants.setForeground(attributes!!, textColor)
			}

			if (isAppend)
				isFirstLine = true
		}

		/*
		 *  Override this method to intercept the output text. Each line of text
		 *  output will actually involve invoking this method twice:
		 *
		 *  a) for the actual text message
		 *  b) for the newLine string
		 *
		 *  The message will be treated differently depending on whether the line
		 *  will be appended or inserted into the Document
		 */
		override fun flush() {
			val message = toString()

			if (message.length == 0) return

			if (isAppend)
				handleAppend(message)
			else
				handleInsert(message)

			reset()
		}

		/*
		 *	We don't want to have blank lines in the Document. The first line
		 *  added will simply be the message. For additional lines it will be:
		 *
		 *  newLine + message
		 */
		private fun handleAppend(message: String) {
			//  This check is needed in case the text in the Document has been
			//	cleared. The buffer may contain the EOL string from the previous
			//  message.

			if (document.length == 0)
				buffer.setLength(0)

			if (EOL == message) {
				buffer.append(message)
			} else {
				buffer.append(message)
				clearBuffer()
			}

		}

		/*
		 *  We don't want to merge the new message with the existing message
		 *  so the line will be inserted as:
		 *
		 *  message + newLine
		 */
		private fun handleInsert(message: String) {
			buffer.append(message)

			if (EOL == message) {
				clearBuffer()
			}
		}

		/*
		 *  The message and the newLine have been added to the buffer in the
		 *  appropriate order so we can now update the Document and send the
		 *  text to the optional PrintStream.
		 */
		private fun clearBuffer() {
			//  In case both the standard out and standard err are being redirected
			//  we need to insert a newline character for the first line only

			if (isFirstLine && document.length != 0) {
				buffer.insert(0, "\n")
			}

			isFirstLine = false
			val line = buffer.toString()

			try {
				if (isAppend) {
					val offset = document.length
					document.insertString(offset, line, attributes)
					textComponent.caretPosition = document.length
				} else {
					document.insertString(0, line, attributes)
					textComponent.caretPosition = 0
				}
			} catch (ble: BadLocationException) {
			}

			printStream?.print(line)

			buffer.setLength(0)
		}
	}
}