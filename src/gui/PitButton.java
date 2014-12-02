package gui;

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
public class PitButton extends JButton  {
	private int pebbles = 0;

	public PitButton(int numPebbles, int numSquares) {
		pebbles = numPebbles;
		this.setText(Integer.toString(pebbles));
		this.setSize(numSquares / 2, numSquares / 2);
	}
	
	public int getPebbles() {
		return pebbles;
	}
	
	public void setPebbles(int numPebbles) {
		pebbles = numPebbles;
	}
}
