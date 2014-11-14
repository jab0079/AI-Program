package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

/**
 * PitButton.java
 * 
 * The pit squares where pebbles are placed on the game board. They are buttons so the 
 * user can click the square from which they want to move pebbles on their turn.
 * 
 * @author Jared Brown
 *
 */
@SuppressWarnings("serial")
public class PitButton extends JButton implements ActionListener  {
	int pebbles = 0;

	public PitButton(int numPebbles) {
		pebbles = numPebbles;
		this.setText(Integer.toString(pebbles));
		this.addActionListener(this);
	}
	
	public void actionPerformed(ActionEvent e) {
		//distribute pebbles
	}
}
