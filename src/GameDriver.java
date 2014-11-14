import gui.GameInterface;

/**
 * GameDriver.java
 * 
 * The driver of our AI program game. Creates the game board and GUI. Handles human and computer
 * players.
 * 
 * @author Jared Brown
 *
 */

public class GameDriver {
	public static int numSquares = 8, numPebbles = 4;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new GameInterface(numSquares, numPebbles);

	}

}
