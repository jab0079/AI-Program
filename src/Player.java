import java.util.ArrayList;
import java.util.List;



/**
 * Player.java
 * 
 * The AI computer player.
 * 
 * @author Jared Brown & Matt Mathis
 *
 */
public abstract class Player {
	Boolean isPlayer1;
	int ply;
	// HashTable transpositionTable
	
	public Player(Boolean isPlayer1, int ply) {
		this.isPlayer1 = isPlayer1;
		this.ply = ply;
	} 
	
	public abstract int makeMove(int[] state);
	
	public int utility(int[] state) {
		int utility = 0, x = state.length;
		
		if (isPlayer1) {
			for (int i = state.length / 2; i < state.length; i++) {
				utility += state[i] * (x-- / 2) * 10;
			}
		} else {
			for (int i = state.length / 2 - 1; i >= 0; i--) {
				utility += state[i] * (x-- / 2) * 10;
			}
		}
		
		return utility;
	}
	
	/*
	 * Distributes the pebbles at a given pit in a counter-clockwise fashion around
	 * the game board. 
	 */
	public int[] distributePebbles(int[] state, int a) {
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
	public int nextPitIndex(int state_length, int i) {
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
	
	public boolean terminal_test(int[] new_state) {
		int pebbles1 = 0, pebbles2 = 0;
		
		// get total pebbles for each player
		for (int i = 0; i < new_state.length / 2; i++) {
			pebbles2 += new_state[i];
			pebbles1 += new_state[i + (new_state.length / 2)];
		}
		
		// if either side is empty & that person goes next, then return true
		if (pebbles1 == 0 && !isPlayer1) // player 2 wins
			return true;
		else if (pebbles2 == 0 && isPlayer1)  // player 1 wins		
			return true;
		else
			return false;
	}

	public int[] possibleActions(int[] state) {
		List<Integer> list = new ArrayList<Integer>();
		
		if (isPlayer1) {
			for (int i = state.length / 2; i < state.length; i++) {
				if (state[i] != 0)
					list.add(i);
			}
		} else {
			for (int i = state.length / 2 - 1; i >= 0; i--) {
				if (state[i] != 0)
					list.add(i);
			}
		}
		
		int[] actions = new int[list.size()];
		for (int i = 0; i < list.size(); i++)
			actions[i] = list.get(i);
		
		return actions;
	}
}
