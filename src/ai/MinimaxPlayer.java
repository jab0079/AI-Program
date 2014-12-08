package ai;

/**
 * MinimaxPlayer.java
 * 
 * The AI computer player using the Alpha-beta Minimax Search algorithm.
 * 
 * @author Jared Brown & Matt Mathis
 *
 */
public class MinimaxPlayer extends Player {
	private int alpha = Integer.MIN_VALUE, beta = Integer.MAX_VALUE;

	/*
	 * Minimax Player constructor
	 */
	public MinimaxPlayer(Boolean isPlayer1, int ply) {
		super(isPlayer1, ply);
	}

	/*
	 * Overrides the Player class method for makeMove. Receives the current game state and
	 *  implements the alpha-beta Minimax algorithm to select move to make and returns
	 *  the index of the pit corresponding to that move 
	 */
	@Override
	public int makeMove(int[] state) { // Alpha-beta search
		int val = Integer.MIN_VALUE, action = -1, depth = 1;
			
		// for each action for the player from the current state
		for (int a : possibleActions(state, isPlayer1)) {
			// find the new state resulting from that action & its heuristic value
			int[] new_state = distributePebbles(state, a); 
			int new_val = min(new_state, depth);
			
			if (new_val > val) { // if that action results in a higher valued state, then keep it
				val = new_val;
				action = a;
			}
			if (val >= beta)  // prune based on beta
				return action;
			else {
				if (val > alpha)  // update alpha
					alpha = val;
			}
		}
		
		return action;
	}
	
	/*
	 * The Min search part of Minimax, which receives a state and depth and returns
	 *  the move that would result in the minimum possible game state for the player.
	 *  If the state is a leaf or the search has reached its ply limit, then the value 
	 *  of the current state is returned.
	 */
	private int min(int[] state, int depth) {
		if (terminal_test(state, !isPlayer1) || depth == ply)
			return utility(state);
		
		int val = Integer.MAX_VALUE;
		
		for (int a : possibleActions(state, !isPlayer1)) {
			int[] new_state = distributePebbles(state, a);
			int new_val = max(new_state, depth + 1);
			
			if (new_val < val) // update val if new_val is smaller
				val = new_val;
			if (val <= alpha) // prune based on alpha
				return val;
			else if (val < beta) // update beta
				beta = val;
		}
		
		return val;
	}

	/*
	 * The Max search part of Minimax, which receives a state and depth and returns
	 *  the move that would result in the maximum possible game state for the player.
	 *  If the state is a leaf or the search has reached its ply limit, then the value 
	 *  of the current state is returned.
	 */
	private int max(int[] state, int depth) {
		if (terminal_test(state, isPlayer1) || depth == ply)
			return utility(state);
		
		int val = Integer.MIN_VALUE;
		
		for (int a : possibleActions(state, isPlayer1)) {
			int[] new_state = distributePebbles(state, a);
			int new_val = min(new_state, depth + 1);
			
			if (new_val > val) // update val if new_val is greater
				val = new_val;
			if (val >= beta) // prune based on beta
				return val;
			else if (val < alpha)  // update alpha
				alpha = val;
		}
		
		return val;
	}
	
}
