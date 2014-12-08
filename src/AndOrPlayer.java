import java.util.ArrayList;

/**
 * 
 */

/**
 * @author Jared
 *
 */
public class AndOrPlayer extends Player {

	public AndOrPlayer(Boolean isPlayer1, int ply) {
		super(isPlayer1, ply);
	}

	@Override
	public int makeMove(int[] state) {
		int val = Integer.MIN_VALUE, action = -1, depth = 1;
		
		for (int a : possibleActions(state, isPlayer1)) {
			int[] new_state = distributePebbles(state, a);
			int new_val = and_search(new_state, depth, new ArrayList<int[]>());
			
			if (new_val > val) {
				val = new_val;
				action = a;
			}
		}
		
		return action;
	}

	private int or_search(int[] state, int depth, ArrayList<int[]> path) {
		if (terminal_test(state, !isPlayer1) || depth == ply)
			return utility(state);
		
		int val = Integer.MAX_VALUE;
	
		for (int a : possibleActions(state, !isPlayer1)) {
			int[] new_state = distributePebbles(state, a);
			int new_val;
			
			@SuppressWarnings("unchecked")
			ArrayList<int[]> new_path = (ArrayList<int[]>) path.clone();
//			if (!path.contains(new_state)) {
//				new_path.add(state);
//				
				new_val = and_search(new_state, depth + 1, new_path);	
//			} else {
//				new_val = utility(state);
//			}
			
			if (new_val < val) { // update val if new_val is lesser
				val = new_val;
			}
		}	
		
		return val;
	}

	private int and_search(int[] state, int depth, ArrayList<int[]> path) {
		if (terminal_test(state, !isPlayer1) || depth == ply)
			return utility(state);
		
		int val = Integer.MIN_VALUE;
	
		for (int a : possibleActions(state, !isPlayer1)) {
			int[] new_state = distributePebbles(state, a);
			int new_val;
			
			@SuppressWarnings("unchecked")
			ArrayList<int[]> new_path = (ArrayList<int[]>) path.clone();
//			if (!path.contains(new_state)) {
//				new_path.add(state);
//				
				new_val = or_search(new_state, depth + 1, new_path);	
//			} else {
//				new_val = utility(state);
//			}
			
			if (new_val > val) { // update val if new_val is greater
				val = new_val;
			}
		}	
		
		return val;
	}


}
