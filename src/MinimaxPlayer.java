import java.util.ArrayList;
import java.util.List;

/**
 * 
 */

/**
 * @author Jared
 *
 */
public class MinimaxPlayer extends Player {
	int alpha = Integer.MIN_VALUE, beta = Integer.MAX_VALUE;

	public MinimaxPlayer(Boolean isPlayer1, int ply) {
		super(isPlayer1, ply);
	}

	@Override
	public int makeMove(int[] state) { // Alpha-beta search
		int val = Integer.MIN_VALUE, action = -1, depth = 1;
			
		
		for (int a : possibleActions(state, isPlayer1)) {
			int[] new_state = distributePebbles(state, a);
			int new_val = min(new_state, depth);
			
			if (new_val > val) {
				val = new_val;
				action = a;
			}
			
			if (val >= beta) { // prune based on beta
				return action;
			} else {
				if (val > alpha)  // update alpha
					alpha = val;
			}
		}
		
		return action;
	}
	
	private int min(int[] state, int depth) {
		if (terminal_test(state, !isPlayer1) || depth == ply)
			return utility(state);
		
		int val = Integer.MAX_VALUE;
		
		for (int a : possibleActions(state, !isPlayer1)) {
			int[] new_state = distributePebbles(state, a);
			// TODO if HashTable contains new_state, return val
			int new_val = max(new_state, depth + 1);
			// TODO add (new_state?, new_val) to HashTable
			
			if (new_val < val) // update val if new_val is smaller
				val = new_val;
			if (val <= alpha) // prune based on alpha
				return val;
			else if (val < beta) // update beta
				beta = val;
		}
		
		return val;
	}

	private int max(int[] state, int depth) {
		if (terminal_test(state, isPlayer1) || depth == ply)
			return utility(state);
		
		int val = Integer.MIN_VALUE;
		
		for (int a : possibleActions(state, isPlayer1)) {
			int[] new_state = distributePebbles(state, a);
			// TODO if HashTable contains new_state, return val
			int new_val = max(new_state, depth + 1);
			// TODO add (new_state?, new_val) to HashTable
			
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
