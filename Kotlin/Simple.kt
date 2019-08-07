

import java.awt.EventQueue
import javax.swing.*

class KFrame(title: String) : JFrame() {
	init {
		createUI(title)
	}
	private fun createUI(title: String) {
		setTitle(title)
		val closeBtn = JButton("Close")
		closeBtn.addActionListener {
			System.exit(0)
		}
		createLayout(closeBtn)
		defaultCloseOperation = JFrame.EXIT_ON_CLOSE
		setSize(300, 200)
		setLocationRelativeTo(null)

	}
	private fun createLayout(vararg arg: JComponent) {
		val gl = GroupLayout(contentPane)
		contentPane.layout = gl
		gl.autoCreateContainerGaps = true
		gl.setHorizontalGroup(gl.createSequentialGroup().addComponent(arg[0]))
		gl.setVerticalGroup(gl.createSequentialGroup().addComponent(arg[0]))
	}
	private fun createMenuBar() {
		val menubar = JMenuBar()
		val runBtn = JMenuItem("src/main/resources/exit.png")
		menubar.add(runBtn)
		jMenuBar = menubar
	}
}

private fun createAndShowGUI() {
	val frame = KFrame("Close Button")
	frame.isVisible = true
}

fun main(args: Array<String>) {
	EventQueue.invokeLater(::createAndShowGUI)
}