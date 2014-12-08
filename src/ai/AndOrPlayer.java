package ai;
import java.util.ArrayList;

/**
 * AndOrPlayer.java
 * 
 * The AI computer player using the And-Or Search algorithm.
 * 
 * @author Jared Brown & Matt Mathis
 *
 */
public class AndOrPlayer extends Player {

	/*
	 * And-Or Player constructor
	 */
	public AndOrPlayer(Boolean isPlayer1, int ply) {
		super(isPlayer1, ply);
	}

	/*
	 * Overrides the Player class method for makeMove. Receives the current game state and
	 *  implements the And-Or algorithm to select move to make and returns
	 *  the index of the pit corresponding to that move 
	 */
	@Override
	public int makeMove(int[] state) {
		ArrayList<Integer> plan = or_search(state, 0, new ArrayList<int[]>());
		return plan.get(0); // return the first move in the plan
	}
	
	/*
	 * The Or part of the And-Or search algorithm, which returns a conditional plan or failure 
	 */
	private ArrayList<Integer> or_search(int[] state, int depth, ArrayList<int[]> path) {
		@SuppressWarnings("unchecked")
		ArrayList<int[]> new_path = (ArrayList<int[]>) path.clone(); // copy the existing path
		ArrayList<Integer> plan = new ArrayList<Integer>();
		
		// check for goal state or if the # of plys has been reached
		if (depth >= ply || terminal_test(state, isPlayer1)) 
			return plan; // return empty plan
		
		if (!path.contains(state)) { // if state is not on the existing path
			for (int a : possibleActions(state, isPlayer1)) {
				int[] new_state = distributePebbles(state, a);
				new_path.add(new_state);
				plan = and_search(new_state, depth + 1, new_path);	
				
				if (!plan.contains(-1)) { // if plan does not result in a failure, then add action to plan
					plan.add(0, a);
					return plan;
				}
			}
		}
		
		plan.add(-1); // otherwise, return a failure plan
		return plan;
	}

	/*
	 * The And part of the And-Or search algorithm, which returns a conditional plan or failure 
	 */
	private ArrayList<Integer> and_search(int[] state, int depth, ArrayList<int[]> path) {
		ArrayList<Integer> plan = new ArrayList<Integer>();
		
		for (int a : possibleActions(state, !isPlayer1)) {
			int[] new_state = distributePebbles(state, a);
			plan = or_search(new_state, depth + 1, path);
			
			if (plan.contains(-1)) {
				return plan;
			}
			
			plan.add(0, a);
		}
		return plan;
	}


}
