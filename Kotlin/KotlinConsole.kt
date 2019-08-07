package me.sunlan.kotlin.console

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea
import org.fife.ui.rsyntaxtextarea.SyntaxConstants
import org.fife.ui.rtextarea.RTextScrollPane
import java.awt.BorderLayout
import java.awt.Color
import java.awt.Component
import javax.script.ScriptEngineManager
import javax.swing.*


class KotlinConsole: JFrame {
	private lateinit var syntaxTextArea: RSyntaxTextArea
	private lateinit var resultPane: JTextPane

	constructor() : super("Kotlin Console") {
		val cp = JPanel(BorderLayout())
		val splitPane = createSplitPane(createRTextScrollPane(), JScrollPane(createResultPane()))

		cp.add(createToolBar(), BorderLayout.NORTH)
		cp.add(splitPane)

		contentPane = cp
		defaultCloseOperation = JFrame.EXIT_ON_CLOSE
		setSize(800, 500)
		setLocationRelativeTo(null)
	}

	fun createToolBar(): JToolBar {
		val toolBar = JToolBar()

		toolBar.isFloatable = false

		val runButton = JButton("Run")

		runButton.addActionListener({
			val engine = ScriptEngineManager().getEngineByExtension("kts")!!
			val result = engine.eval(syntaxTextArea.text)

			println(result?.toString())
		})

		toolBar.add(runButton)

		return toolBar
	}

	fun createSplitPane(c1: Component, c2: Component): JSplitPane {
		val splitPane = JSplitPane(JSplitPane.VERTICAL_SPLIT, c1, c2)

		return splitPane
	}

	fun createRTextScrollPane(): RTextScrollPane {
		syntaxTextArea = RSyntaxTextArea(15, 60)
		syntaxTextArea.syntaxEditingStyle = SyntaxConstants.SYNTAX_STYLE_JAVA
		syntaxTextArea.isCodeFoldingEnabled = true

		return RTextScrollPane(syntaxTextArea)
	}

	fun createResultPane(): JTextPane {
		resultPane = JTextPane()

		val mc = MessageConsole(resultPane)
		mc.redirectOut()
		mc.redirectErr(Color.RED, null)
		mc.setMessageLines(100)

		return resultPane
	}
}

fun main(args: Array<String>) {
	try {
		for (info in UIManager.getInstalledLookAndFeels()) {
			if ("Nimbus" == info.name) {
				UIManager.setLookAndFeel(info.className)
				break
			}
		}
	} catch (e: Exception) {
		// ignore
	}

	SwingUtilities.invokeLater { KotlinConsole().isVisible = true }
}
