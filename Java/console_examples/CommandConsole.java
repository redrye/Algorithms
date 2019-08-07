
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.*;
import javax.swing.*;
import javax.swing.text.*;

/**
 * Example usage of the ConsolePane example.
 *
 * @author  Timothy Prinzing
 * @version 1.1 02/05/99
 */
public class CommandConsole extends JFrame implements ActionListener, Runnable {

    public static void main(String[] args) {
	CommandConsole ex = new CommandConsole();
	ex.setVisible(true);
    }

    public CommandConsole() {
	super("ConsolePane Example");
	pp = new ConsolePane();
	MutableAttributeSet a = new SimpleAttributeSet();
	StyleConstants.setForeground(a, new Color(58,106,122));
	pp.setOutputAttributes(a);
	a.removeAttributes(a);
	StyleConstants.setForeground(a, new Color(127,69, 145));
	StyleConstants.setBold(a, true);
	pp.setErrorAttributes(a);

	field = new JTextField();
	field.addActionListener(this);
	Box hbox1 = new Box(BoxLayout.X_AXIS) {
	    public Dimension getMaximumSize() {
		Dimension d = getPreferredSize();
		d.width = Short.MAX_VALUE;
		return d;
	    }
	};
	hbox1.add(new JLabel("Command: "));
	hbox1.add(Box.createHorizontalStrut(3));
	hbox1.add(field);
	hbox1.add(Box.createHorizontalStrut(3));
	kill = new JButton("Kill");
	kill.addActionListener(this);
	kill.setEnabled(false);
	kill.setForeground(Color.red);
	hbox1.add(kill);
	Box vbox = Box.createVerticalBox();
	vbox.add(Box.createVerticalStrut(10));
	vbox.add(hbox1);
	vbox.add(Box.createVerticalStrut(10));
	vbox.add(pp);
	vbox.add(Box.createVerticalStrut(10));
	Box hbox2 = Box.createHorizontalBox();
	hbox2.add(Box.createHorizontalStrut(10));
	hbox2.add(vbox);
	hbox2.add(Box.createHorizontalStrut(10));

	getContentPane().setLayout(new BorderLayout());
	getContentPane().add("Center", hbox2);
	pack();
	setSize(600, 600);
    }

    public void actionPerformed(ActionEvent e) {
	Object source = e.getSource();
	if (source == field) {
	    String command = field.getText();
	    try {
		p = Runtime.getRuntime().exec(command);
		Thread proc = new Thread(this);
		proc.setPriority(Thread.MIN_PRIORITY);
		proc.start();
	    } catch (Throwable t) {
		getToolkit().beep();
		pp.error(t.toString());
	    }
	} else if (source == kill) {
	    synchronized(this) {
		if (p != null) {
		    p.destroy();
		    pp.error("Process Killed");
		}
	    }
	}
    }

    public void run() {
	kill.setEnabled(true);
	pp.showProcessOutput(p);
	synchronized(this) {
	    p = null;
	    kill.setEnabled(false);
	}
    }
	
    ConsolePane pp;
    JTextField field;
    JButton kill;
    Process p;
}
