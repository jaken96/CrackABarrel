import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

public class NodeTesting {

	@Test
	public void test() {
		// create board and a bunch of nodes
		GameBoard gb = new GameBoard(5);
		ArrayList<GameBoard.Node> actualNeighbors = gb.getNode(4).getNeighbors();
		ArrayList<GameBoard.Node> expectedNeighbors = new ArrayList<GameBoard.Node>();
		//{gb.getNode(5) ,gb.getNode(2), gb.getNode(8), null, gb.getNode(7), null};
		//assertArrayEquals(actualNeighbors, expectedNeighbors);
	}

	@Test
	public void test2() {
		int positionToCheck = 1;
		GameBoard gb = new GameBoard(5);
		gb.getNode(positionToCheck).setEmpty(true);
		System.out.println(gb.getNode(positionToCheck).getValidMoves().toString());
	}
}
