
import java.io.*;
import java.awt.Color;
import javax.swing.*;
import javax.swing.text.*;

/**
 * A display area for console output.  This component
 * will display the output of a PrintStream, PrintWriter,
 * or of a running process in a swing text component.  
 * The text from the output and error pipes to the child 
 * process can be displayed with whatever character 
 * attributes desired.
 * 
 * @author  Timothy Prinzing
 * @version 1.2 03/04/99
 */
public class ConsolePane extends JScrollPane {

    /**
     * Create a console display.  By default
     * the text region is set to not be editable.
     */
    public ConsolePane() {
	super();
	outputArea = createOutputArea();
	outputArea.setEditable(false);
	JViewport vp = getViewport();
	vp.add(outputArea);
	vp.setBackingStoreEnabled(true);
    }

    /**
     * Create the component to be used to display the
     * process output.  This is a hook to allow the
     * component used to be customized.  
     */
    protected JTextComponent createOutputArea() {
	JTextPane pane = new JTextPane();
	return pane;
    }

    /**
     * Create a PrintStream that will display in the console
     * using the given attributes.
     */
    public PrintStream createPrintStream(AttributeSet a) {
	Document doc = outputArea.getDocument();
	OutputStream out = new DocumentOutputStream(doc, a);
	PrintStream pOut = new PrintStream(out);
	return pOut;
    }

    /**
     * Create a PrintWriter that will display in the console
     * using the given attributes.
     */
    public PrintWriter createPrintWriter(AttributeSet a) {
	Document doc = outputArea.getDocument();
	Writer out = new DocumentWriter(doc, a);
	PrintWriter pOut = new PrintWriter(out);
	return pOut;
    }

    /**
     * Fetch the component used for the output.  This
     * allows further parsing of the output if desired,
     * and allows things like mouse listeners to be 
     * attached.  This can be useful for things like 
     * compiler output where clicking on an error 
     * warps another view to the location of the error.
     */
    public JTextComponent getOutputArea() {
	return outputArea;
    }

    /**
     * Set the attributes to use when displaying the output
     * pipe to the process being monitored.
     */
    public void setOutputAttributes(AttributeSet a) {
	outputAttr = a.copyAttributes();
    }

    /**
     * Get the attributes being used when displaying the output
     * pipe to the process being monitored.
     */
    public AttributeSet getOutputAttributes() {
	return outputAttr;
    }

    /**
     * Set the attributes to use when displaying the error
     * pipe to the process being monitored.
     */
    public void setErrorAttributes(AttributeSet a) {
	errorAttr = a.copyAttributes();
    }

    /**
     * Get the attributes being used when displaying the error
     * pipe to the process being monitored.
     */
    public AttributeSet getErrorAttributes() {
	return errorAttr;
    }

    /**
     * Monitor a running process, displaying any output from the
     * process.  This method will not return until the process has
     * finished and the streams associated with the process have
     * emptied (or been interrupted).
     * To get a process, the Runtime class can be used.
     * 
     * @param p	the process to monitor
     * @return	the exit value of the process.  If an exception
     *  is thrown, a -1 will be returned.
     */
    public int showProcessOutput(Process p) {
	int exitCode = -1;
	try {
	    Document doc = outputArea.getDocument();
	    doc.remove(0, doc.getLength());
	    InputStream outIn = p.getInputStream(); // output stream from process
	    PrintStream pOut = createPrintStream(outputAttr);
	    StreamCopier outConnector = new StreamCopier(outIn, pOut);
	    outConnector.start();
	    InputStream errIn = p.getErrorStream();
	    PrintStream pErr = createPrintStream(errorAttr);
	    StreamCopier errConnector = new StreamCopier(errIn, pErr);
	    errConnector.start();

	    // wait for the stream copiers to complete (which may be interrupted by the
	    // timeout thread
	    outConnector.waitUntilDone();
	    errConnector.waitUntilDone();
	    exitCode = p.waitFor();

	    outIn.close();
	    errIn.close();
	} catch (InterruptedException e) {
	    error("InterrupedException: " + e.getMessage());
	} catch (IOException e) {
	    error("IOException: " + e.getMessage());
	} catch (BadLocationException e) {
	    error("BadLocationException: " + e.getMessage());
	}
	return exitCode;
    }

    /**
     * Display an error into the output area.  By default
     * this displays the given string with a foreground color
     * of Color.red.  This is used if an exception is thrown
     * when monitoring a running process.
     */
    public void error(String s) {
	Document doc = outputArea.getDocument();
	MutableAttributeSet a = new SimpleAttributeSet();
	StyleConstants.setForeground(a, Color.red);
	try {
	    doc.insertString(doc.getLength(), s, a);
	} catch (Throwable e) {
	    getToolkit().beep();
	}
    }

    private JTextComponent outputArea;
    private AttributeSet outputAttr;
    private AttributeSet errorAttr;

    /**
     * A thread to copy an input stream to an output stream.
     * This is done line by line because process output tends
     * to be line oriented and it potentially gives better 
     * visual feedback.
     */
    static class StreamCopier extends Thread {

	/**
	 * Create one.
	 * @param from 	the stream to copy from
	 * @param out	the log to copy to
	 */
        StreamCopier(InputStream from, PrintStream to) {
	    setName(Thread.currentThread().getName() + "_StreamCopier_" + (serial++));
	    in = new DataInputStream(from);
	    out = to;
	}

	/**
	 * Start copying the input stream (i.e. run
	 * the thread).
	 */
        public void run() {
	    try {
		String line;
		while ((line = in.readLine()) != null) {
		    out.println(line);
		}
	    }
	    catch (IOException e) {
	    }
	    //System.out.println("Stream copied");
	    synchronized (this) {
		done = true;
		notifyAll();
	    }
	}

        public synchronized boolean isDone() {
	    return done;
	}

	/**
	 * Blocks until the copy is complete, or until the thread is interrupted
	 */
        public synchronized void waitUntilDone() throws InterruptedException {
	    // poll interrupted flag, while waiting for copy to complete
	    while (!Thread.interrupted() && !done) 
		wait(1000);
	    
	    // workaround; if the exception hasn't been thrown already, do it now
	    if (Thread.interrupted()) {
		//System.out.println("Stream copier: throwing InterruptedException");
		throw new InterruptedException();
	    }
	}

        private DataInputStream in;
        private PrintStream out;
        private boolean done;

        private static int serial;
    }

}
