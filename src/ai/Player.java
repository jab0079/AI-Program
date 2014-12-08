package ai;
import java.util.ArrayList;
import java.util.List;

/**
 * Player.java
 * 
 * The AI computer player abstract class.
 * 
 * @author Jared Brown & Matt Mathis
 *
 */
public abstract class Player {
	protected Boolean isPlayer1;
	protected int ply;
	
	public Player(Boolean isPlayer1, int ply) {
		this.isPlayer1 = isPlayer1;
		this.ply = ply;
	} 
	
	/*
	 * Receives the current game state and returns the index of the pit corresponding to the move 
	 */
	public abstract int makeMove(int[] state);
	
	/*
	 * Finds the heuristic value of the game state. This function places value on the player having
	 *  pebbles in squares to the left of its POV. 
	 */
	protected int utility(int[] state) {
		int utility = 0, weight = state.length / 2;
		
		if (isPlayer1) {
			for (int i = state.length / 2; i < state.length; i++)
				utility += state[i] * weight-- * 10;
		} else {
			for (int i = state.length / 2 - 1; i >= 0; i--)
				utility += state[i] * weight-- * 10;
		}
		
		return utility;
	}
	
	/*
	 * Distributes the pebbles at a given pit in a counter-clockwise fashion around
	 * the game board. 
	 */
	protected int[] distributePebbles(int[] state, int a) {
		// take all the pebbles from the pit at index a
		int[] new_state = state.clone();
		int index = a, numPebbles = state[index];
		new_state[index] = 0;
		
		// distribute the pebbles one-by-one to each pit in a counter-clockwise order
		while (numPebbles > 0) {
			index = nextPitIndex(state.length, index);
			new_state[index] = new_state[index] + 1;
			numPebbles--;
		}
		
		return new_state;
	} 
	
	/*
	 * Gets the corrected index for the next pit in the counter-clockwise order. 
	 */
	private int nextPitIndex(int state_length, int i) {
		int numSquaresPerSide = state_length / 2;
		if (i == 0) {
			return numSquaresPerSide;
		} else if (i == state_length - 1) {
			return state_length - numSquaresPerSide - 1;
		} else if (i < numSquaresPerSide) {
			return i - 1;
		} else {
			return i + 1;
		}
	}
	
	/*
	 * Tests the state for a terminal state - that is one player is going next and has
	 *  no possible moves (no pebbles on their side). Returns true if the state means the
	 *  game is over, otherwise returns false.
	 */
	protected boolean terminal_test(int[] state, Boolean isP1sTurn) {
		int pebbles1 = 0, pebbles2 = 0;
		
		// get total pebbles for each player
		for (int i = 0; i < state.length / 2; i++) {
			pebbles2 += state[i];
			pebbles1 += state[i + (state.length / 2)];
		}
		
		// if either side is empty & that person goes next, then return true
		if (pebbles1 == 0 && isP1sTurn) // player 1 can't go, so player 2 wins
			return true;
		else if (pebbles2 == 0 && !isP1sTurn)  // player 2 can't go, so player 1 wins		
			return true;
		else
			return false;
	}

	/*
	 * Returns the possible actions for a given state and player. A player can move any
	 *  pit on his/her side if the pit contains pebbles. 
	 */
	protected int[] possibleActions(int[] state, Boolean isP1) {
		List<Integer> list = new ArrayList<Integer>();
		
		if (isP1) { // if player1's turn 
			for (int i = state.length / 2; i < state.length; i++) {
				if (state[i] != 0) // if pit contains pebbles, add it to list
					list.add(i);
			}
		} else { // if player2's turn
			for (int i = state.length / 2 - 1; i >= 0; i--) {
				if (state[i] != 0) // if pit contains pebbles, add it to list
					list.add(i);
			}
		}
		
		// generate and return array of possible moves 
		int[] actions = new int[list.size()];
		for (int i = 0; i < list.size(); i++)
			actions[i] = list.get(i);
		return actions;
	}
}
