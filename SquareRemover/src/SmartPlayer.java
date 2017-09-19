import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SmartPlayer {
	public String[] THEBOARD;
	public int COLORS;
	public char[] COLORSCHAR;

	public int[] THEMOVES;
	public SquareRemover SQRRMV;
	
	/*Class assumes the board has no match*/
	
	public static void main(String[] args) {
		
		
		String[] woMatches0 = {"RGBY", "BRYY", "RGRB", "RRGG"};
		String[] withMatches1 = {"RRGG", "RRBY", "YBBG", "GBBR"};
		String[] withMatches2 = {"YBYG", "GYYR", "RYYG", "BYYB"};
		String[] withMatches3 = {"RRRR", "RRRR", "RRRR", "RRRR"};
		
		//SmartPlayer s = new SmartPlayer(woMatches0, 4, "RGBY".toCharArray());
		//s.THEBUFFER = woMatches0;
		//s.STARTSEED = 1;
	}
	
//	public SmartPlayer(String[] theBoard, int colors, char[] colorsChar, SquareRemover squareRemover) {
	public SmartPlayer(SquareRemover squareRemover) {
		this.THEBOARD = squareRemover.THEBOARD;
		this.COLORS = squareRemover.COLORS;
		this.COLORSCHAR = squareRemover.COLORSCHAR;
		this.SQRRMV = squareRemover;
	}
	
	/* Returns pair of indices of tile placements on THEBOARD
	 * representing that tile's moves
	 * */
	int CHARWANTEDOCCURENCES = 0;
	char CHARWANTED = '\u0000';
	
	public int[] getMoves() {
		boolean isVertical = false;
		int[] match = existsMatch(); 	// returns left tile of horizontal match
										// returns top tile of vertical match
		 								// {[3/4], [2/4]}
		
		CHARWANTEDOCCURENCES = (match[0] == -1) ? 2 : 1;
		
		// search vertical match
		if (match[0] == -1 && match[2] == -1) {
			SQRRMV.rotateBoardCCwise();
			match = existsMatch();
			if (match[0] == -1 && match[2] == -1) {
				SQRRMV.rotateBoardCwise();
				return new int[] {-1, -1, -1, -1};
			}

			SQRRMV.rotateBoardCwise();
			
			// rotate match indices
			isVertical = true;
			if (match[2] == -1) {
				int j = match[0];
				match[0] = THEBOARD.length - 1 - match[1];
				match[1] = j;				
			} else {
				int j = match[2];
				match[2] = THEBOARD.length - 1 - match[3];
				match[3] = j;
			}

		}
		

		CHARWANTED = THEBOARD[(match[0] == -1) ? match[2] : match[0]].charAt((match[0] == -1) ? match[3] : match[1]);		
		// decide which index to drag occurance to
		int[] dragOccurencesTo = getMissingTilesOfMatch(match, CHARWANTED, isVertical); 	// {[1st loc],[2nd loc]}
															// {[1st loc], -1, -1} if a 3/4 match				
		
		// get missing tile indices for this match
		
		
		

		
		while (CHARWANTEDOCCURENCES > 0) {
			
			// find occurence
			//int row = (match[0] == -1) ? match[2] : match[0];
			int[] occurance = findOccurance(CHARWANTED);

			
			
			// calculate path of swaps
			
			
			// collect swap indices for path
			
			CHARWANTEDOCCURENCES -= 1;
		}
		
		return new int[] {-1, -1};
	}

	
	public int[] existsMatch() {
		int[] ret = new int[] {-1, -1, -1, -1}; // {[3/4], [2/4]}
		Pattern p = Pattern.compile("(([a-zA-Z])\\2)");
		Matcher m;
		for (int row = 0; row < THEBOARD.length-1; row++) {
			m = p.matcher(THEBOARD[row]);
			if (m.find()) {
				
				int[] incr = {-1, 1};
				int col = m.start();
				char wantedChar = THEBOARD[row].charAt(col);
				
				for (int j : incr) {
					if (0 <= (row+j) && (row+j) < THEBOARD.length) {
						
						// if 3/4 match, check if there's a 4th tile somewhere else in THEBOARD
						if (THEBOARD[row].charAt(col) == THEBOARD[row+j].charAt(col)) {
							if (charExists(row, 1, wantedChar)){
								return new int[] {row+j, col, -1, -1};
							}
							continue;
						}
						else if (THEBOARD[row].charAt(col) == THEBOARD[row+j].charAt(col+1)) {
							if (charExists(row, 1, wantedChar)){
								return new int[] {row+j, col+1, -1, -1};
							}
							continue;
						}
						
						// else check if valid 2/4 match
						if (charExists(row, 2, wantedChar)){
							return new int[] {-1, -1, row+j, col+1};
						}
					}
					
				}
				
			}
		}
		return ret;
	}
	
	// assumes match is horizontal match on THEBOARD
	public int[] getMissingTilesOfMatch(int[] match, char charWanted, boolean isVertical) {
		Pattern p = Pattern.compile(String.valueOf(charWanted));
		Matcher m;
		for (int row = 0; row < THEBOARD.length-1; row++) {
			m = p.matcher(THEBOARD[row]);
			if (m.find()) {
				return new int[] {row, m.start()};
			}
		}
		return new int[] {-1, -1};
	}
	
	public int[] collectPathSwaps(boolean isVertical, int[] match, int[] occurance) {
		if (isVertical) {
			
		}
	}
	
	/* returns index of charWanted that's not in row rowToAvoid
	 * */
	public int[] findOccurance(char charWanted) {
		Pattern p = Pattern.compile(String.valueOf(charWanted));
		Matcher m;
		for (int row = 0; row < THEBOARD.length-1; row++) {
			m = p.matcher(THEBOARD[row]);
			if (m.find()) {
				return new int[] {row, m.start()};
			}
		}
		return new int[] {-1, -1};
	}
	
	
	/* checks whether there are occurencesWanted occurences
	 * of charWanted not on the row rowToAvoid
	 * */
	public boolean charExists(int rowToAvoid, int occurencesWanted, char charWanted) {
		Pattern p = Pattern.compile(String.valueOf(charWanted));
		Matcher m;
		for (int row = 0; row < THEBOARD.length-1 && row != rowToAvoid; row++) {
			m = p.matcher(THEBOARD[row]);
			while (m.find() && occurencesWanted > 0) {
				occurencesWanted -= 1;
			}
		}
		return occurencesWanted == 0;
	}
}
