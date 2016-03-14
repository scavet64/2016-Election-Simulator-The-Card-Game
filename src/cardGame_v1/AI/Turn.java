package cardGame_v1.AI;

import java.util.Queue;

/**
 * The overall turn that holds a value which will be compared in the Minimax Algorithm. 
 * 
 * @author :^)
 *
 */
public class Turn {
	private double minimaxValue;
	private Queue<Play> moveQueue;
}
