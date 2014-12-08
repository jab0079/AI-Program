package gui;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionListener;

import javax.swing.*;

/**
 * GameInterface.java
 * 
 * The simple GUI for our AI pebble game. Generates a board based on a number of
 * squares and pebbles selected by the user, as well as the computer player(s).
 * 
 * @author Jared Brown & Matt Mathis
 * @param <AIGame>
 *
 */

@SuppressWarnings("serial")
public class GameInterface<AIGame> extends JFrame  {
	public static final int[] DEFAULT_NUM_PITS = {2, 3, 4, 5, 6, 7, 8, 9, 10}, 
			DEFAULT_NUM_PEBBLES_PLYS = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
	public static final String[] DEFAULT_PLAYERS = new String[] {"Human",
			"Computer - AND-OR", "Computer - Minimax"};
	
	public JSplitPane splitPane;
	public JPanel options = new JPanel();
	public JPanel gameBoard = new JPanel();
	
	public JButton newGameButton = new JButton("New Game");
	public JButton runStepButton = new JButton("Step");
	
	private JLabel runLabel = new JLabel("Run?");
	public JCheckBox runStepCheckBox = new JCheckBox();
	
	private JLabel pitsLabel = new JLabel("Pits: ");
	public JComboBox<Integer> pitsComboBox = new JComboBox<Integer>();
	
	private JLabel pebblesLabel = new JLabel("Pebbles: ");
	public JComboBox<Integer> pebblesComboBox = new JComboBox<Integer>();
	
	private JLabel plyLabel = new JLabel("Ply: ");
	public JComboBox<Integer> plyComboBox = new JComboBox<Integer>();
	
	private JLabel player1Label = new JLabel("Player 1: ");
	public JComboBox<String> player1ComboBox = new JComboBox<String>(DEFAULT_PLAYERS);
	
	private JLabel player2Label = new JLabel("Player 2:");
	public JComboBox<String> player2ComboBox = new JComboBox<String>(DEFAULT_PLAYERS);
	
	public PitButton pits[];
	
	public GameInterface(AIGame game) {
		super("AI Pebble Game");
		setSize(1000, 500);
		setResizable(false);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		// setup menu options panel in gui
		setupMenuOptions(game);
				
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
	public void setupMenuOptions(AIGame game) {
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
		newGameButton.addActionListener((ActionListener) game);
		options.add(runStepButton);
		runStepButton.addActionListener((ActionListener) game);
		runStepButton.setEnabled(false);
		options.add(runLabel);
		options.add(runStepCheckBox);
//		runStepCheckBox.addActionListener((ActionListener) game);
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
	public void setupGameBoard(AIGame game, int numSquares, int numPebbles) {
		gameBoard.removeAll();
		gameBoard.setLayout(new GridLayout(2, numSquares / 2));
		
		pits = new PitButton[numSquares];
		for (int i = 0; i < numSquares; i++) {
			pits[i] = new PitButton(numPebbles, numSquares);
			pits[i].addActionListener((ActionListener) game);
			pits[i].setFont(new Font("Arial", Font.BOLD, 18));
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
	 * Distributes the pebbles at a given pit in a counter-clockwise fashion around
	 * the game board. 
	 */
	public int[] distributePebbles(int i) {
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
		
		// update and return pebbles game state array
		int[] newPebbles = new int[pits.length];
		for (int j = 0; j < pits.length; j++)
			newPebbles[j] = pits[j].getPebbles();
		return newPebbles;
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
	
	/*
	 * Display winner message for game over state.
	 */
	public void displayGameOver(int winner) {
		String str = "Player " + winner + " Wins!!!";
		JOptionPane.showMessageDialog(this,
			    str,
			    "GAME OVER",
			    JOptionPane.PLAIN_MESSAGE);
	}
}
