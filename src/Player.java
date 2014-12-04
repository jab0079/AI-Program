

/**
 * Player.java
 * 
 * The AI computer player.
 * 
 * @author Jared Brown & Matt Mathis
 *
 */
public abstract class Player {
	int ply;
	// HashTable transpositionTable
	
	public Player(int ply) {} // constructor
	public abstract int utility(int[] state); // heuristic function
	public abstract int makeMove(int[] state);
}
