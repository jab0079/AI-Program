import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

import javax.swing.JButton;

import ai.AndOrPlayer;
import ai.MinimaxPlayer;
import ai.Player;
import gui.*;


/**
 * AIGame.java
 * 
 * The AI pebble game instance which interacts with the game board gui and handles 
 * human and computer players.
 * 
 * @author Jared Brown & Matt Mathis
 *
 */
public class AIGame implements ActionListener {
	GameInterface<AIGame> gui;
	Player player1, player2;
	boolean player1AI = false, player2AI = false,
			gameOver = true, player1sTurn = false,
			run = false;
	int ply = 1;
	int[] pebbles;
	
	public AIGame() {
		gui = new GameInterface<AIGame>(this);
		
	}
	
	public static void main(String[] args) {
		new AIGame();
	}

	/*
	 * Gets the button pressed and performs the appropriate action.
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		JButton buttonPressed = (JButton) e.getSource();
		
		if (buttonPressed == gui.newGameButton) { // start new game 
			newGame();
			
			if (run && player1AI && player2AI) { // if both players are AI
				while (!gameOver) // run through entire game
					runStep();
			} else if (run && player1AI) {
				runStep();
			}
		} else if (buttonPressed == gui.runStepButton) // step through AI move
			runStep();
		else { // check which pit button was pressed & if it has pebbles to move
			for (int i = 0; i < gui.pits.length; i++) {  // distribute pebbles
				if (buttonPressed == gui.pits[i] && gui.pits[i].getPebbles() != 0) {
					pebbles = gui.distributePebbles(i);
					swapTurn();
					
					if (run && !gameOver)
						runStep();
				}
			}
		}	
	}
	
	public void setup() {
		// setup menu options panel in gui
		gui.setupMenuOptions(this);
	}

	/*
	 * Handles the new game button. Takes the game settings from the options pane and
	 * creates a new game board accordingly.
	 */
	private void newGame() {
		// get preferences from user
		int numSquares = (int) gui.pitsComboBox.getSelectedItem(),
			numPebbles = (int) gui.pebblesComboBox.getSelectedItem();
		ply = (int) gui.plyComboBox.getSelectedItem();
		run = gui.runStepCheckBox.isSelected();
		
		// check players for AI computer players & init them
		int p1 = gui.player1ComboBox.getSelectedIndex(),
			p2 = gui.player2ComboBox.getSelectedIndex();
		
		player1AI = false;
		player2AI = false;
		
		switch(p1) {
			case 1:
				player1 = new AndOrPlayer(true, ply);
				player1AI = true;
				break;
			case 2:
				player1 = new MinimaxPlayer(true, ply);
				player1AI = true;
				break;
			default:
				break;
		}
		switch(p2) {
			case 1:
				player2 = new AndOrPlayer(false, ply);
				player2AI = true;
				break;
			case 2:
				player2 = new MinimaxPlayer(false, ply);
				player2AI = true;
				break;
			default:
				break;
		}
		
		// setup pebbles game state array
		pebbles = new int[numSquares * 2];
		Arrays.fill(pebbles, numPebbles);
		
		gui.options.repaint();
		gui.setupGameBoard(this, numSquares * 2, numPebbles);
		gameOver = false;
		player1sTurn = false;
		swapTurn();
	}
	
	/*
	 * Handles the run/step button, which depending on the setting either runs the
	 * game between two computers to completion or steps through each AI move.
	 */
	private void runStep() {
		if (player1sTurn && player1AI) {
			int move = player1.makeMove(pebbles);
			pebbles = gui.distributePebbles(move);
			swapTurn();
		} else if (!player1sTurn && player2AI) {
			int move = player2.makeMove(pebbles);
			pebbles = gui.distributePebbles(move);
			swapTurn();
		}
	}
	
	/*
	 * Swaps the turn between player1 & player2. Checks if the game is over and enables
	 * the pits on the game board accordingly.
	 */
	private void swapTurn() {
		player1sTurn = !player1sTurn; // switch turns
		
		for (int i = 0; i < gui.pits.length; i++) { // disable all pits
			gui.pits[i].setEnabled(false);
		}
		
		checkIfGameOver();
		if (!gameOver) { // if game is not over
			if (player1sTurn) { // if player1's turn
				if (!player1AI) { // if player1 is human
					gui.runStepButton.setEnabled(false); // disable step button
					for (int i = 0; i < gui.pits.length / 2; i++) {
						gui.pits[i + (gui.pits.length / 2)].setEnabled(true); // enable player1's pits
					}
				} else if (!run && player1AI) { // if player1 is an AI & run is disabled
					gui.runStepButton.setEnabled(true); // enable step button
				}
			} 
			else if(!player1sTurn) { // if player2's turn
				if (!player2AI) { // if player2 is human
					gui.runStepButton.setEnabled(false); // disable step button
					for (int i = 0; i < gui.pits.length / 2; i++) {
						gui.pits[i].setEnabled(true); // enable player2's pits
					}
				} else if (!run && player2AI) { // if player2 is an AI & run is disabled
					gui.runStepButton.setEnabled(true); // enable step button
				}
			} 
		}
	}
	
	/*
	 * Checks if the current game state is a victory for either player.
	 * If either player has no pebbles on their turn, they have no possible moves
	 * and the game is over. Displays game over message dialog.
	 */
	private void checkIfGameOver() {
		int pebbles1 = 0, pebbles2 = 0;
		
		// get total pebbles for each player
		for (int i = 0; i < gui.pits.length / 2; i++) {
			pebbles2 += gui.pits[i].getPebbles();
			pebbles1 += gui.pits[i + (gui.pits.length / 2)].getPebbles();
		}
		
		// if either side is empty this show win state
		if (pebbles1 == 0 && player1sTurn) {
			gui.displayGameOver(2); // show player 2 wins
			gameOver = true;
		} else if (pebbles2 == 0 && !player1sTurn) {
			gui.displayGameOver(1); // show player 1 wins
			gameOver = true;
		}
	}

	
}
