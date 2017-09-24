import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class Tester {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}
	
	// 1 test case
	@Test
	public void adjustScoreBruteTest() {
		// board with matches
		String[] withMatches0 = {"RGBY", "BRYY", "RGRB", "RRGG"};
		String[] withMatches1 = {"RRGG", "RRBY", "YBBG", "GBBR"};
		String[] withMatches2 = {"YBRG", "GYYR", "RYYG", "BYYB"};
		String[] withMatches3 = {"RRRR", "RRRR", "RRRR", "RRRR"};
		
		int startSeed = 1;
		SquareRemover s = new SquareRemover(4, "RGBY".toCharArray(), withMatches0, startSeed);
		//s.setTheBoard(withMatches0);
		int[] out = s.adjustScoreBrute();
		System.out.println(Arrays.toString(out));
		assertArrayEquals(out, new int[] {-1, -1, -1, -1, -1, -1, -1, -1});
		
		s.setTheBoard(withMatches1);
		out = s.adjustScoreBrute();
		System.out.println(Arrays.toString(out));
		assertArrayEquals(out, new int[] {0, 0, 2, 1, -1, -1, -1, -1});
		
		s.setTheBoard(withMatches2);
		out = s.adjustScoreBrute();
		System.out.println(Arrays.toString(out));
		assertArrayEquals(out, new int[] {1, 1, -1, -1, -1, -1, -1, -1});
		
		s.setTheBoard(withMatches3);
		out = s.adjustScoreBrute();
		System.out.println(Arrays.toString(out));
		assertArrayEquals(out, new int[] {0, 0, 0, 2, 2, 0, 2, 2});
	}

}
