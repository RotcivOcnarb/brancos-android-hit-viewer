package pack;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

import javax.swing.JOptionPane;

public class DialogOut extends PrintStream{

	MyPanel mp;

	public DialogOut (MyPanel mp) throws FileNotFoundException {
    	super("log.txt");
    	this.mp = mp;
    }

	@Override
	public void print(String s) {
		mp.console_area.append("\n" + s);
	}
}
