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
				{2, 0}
		};
		int[][] toTiles = {
				{1, 0},
				{1, 3}
		};
		int[][] results = {
				{},
				
		};
		for (int i=0; i<2; i++) {
			assertArrayEquals(sp.dragTile(fromTiles[i], toTiles[i]), results[i]);
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
	public void testCollectPathSwapsAlignedMatch() {
		/*String[] woMatchInBetween1 = {"GGGG", "GGGG", "RGGR", "RRGG"};
		int[] match = {3, 0, 3, 1};
		int[] occuranceToIdx = {2, 1};
		int[] occuranceFromIdx = {2, 3};
				
		SquareRemover s = new SquareRemover(4, "RGBY".toCharArray(), woMatchInBetween1, STARTSEED);
		SmartPlayer sp = s.createSmartPlayer();

		int[] correctMoves = new int[] {2, 3, 2, 2, 2, 2, 2, 1};
		// w/o match in between
		for (int i=0; i < 4; i++) {
			 int[] moves = sp.collectPathSwaps(match, occuranceToIdx, occuranceFromIdx);
			 s.rotateBoardCwise();
			 sp.rotateTileIndices(match);
			 sp.rotateTileIndices(occuranceToIdx);
			 sp.rotateTileIndices(occuranceFromIdx);
			 sp.rotateTileIndices(correctMoves);
			 
			 assertTrue(moves == correctMoves);
		}		
		
		// w/ match in between
		String[] wMatchInBetween1 = {"GGGG", "GGGG", "GRGR", "RRGG"};
		match = new int[] {3, 0, 3, 1};
		occuranceFromIdx = new int[] {2, 3};
		occuranceToIdx = new int[] {2, 0};
		
		s = new SquareRemover(4, "RGBY".toCharArray(), woMatchInBetween1, STARTSEED);
		sp = s.createSmartPlayer();
		
		correctMoves = new int[] {2, 3, 2, 2, 2, 1, 2, 0, 2, 2, 2, 1};
		*/
		
		SquareRemover s;
		SmartPlayer sp;
		String[][] hasMatchesInBetween = {
				{"GGGG", "GGGG", "RGGR", "RRGG"},
				{"GGGG", "GGGG", "GRGR", "RRGG"}
		};
		int[][] correctMoves = {
				{2, 3, 2, 2, 2, 2, 2, 1},
				{2, 3, 2, 2, 2, 1, 2, 0, 2, 2, 2, 1}
		};
		for (int i=0; i<2; i++) {
			int[] match = new int[] {3, 0, 3, 1};
			int[] occuranceToIdx = new int[] {2, i};
			int[] occuranceFromIdx = new int[] {2, 3};

			s = new SquareRemover(4, "RGBY".toCharArray(), hasMatchesInBetween[i], STARTSEED);
			sp = s.createSmartPlayer();
			
			for (int j=0; j<4; i++) {
				 int[] moves = sp.collectPathSwaps(match, occuranceToIdx, occuranceFromIdx);
				 s.rotateBoardCwise();
				 sp.rotateTileIndices(match);
				 sp.rotateTileIndices(occuranceToIdx);
				 sp.rotateTileIndices(occuranceFromIdx);
				 sp.rotateTileIndices(correctMoves[i]);
				 
				 assertTrue(moves == correctMoves[i]);
			}
		}
	}
	
	@Test
	public void testcollectPathSwapsUnalignedMatch() {
		fail("Not yet implemented");
	}

}
