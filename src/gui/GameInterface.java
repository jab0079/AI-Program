package gui;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

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
public class GameInterface extends JFrame implements ActionListener {
	private final int[] DEFAULT_NUM_PITS = {2, 3, 4, 5, 6, 7, 8, 9, 10}, 
			DEFAULT_NUM_PEBBLES_PLYS = {1, 2, 3, 4, 5};
	private final String[] DEFAULT_PLAYERS = new String[] {"Human",
			"Computer - AND-OR", "Computer - Minimax"};
	private int ply = 1, player1 = 0, player2 = 0;
	private boolean player1sTurn = false;
	
	JSplitPane splitPane;
	JPanel options = new JPanel();
	JPanel gameBoard = new JPanel();
	
	JButton newGameButton = new JButton("New Game");
	JButton runStepButton = new JButton("Run");
	
	JLabel pitsLabel = new JLabel("Pits: ");
	JComboBox<Integer> pitsComboBox = new JComboBox<Integer>();
	
	JLabel pebblesLabel = new JLabel("Pebbles: ");
	JComboBox<Integer> pebblesComboBox = new JComboBox<Integer>();
	
	JLabel plyLabel = new JLabel("Ply: ");
	JComboBox<Integer> plyComboBox = new JComboBox<Integer>();
	
	JLabel player1Label = new JLabel("Player 1: ");
	JComboBox<String> player1ComboBox = new JComboBox<String>(DEFAULT_PLAYERS);
	
	JLabel player2Label = new JLabel("Player 2:");
	JComboBox<String> player2ComboBox = new JComboBox<String>(DEFAULT_PLAYERS);
	
	PitButton pits[];
	
	public GameInterface() {
		super("AI Pebble Game");
		setSize(1000, 500);
		setResizable(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		// Set up menu/options panel
		setupMenuOptions();
		
		// Add options & game board panels to split pane
		splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, false, 
				options, gameBoard);
		splitPane.setEnabled(false);
        getContentPane().add(splitPane);
		setVisible(true);
	}

	/*
	 * Sets up the options pane with the parameters and buttons that the user can
	 * use to create and control the pebble game.
	 */
	private void setupMenuOptions() {
		options.removeAll();
		
		// setup comboboxes
		for(Integer i: DEFAULT_NUM_PITS)
			pitsComboBox.addItem(i);
		
		for(Integer i: DEFAULT_NUM_PEBBLES_PLYS) {
			pebblesComboBox.addItem(i);
			plyComboBox.addItem(i);
		}
		
		// formating and layout
		options.setLayout(new FlowLayout());
		player1Label.setForeground(new Color(51, 153, 255));
		player2Label.setForeground(new Color(255, 153, 51));
		
		// add components to panel
		options.add(newGameButton);
		newGameButton.addActionListener(this);
		options.add(runStepButton);
		runStepButton.addActionListener(this);
		options.add(pitsLabel);
		options.add(pitsComboBox);
		options.add(pebblesLabel);
		options.add(pebblesComboBox);
		options.add(plyLabel);
		options.add(plyComboBox);
		options.add(player1Label);
		options.add(player1ComboBox);
		options.add(player2Label);
		options.add(player2ComboBox);
		
		options.updateUI();
	}

	/*
	 * Sets up the game board based on a specified number of pits and pebbles.
	 */
	private void setupGameBoard(int numSquares, int numPebbles) {
		gameBoard.removeAll();
		gameBoard.setLayout(new GridLayout(2, numSquares / 2));
		
		pits = new PitButton[numSquares];
		for (int i = 0; i < numSquares; i++) {
			pits[i] = new PitButton(numPebbles, numSquares);
			pits[i].addActionListener(this);

			if (i < numSquares / 2) { // make Player2's squares orange & disabled
				pits[i].setBackground(new Color(255, 153, 51));
			} else { // make Player1's squares blue
				pits[i].setBackground(new Color(51, 153, 255));
			}
			
			gameBoard.add(pits[i]);
			//swapTurn();
		}
		
		gameBoard.updateUI();
		splitPane.updateUI();
	}

	/*
	 * Gets the button pressed and performs the appropriate action.
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		JButton buttonPressed = (JButton) e.getSource();
		
		if (buttonPressed == newGameButton) { 		 // start new game 
			newGame();
		} else if (buttonPressed == runStepButton) { // run/step
			runStep();
		} else {
			for (int i = 0; i < pits.length; i++) {  // distribute pebbles
				if (buttonPressed == pits[i]) {
					distributePebbles(i);
					swapTurn();
				}
			}
		}	
	}

	/*
	 * Handles the new game button. Takes the game settings from the options pane and
	 * creates a new game board accordingly.
	 */
	private void newGame() {
		// get preferences from user
		int numSquares = DEFAULT_NUM_PITS[pitsComboBox.getSelectedIndex()],
			numPebbles = DEFAULT_NUM_PEBBLES_PLYS[pebblesComboBox.getSelectedIndex()];
		ply = DEFAULT_NUM_PEBBLES_PLYS[plyComboBox.getSelectedIndex()];
		player1 = plyComboBox.getSelectedIndex();
		player2 = plyComboBox.getSelectedIndex();
		
		// setup game board and begin the new game
		setupGameBoard(numSquares * 2, numPebbles);
		player1sTurn = false;
		swapTurn();
	}
	
	/*
	 * Handles the run/step button, which depending on the setting either runs the
	 * game between two computers to completion or steps through each AI move.
	 */
	private void runStep() {
		// TODO Auto-generated method stub
		
	}
	
	/*
	 * Swaps the turn between player1 & player2. Checks if the game is over and enables
	 * the pits on the game board accordingly.
	 */
	private void swapTurn() {
		if (player1sTurn) { // switch turn to player2
			player1sTurn = false;
			if (gameOver()) { // if game is over, disable all pits
				for (int i = 0; i < pits.length; i++) {
					pits[i].setEnabled(false);
				}
			} else { // otherwise switch enabled pits
				for (int i = 0; i < pits.length / 2; i++) {
					pits[i + (pits.length / 2)].setEnabled(false);  // disable player1's pits
					pits[i].setEnabled(true);  // enable player2's pits
				}
			}
		} else { // switch turn to player1
			player1sTurn = true;
			if (gameOver()) { // if game is over, disable all pits
				for (int i = 0; i < pits.length; i++) {
					pits[i].setEnabled(false); 
				}
			} else { // otherwise switch enabled pits
				for (int i = 0; i < pits.length / 2; i++) {
					pits[i + (pits.length / 2)].setEnabled(true);  // enable player1's pits
					pits[i].setEnabled(false);  // disable player2's pits
				}
			}		
		}
	}

	/*
	 * Distributes the pebbles at a given pit in a counter-clockwise fashion around
	 * the game board. 
	 */
	private void distributePebbles(int i) {
		// take all the pebbles from the pit at index i
		int numPebbles = pits[i].getPebbles(), index = i;
		pits[index].setPebbles(0);
		pits[index].setText(Integer.toString(pits[index].getPebbles()));
		
		// distribute the pebbles one-by-one to each pit in a counter-clockwise order
		while (numPebbles > 0) {
			index = nextPitIndex(index);
			pits[index].setPebbles(pits[index].getPebbles() + 1);
			pits[index].setText(Integer.toString(pits[index].getPebbles()));
			numPebbles--;
		}
	}

	/*
	 * Checks if the current game state is a victory for either player.
	 * If either player has no pebbles on their turn, they have no possible moves
	 * and the game is over. Displays game over message dialog.
	 */
	private boolean gameOver() {
		int pebbles1 = 0, pebbles2 = 0;
		
		// get total pebbles for each player
		for (int i = 0; i < pits.length / 2; i++) {
			pebbles2 += pits[i].getPebbles();
			pebbles1 += pits[i + (pits.length / 2)].getPebbles();
		}
		
		// if either side is empty this show win state
		if (pebbles1 == 0 && player1sTurn) {
			JOptionPane.showMessageDialog(this,
				    "Player 2 Wins!!!",
				    "GAME OVER",
				    JOptionPane.PLAIN_MESSAGE);
			return true;
			
		} else if (pebbles2 == 0 && !player1sTurn) {
			JOptionPane.showMessageDialog(this,
				    "Player 1 Wins!!!",
				    "GAME OVER",
				    JOptionPane.PLAIN_MESSAGE);
			for (int i = 0; i < pits.length; i++) {
				pits[i].setEnabled(false);  // disable all pits
			}
			return true;
		} else {
			return false;
		}
	}

	/*
	 * Gets the corrected index for the next pit in the counter-clockwise order. 
	 */
	private int nextPitIndex(int i) {
		int numSquaresPerSide = pits.length / 2;
		if (i == 0) {
			return numSquaresPerSide;
		} else if (i == pits.length - 1) {
			return pits.length - numSquaresPerSide - 1;
		} else if (i < numSquaresPerSide) {
			return i - 1;
		} else {
			return i + 1;
		}
	}
	
}
