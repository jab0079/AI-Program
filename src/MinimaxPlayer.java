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

	public MinimaxPlayer(Boolean isPlayer1, int ply) {
		super(isPlayer1, ply);
	}

	@Override
	public int makeMove(int[] state) { // Alpha-beta search
		int val = Integer.MIN_VALUE, action = -1, depth = 1,
			alpha = Integer.MIN_VALUE, beta = Integer.MAX_VALUE;
		
		for (int a : possibleActions(state)) {
			int[] new_state = distributePebbles(state, a);
			int new_val = min(new_state, alpha, beta, depth);
			
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
	
	private int min(int[] state, int alpha, int beta, int depth) {
		if (terminal_test(state) || depth == ply)
			return utility(state);
		
		int val = Integer.MAX_VALUE, new_alpha = alpha, new_beta = beta;
		
		for (int a : possibleActions(state)) {
			int[] new_state = distributePebbles(state, a);
			// TODO if HashTable contains new_state, return val
			int new_val = max(new_state, new_alpha, new_beta, depth + 1);
			// TODO add (new_state?, new_val) to HashTable
			
			if (new_val < val) // update val if new_val is smaller
				val = new_val;
			if (val <= new_alpha) // prune based on alpha
				return val;
			else if (val < new_beta) // update beta
				new_beta = val;
		}
		
		return val;
	}

	private int max(int[] state, int alpha, int beta, int depth) {
		if (terminal_test(state) || depth == ply)
			return utility(state);
		
		int val = Integer.MIN_VALUE, new_alpha = alpha, new_beta = beta;
		
		for (int a : possibleActions(state)) {
			int[] new_state = distributePebbles(state, a);
			// TODO if HashTable contains new_state, return val
			int new_val = max(new_state, new_alpha, new_beta, depth + 1);
			// TODO add (new_state?, new_val) to HashTable
			
			if (new_val > val) // update val if new_val is greater
				val = new_val;
			if (val >= new_beta) // prune based on beta
				return val;
			else if (val < new_alpha)  // update alpha
				new_alpha = val;
		}
		
		return val;
	}
	
}
