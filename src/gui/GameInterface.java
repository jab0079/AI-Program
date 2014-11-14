package gui;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * GameInterface.java
 * 
 * The simple GUI for our AI pebble game. Generates a board based on a number of
 * squares and pebbles selected by the user, as well as the computer player(s).
 * 
 * @author Jared Brown
 *
 */

@SuppressWarnings("serial")
public class GameInterface extends JFrame {
	JPanel options = new JPanel();
	JButton startButton = new JButton("Start");
	JPanel gameBoard = new JPanel();
	PitButton pits[];
	
	public GameInterface(int numSquares, int numPebbles) {
		super("GameInterface");
		setSize(100 * numSquares, 400);
		setResizable(false);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		// Set up menu options
		options.add(startButton);
		
		// Set up game board with squares and pebbles
		gameBoard.setLayout(new GridLayout(2, numSquares / 2));
		pits = new PitButton[numSquares];
		for (int i = 0; i < numSquares; i++) {
			pits[i] = new PitButton(numPebbles);
			gameBoard.add(pits[i]);
		}
		
		add(options);
		add(gameBoard);
		setVisible(true);
	}
}
