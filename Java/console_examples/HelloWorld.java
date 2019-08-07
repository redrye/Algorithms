
import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.text.*;
import javax.swing.event.*;

import java.io.*;

public class HelloWorld
{
    public static void main(String[] args) {
	
	JTextArea textArea = new JTextArea();
	Document doc = textArea.getDocument();
	PrintStream out = new PrintStream(new DocumentOutputStream(doc));
	
	for(int i = 0; i < 64; i++) {
	    out.println("Hello World");
	}
	
	WindowListener l = new WindowAdapter() {
	    public void windowClosing(WindowEvent e) {
		System.exit(0);
	    }
	};
	JFrame f = new JFrame("Simple DocumentOutputStream Example");
	f.addWindowListener(l); 
	f.getContentPane().add(new JScrollPane(textArea), BorderLayout.CENTER);
	f.setSize(640, 480);
	f.setVisible(true);
    }
}
