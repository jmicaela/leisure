import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class SmartPlayerTester {
	int STARTSEED = 1;
	
	@Before
	public void setUp() {
		// not aligned
		String[] woMatchInBetween2 = {"GGGR", "GGGG", "RGGG", "RRGG"};
		String[] wMatchInBetween2 = {"GGGG", "GGGG", "GRGR", "RRGG"};		
	}

	@After
	public void tearDown() { 
	}
	
	@Test 
	public void testRotateTileIndices() {
		String[] board = {"GGGG", "GGGG", "GGGR", "GGGG"};
		SmartPlayer sp = new SquareRemover(4, "RGBY".toCharArray(), board, STARTSEED).createSmartPlayer();
		
		int[] tile = {2, 3};
		int[][] results = new int[][] {
				{3, 1},
				{1, 0},
				{0, 2},
				{2, 3}
		};
		for (int i=0; i<3; i++) {
			tile = sp.rotateTileIndices(tile);
			assertArrayEquals(tile, results[i]);			
		}
		
		// multiple tiles, not ordered
		sp.THEBOARD = new String[] {"GGGG", "GGGG", "GGRR", "GGGG"};
		tile = new int[] {2, 3, 2, 2};
		results = new int[][] {
			{3, 1, 2, 1},
			{1, 0, 1, 1},
			{0, 2, 1, 2},
			{2, 3, 2, 2}
		};
		for (int i=0; i<3; i++) {
			tile = sp.rotateTileIndices(tile);
			assertArrayEquals(tile, results[i]);			
		}
	}
	
	@Test
	public void testDragTile() {
		String[] board = {"GGGG", "RGGG", "GGGR", "GGGG"};
		SmartPlayer sp = new SquareRemover(4, "RGBY".toCharArray(), board, STARTSEED).createSmartPlayer();
		
		int[][] fromTiles = {
				{2, 3},
				{1, 0}
		};
		int[][] toTiles = {
				{2, 0},
				{1, 3}
		};
		int[][] results = {
				{2, 3, 2, 2, 2, 2, 2, 1, 2, 1, 2, 0},
				{1, 0, 1, 1, 1, 1, 1, 2, 1, 2, 1, 3}				
		};
		int[] moves;
		for (int i=0; i<2; i++) {
			moves = sp.dragTile(fromTiles[i], toTiles[i]);
			assertArrayEquals(results[i], moves);
		}
	}
	
	// test collectPathSwaps() where 1 missing tile already aligned to match
	// each case for when there's w/ and w/o matching tile in between
	 /*
	  * GGGG
	  * GGGG
	  * RGGR
	  * RRGG
	  * */
	@Test
	public void testCollectPathSwaps() {		
		SquareRemover s;
		SmartPlayer sp;
		String[][] boards = {
				{"GGGG", "GGGG", "RGGR", "RRGG"},
				{"GGGG", "GGGG", "GRGR", "RRGG"},
				{"GGGG", "GGGR", "GRGG", "RRGG"}
		};
		int[][] correctMoves = {
				{2, 3, 2, 2, 2, 2, 2, 1},
				{2, 1, 2, 0, 2, 3, 2, 2, 2, 2, 2, 1},
				{1, 3, 2, 3, 2, 1, 2, 0, 2, 3, 2, 2, 2, 2, 2, 1}
		};
		int[] match = new int[] {3, 0, 3, 1};
		int[][] occuranceFromIdx = {{2, 3}, {2, 3}, {1, 3}};
		int[][] occuranceToIdx = {{2, 1}, {2, 0}, {2, 0}};
		
		for (int i=0; i<3; i++) {
			s = new SquareRemover(4, "RGBY".toCharArray(), boards[i], STARTSEED);
			sp = s.createSmartPlayer();
			
			assertArrayEquals(correctMoves[i], sp.collectPathSwaps(match, occuranceFromIdx[i], occuranceToIdx[i]));
			/*for (int j=0; j<4; i++) {
				 int[] moves = sp.collectPathSwaps(match, occuranceToIdx, occuranceFromIdx);
				 s.rotateBoardCwise();
				 sp.rotateTileIndices(match);
				 sp.rotateTileIndices(occuranceToIdx);
				 sp.rotateTileIndices(occuranceFromIdx);
				 sp.rotateTileIndices(correctMoves[i]);				 
				 assertTrue(moves == correctMoves[i]);
			}*/
		}
	}

}
